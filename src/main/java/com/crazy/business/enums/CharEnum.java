package com.crazy.business.enums;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
public enum CharEnum {

    CHAR("char"),
    VARCHAR("varchar"),
    TINYBLOB("tinyblob"),
    TINYTEXT("tinytext"),
    BLOB("blob"),
    TEXT("text"),
    MEDIUMBLOB("mediumblob"),
    MEDIUMTEXT("mediumtext"),
    LONGBLOB("longblob"),
    LONGTEXT("longtext");

    private String name;

    CharEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
