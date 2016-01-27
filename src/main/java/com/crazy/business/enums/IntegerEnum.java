package com.crazy.business.enums;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
public enum IntegerEnum {

    TINYINT("tinyint"),
    SMALLINT("smallint"),
    MEDIUMINT("mediumint"),
    INT("int"),
    BIGINT("bigint");

    private String name;

    IntegerEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
