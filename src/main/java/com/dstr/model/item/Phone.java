package com.dstr.model.item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */
public class Phone extends Item {
    private String producer;
    private String model;
    private Map<String, String> extendedFields;

    public Map<String, String> getExtendedFields() {
        if (this.extendedFields == null) {
            this.extendedFields = new HashMap<>();
        }

        this.extendedFields.put("producer", this.producer);
        this.extendedFields.put("model", this.model);

        return this.extendedFields;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
