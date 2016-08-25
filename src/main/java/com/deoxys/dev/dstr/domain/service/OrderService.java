package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.OrderReader;
import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.domain.model.OrderStatus;
import com.deoxys.dev.dstr.domain.model.User;
import com.deoxys.dev.dstr.persistence.dao.ItemDAO;
import com.deoxys.dev.dstr.persistence.dao.OrderDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

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
        } else loadAllOrders(req);
    }

    public void loadCustomerOrders(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("customer");
        long id = user.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : user.getId();
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

    private void loadAllOrders(HttpServletRequest req) {
        req.setAttribute("orders", orderDao.getAll());
    }

    public void loadCustomerActivity(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("customer");
        long id = user.isAdmin()
                ? Long.parseLong(req.getParameter("id"))
                : user.getId();
        req.setAttribute("nItems", orderDao.countCustomerItems(id));
        req.setAttribute("nOrders", orderDao.countCustomerOrders(id));
    }

    public void addItemsToOrder(HttpServletRequest req) {
        HttpSession ses = req.getSession();
        Order order = sessionReader.read(ses);
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String value = req.getParameter(param);
            if (! value.isEmpty() && param.matches("^[0-9a-fA-F]{24}$")) {
                /**
                 * Better to access DB once and get the whole Item,
                 * then to access it twice getting Item stocked status first,
                 * and then (not always, but in most cases) the whole Item.
                 */
                Item item = itemDao.get(param);
                int quantity = Integer.parseInt(req.getParameter(param));
                if (item.stocked() >= quantity) {
                    ItemService itemService = new ItemService();
                    itemService.takeOrderItemFromStock(item, quantity);
                    itemService.addOrderItemToReserve(item, quantity);

                    /**
                     * receipt should be updated before new item was added,
                     * since updateReceipt() checks whether such item already exists in order
                     */
                    order.updateReceipt(item, quantity);
                    order.addItem(item, quantity);
                    ses.setAttribute("order", order);
                } else {
                    req.setAttribute("error", "Not enough items in stock.");
                    return;
                }
            }
        }
    }

    public void dropItemFromOrder(HttpServletRequest req) {
        HttpSession ses = req.getSession();
        Order order = sessionReader.read(ses);
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (param.matches("^[0-9a-fA-F]{24}$")) {
                Item item = order.getItem(param);
                int quantity = order.getItems().get(item);
                ItemService itemService = new ItemService();
                itemService.takeOrderItemFromReserve(item, quantity);
                itemService.addOrderItemToStock(item, quantity);

                /**
                 * receipt should be updated before new item was removed,
                 * since updateReceipt() checks whether such item exists in order
                 */
                order.updateReceipt(item, 0);
                order.removeItem(item, quantity);
                if (order.getItems().size() == 0) ses.removeAttribute("order");
                else ses.setAttribute("order", order);
            }
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
            order.setStatus(OrderStatus.IN_PROCESS);
            orderDao.add(order);
            ses.removeAttribute("order");
            req.setAttribute("order", order);
        } else {
            /**
             * Customer would like to know, which Item is a problem.
             * Link to that Item will be sufficient.
             */
            req.setAttribute("error", "Not enough items in stock.");
        }
    }

    public void declineOrder(HttpServletRequest req) {
        HttpSession ses = req.getSession();
        Order order = sessionReader.read(ses);
        ItemService itemService = new ItemService();
        itemService.takeOrderItemsFromReserve(order.getItems());
        itemService.addOrderItemsToStock(order.getItems());
        ses.removeAttribute("order");
    }

    public void swapOrderStatus(HttpServletRequest req) {
        String id = req.getParameter("id");

        /**
         * Possible solutions:
         *      load OrderStatus, if it's valid, then load Order
         *      load Order
         */
        Order order = orderDao.get(id);
        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = oldStatus.equals(OrderStatus.REJECTED)
                        ? OrderStatus.IN_PROCESS
                        : OrderStatus.REJECTED;

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
        OrderStatus newStatus = OrderStatus.valueOf(status);

        /**
         * Possible solutions:
         *      load OrderStatus, if it's valid, then load Order
         *      load Order
         */
        Order order = orderDao.get(id);
        OrderStatus oldStatus = order.getStatus();
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
