package com.crazy.business.model;

import com.crazy.business.enums.SqlValueTypeEnum;

/**
 * Author: crazy.jack
 * Date:   15-11-26
 */
public class SqlValue {

    private String valueStr;

    private SqlValueTypeEnum sqlValueTypeEnum;

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public SqlValueTypeEnum getSqlValueTypeEnum() {
        return sqlValueTypeEnum;
    }

    public void setSqlValueTypeEnum(SqlValueTypeEnum sqlValueTypeEnum) {
        this.sqlValueTypeEnum = sqlValueTypeEnum;
    }
}
