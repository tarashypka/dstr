package com.deoxys.dev.dstr.domain.model;

import java.io.Serializable;

public class ItemStatus implements Serializable {

    /**
     * int vs Integer
     *   MongoDB Java driver takes and produces Integer wrapper type
     *
     * Thus, in order to avoid redundant autoboxing, Integer will be better
     */
    private Integer stocked;
    private Integer reserved;
    private Integer sold;

    public ItemStatus(Integer stocked, Integer reserved, Integer sold) {
        this.stocked = stocked;
        this.reserved = reserved;
        this.sold = sold;
    }

    public Integer getStocked() {
        return stocked;
    }

    public void setStocked(Integer stocked) {
        this.stocked = stocked;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public void changeStocked(Integer quantity) {
        this.stocked += quantity;
    }

    public void changeReserved(Integer quantity) {
        this.reserved += quantity;
    }

    public void changeSold(Integer quantity) {
        this.sold += quantity;
    }

    @Override
    public String toString() {
        return "{ stocked=" + stocked +
               ", reserved=" + reserved +
               ", sold=" + sold + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemStatus status = (ItemStatus) o;

        if (stocked != null ? !stocked.equals(status.stocked) : status.stocked != null) return false;
        if (reserved != null ? !reserved.equals(status.reserved) : status.reserved != null) return false;
        return sold != null ? sold.equals(status.sold) : status.sold == null;

    }

    @Override
    public int hashCode() {
        int result = stocked != null ? stocked.hashCode() : 0;
        result = 31 * result + (reserved != null ? reserved.hashCode() : 0);
        result = 31 * result + (sold != null ? sold.hashCode() : 0);
        return result;
    }
}