package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.CustomerReader;
import com.deoxys.dev.dstr.domain.converter.OrderReader;
import com.deoxys.dev.dstr.domain.model.Customer;
import com.deoxys.dev.dstr.domain.model.Item;
import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.persistence.dao.ItemDAO;
import com.deoxys.dev.dstr.persistence.dao.OrderDAO;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

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

    public long count() {
        return orderDao.count();
    }

    public void loadOrders(HttpServletRequest req) {
    }

    public void loadCustomerOrder(HttpServletRequest req) {

    }

    public void loadCustomerOrders(HttpServletRequest req) {
        CustomerReader customerReader = new CustomerReader();
        Customer customer = customerReader.read(req.getSession());
        List<Order> orders = orderDao.getAllForCustomer(customer.getEmail());
        req.setAttribute("orders", orders);
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
         * from this Order meanwhile, it may be some losses.
         *
         * Decision to be made is whether to verify if that's true.
         */
        if (itemDao.enoughItems(order.getItems())) {
            order.setOrderNumber(orderDao.getNextSequence("orderNumber"));
            order.setDate(new Date());
            order.setStatus(Order.OrderStatus.IN_PROCESS);
            orderDao.add(order);
            ses.removeAttribute("order");
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
    }

    public void changeOrderStatus(HttpServletRequest req) {
        String orderId = req.getParameter("orderId");
        String status = req.getParameter("orderNewStatus");
        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status);
        Order order = orderDao.get(orderId);
        Order.OrderStatus oldStatus = order.getStatus();

        if (newStatus.equals(oldStatus)) return;
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
        logger.info("Order's with id=" + orderId + " status was changed: "
                + oldStatus + " --> " + newStatus);
    }
}
