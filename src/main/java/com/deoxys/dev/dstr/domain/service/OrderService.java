package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.OrderReader;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.persistence.dao.ItemDAO;
import com.deoxys.dev.dstr.persistence.dao.OrderDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by deoxys on 07.07.16.
 */

public class OrderService extends MongoService<Order> {
    Logger logger = Logger.getLogger(OrderService.class);

    private OrderDAO orderDao;
    private ItemDAO itemDao;

    public OrderService() {
        super(new OrderReader());
        orderDao = new OrderDAO(mongo);
        itemDao = new ItemDAO(mongo);
    }

    private static final String DATE_FORMAT;

    static {
        DATE_FORMAT = "yyyy-MM-dd";
    }

    public long count() {
        return orderDao.count();
    }

    public void loadOrder(HttpServletRequest req) {
        String id = req.getParameter("id");
        req.setAttribute("order", orderDao.get(id));
    }

    public void loadOrders(HttpServletRequest req) {
        String filter = req.getParameter("filter");
        if (filter != null) {
            switch (filter) {
                case "date":
                    loadOrdersInPeriod(req);
                    break;
                default:
                    break;
            }
        } else req.setAttribute("orders", orderDao.getAll());
    }

    public void loadCustomerOrders(HttpServletRequest req) {
        Customer customer = (Customer) req.getSession().getAttribute("customer");
        long id = customer.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : customer.getId();
        req.setAttribute("orders", orderDao.getAllForCustomer(id));
    }

    private void loadOrdersInPeriod(HttpServletRequest req) {
        String fromStr = req.getParameter("from");
        String tillStr = req.getParameter("till");
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            Date from = format.parse(fromStr);
            Date till = format.parse(tillStr);
            req.setAttribute("orders", orderDao.getAllInPeriod(from, till));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public void loadCustomerActivity(HttpServletRequest req) {
        Customer customer = (Customer) req.getSession().getAttribute("customer");
        long id = customer.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : customer.getId();
        req.setAttribute("nItems", orderDao.countCustomerItems(id));
        req.setAttribute("nOrders", orderDao.countCustomerOrders(id));
    }

    public void addItemToOrder(HttpServletRequest req) {
        HttpSession ses = req.getSession();
        Order order = sessionReader.read(ses);
        String itemId = req.getParameter("id");
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        /**
         * It's better to access Database only once and get the whole Item,
         * then to access it twice getting the Item stocked status first,
         * and then (not always, but in most cases) the whole Item.
         */
        Item item = itemDao.get(itemId);
        if (item.stocked() >= quantity) {
            order.updateReceipt(order, item, quantity);
            order.addItem(item, quantity);
            ses.setAttribute("order", order);
        } else {
            req.setAttribute("error", "Not enough items in stock.");
        }
    }

    public void makeOrder(HttpServletRequest req) {
        HttpSession ses = req.getSession();
        Order order = sessionReader.read(ses);

        /**
         * Since another Customer could have been ordered any Item
         * from this Order meanwhile, it may be some conflicts.
         *
         * Decision to be made is whether to verify if that's true.
         */
        if (itemDao.enoughItems(order.getItems())) {
            order.setOrderNumber(orderDao.getNextSequence("orderNumber"));
            order.setDate(new Date());
            order.setStatus(Order.OrderStatus.IN_PROCESS);
            orderDao.add(order);
            ses.removeAttribute("order");
            req.setAttribute("order", order);
            logger.info("New order=" + order + " has been made");
        } else {
            /**
             * Customer would like to know, which Item is a problem.
             * Link to that Item will be sufficient.
             */
            req.setAttribute("error", "Not enough items in stock.");
        }
    }

    public void swapOrderStatus(HttpServletRequest req) {
        String id = req.getParameter("id");

        /**
         * Possible solutions:
         *      load OrderStatus, if it's valid, then load Order
         *      load Order
         */
        Order order = orderDao.get(id);
        Order.OrderStatus oldStatus = order.getStatus();
        Order.OrderStatus newStatus = oldStatus.equals(Order.OrderStatus.REJECTED)
                        ? Order.OrderStatus.IN_PROCESS
                        : Order.OrderStatus.REJECTED;

        /**
         * To change order status, items statuses should be changed
         * and
         * orderDao.get(id) returns order with items that only have id field.
         */
        itemDao.expandOrderItems(order);

        ItemService itemService = new ItemService();
        switch (oldStatus) {
            case IN_PROCESS:
                itemService.takeOrderItemsFromReserve(order.getItems());
                itemService.addOrderItemsToStock(order.getItems());
                break;
            case REJECTED:
                itemService.takeOrderItemsFromStock(order.getItems());
                itemService.addOrderItemsToReserve(order.getItems());
                break;
            default:
                return;
        }
        orderDao.updateStatus(id, newStatus);
        req.setAttribute("order", orderDao.get(id));
        logger.info("Order's with id=" + id + " status was changed: "
                + oldStatus + " --> " + newStatus);
    }

    public void changeOrderStatus(HttpServletRequest req) {
        String id = req.getParameter("id");
        String status = req.getParameter("status");
        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status);

        /**
         * Possible solutions:
         *      load OrderStatus, if it's valid, then load Order
         *      load Order
         */
        Order order = orderDao.get(id);
        Order.OrderStatus oldStatus = order.getStatus();
        if (newStatus.equals(oldStatus)) {
            req.setAttribute("order", order);
            return;
        }

        /**
         * To change order status, items statuses should be changed
         * and
         * orderDao.get(id) returns order with items that only have id field.
         */
        itemDao.expandOrderItems(order);

        ItemService itemService = new ItemService();
        switch (oldStatus) {
            case REJECTED:
                itemService.takeOrderItemsFromStock(order.getItems());
                break;
            case IN_PROCESS:
                itemService.takeOrderItemsFromReserve(order.getItems());
                break;
            case PROCESSED:
                itemService.takeOrderItemsFromSold(order.getItems());
                break;
            default:
                return;
        }
        switch (newStatus) {
            case REJECTED:
                itemService.addOrderItemsToStock(order.getItems());
                break;
            case IN_PROCESS:
                itemService.addOrderItemsToReserve(order.getItems());
                break;
            case PROCESSED:
                itemService.addOrderItemsToSold(order.getItems());
                break;
            default:
                return;
        }
        orderDao.updateStatus(order.getId(), newStatus);
        req.setAttribute("order", orderDao.get(id));
        logger.info("Order's with id=" + id + " status was changed: "
                + oldStatus + " --> " + newStatus);
    }
}
