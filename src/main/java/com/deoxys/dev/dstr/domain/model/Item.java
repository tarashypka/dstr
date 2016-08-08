package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;
import java.util.*;

public class Item implements Serializable {
    private String id;
    private String name;

    /**
     * double vs Double
     *   MongoDB Java driver takes and produces Double wrapper type
     *
     * Thus, in order to avoid redundant autoboxing, Double will be better
     */
    private Double price;

    private Currency currency;
    private ItemStatus status;
    private List<String> tags = new ArrayList<>();
    private Map<String, String> extendedFields = new LinkedHashMap<>();

    private String errType;

    public Item() { }

    public Item(String id) {
        this.id = id;
    }

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(String name, double price, Currency currency, ItemStatus status) {
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Integer stocked() {
        return status.getStocked();
    }

    public Integer reserved() {
        return status.getReserved();
    }

    public Integer sold() {
        return status.getSold();
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getExtendedFields() {
        return extendedFields;
    }

    public void setExtendedFields(Map<String, String> extendedFields) {
        this.extendedFields = extendedFields;
    }

    public void addField(String k, String v) {
        extendedFields.put(k, v);
    }

    public String getErrType() {
        return errType;
    }

    public void setErrType(String errType) {
        this.errType = errType;
    }

    @Override
    public String toString() {
        return  "{ id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", currency=" + currency +
                ", status=" + status +
                ", extendedFields=" + extendedFields +
                ", tags=" + tags + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return id != null ? id.equals(item.id) : item.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
