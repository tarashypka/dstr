package model;

import model.item.Item;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by deoxys on 27.05.16.
 */
public class Order {
    private String id;
    private String orderNumber;
    private Customer customer;
    private List<ObjectId> itemsIds;
    private int totalSum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<ObjectId> getItemsIds() {
        return itemsIds;
    }

    public void setItemsIds(List<ObjectId> itemsIds) {
        this.itemsIds = itemsIds;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }
}