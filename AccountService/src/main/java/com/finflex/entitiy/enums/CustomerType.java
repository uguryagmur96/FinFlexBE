package com.finflex.entitiy.enums;

public enum CustomerType {
    B ("Bireysel"),
    K ("Kurumsal");

    private final String description;

    CustomerType(String description){this.description = description;}

    public String getDescription() {
        return description;
    }
}
