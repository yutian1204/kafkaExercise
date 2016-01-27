package com.crazy.business.enums;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
public enum FloatEnum {

    FLOAT("float"),
    DOUBLE("double"),
    DECIMAL("decimal");

    private String name;

    FloatEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
