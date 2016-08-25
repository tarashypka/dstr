package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;
import java.util.*;

public class Item implements Serializable {

    private String id;
    private String name;
    private Price price;
    private ItemStatus status;
    private String errType;

    private List<String> tags;

    private Map<String, String> extendedFields = new LinkedHashMap<>();

    public static class ItemBuilder implements Builder<Item> {
        // required fields
        private String name;

        // optional fields
        private String id;
        private Price price;
        private ItemStatus status;
        private String errType;

        private List<String> tags = new ArrayList<>();

        private Map<String, String> extendedFields = new LinkedHashMap<>();

        public ItemBuilder(String name) {
            this.name = name;
        }

        public ItemBuilder withId(String id) {
            this.id = id;
            return this;
        }
        public ItemBuilder withPrice(Price price) {
            this.price = price;
            return this;
        }
        public ItemBuilder withStatus(ItemStatus status) {
            this.status = status;
            return this;
        }
        public ItemBuilder withErrType(String errType) {
            this.errType = errType;
            return this;
        }
        public ItemBuilder withTags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        public ItemBuilder withExtFields(Map<String, String> extFields) {
            this.extendedFields = extFields;
            return this;
        }

        @Override
        public Item build() {
            return new Item(this);
        }
    }

    private Item(ItemBuilder builder) {
        id = builder.id;
        name = builder.name;
        price = builder.price;
        status = builder.status;
        errType = builder.errType;
        tags = builder.tags;
        extendedFields = builder.extendedFields;
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

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
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
