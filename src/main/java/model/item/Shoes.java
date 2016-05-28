package model.item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */
public class Shoes extends Item {
    private String brand;
    private String fabric;
    private String color;
    private int size;
    private Map<String, String> extendedFields;

    public Map<String, String> getExtendedFields() {
        if (this.extendedFields == null) {
            this.extendedFields = new HashMap<>();
        }

        this.extendedFields.put("brand", brand);
        this.extendedFields.put("fabric", fabric);
        this.extendedFields.put("color", color);
        this.extendedFields.put("size", Integer.toString(this.size));

        return this.extendedFields;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFabric() {
        return fabric;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
