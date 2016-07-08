package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.model.Order;
import com.deoxys.dev.dstr.persistence.dao.MongoOrderDAO;
import org.apache.log4j.Logger;

/**
 * Created by deoxys on 07.07.16.
 */

public class OrderService extends MongoService {

    private MongoOrderDAO orderDao;
    Logger logger = Logger.getLogger(OrderService.class);

    public OrderService() {
        super();
        this.orderDao = new MongoOrderDAO(super.mongo);
    }

    public Order makeOrder(Order order) {
        ItemService itemService = new ItemService();
        itemService.takeOrderItemsFromStock(order.getItems());
        itemService.addOrderItemsToReserve(order.getItems());
        orderDao.insertOrder(order);
        logger.info("New order with id=" + order.getId() + " was inserted");
        return order;
    }

    public boolean changeOrderStatus(Order order, Order.OrderStatus newStatus) {
        Order.OrderStatus oldStatus = order.getStatus();
        if (newStatus.equals(oldStatus)) return true;
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
                return false;
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
                return false;
        }
        // orderDao.updateOrderStatus(order.getId(), newStatus)
        return true;
    }

    public long count() {
        return orderDao.count();
    }
}
