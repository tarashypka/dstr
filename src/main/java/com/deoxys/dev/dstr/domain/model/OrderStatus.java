package com.deoxys.dev.dstr.domain.model;

public enum OrderStatus {
    REJECTED(-1), IN_PROCESS(0), PROCESSED(+1);

    /**
     * int vs Integer
     *   MongoDB Java driver takes and produces Integer wrapper type
     *
     * Thus, in order to avoid redundant autoboxing, Integer will be better
     */
    private Integer value;
    private String name;    // for easy referring with EL

    OrderStatus(int value) {
        this.value = value;
    }

    public static OrderStatus getStatus(Integer value) {
        switch (value) {
            case -1:
                return OrderStatus.REJECTED;
            case 0:
                return OrderStatus.IN_PROCESS;
            case +1:
                return OrderStatus.PROCESSED;
            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return value == -1 ? "Rejected" : (value == 0 ? "In process" : "Processed");
    }
}
