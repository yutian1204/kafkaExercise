package com.crazy.business.enums;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
public enum TimeEnum {

    DATE("date"),
    TIME("time"),
    YEAR("year"),
    DATETIME("datetime"),
    TIMESTAMP("timestamp");

    private String name;

    TimeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
