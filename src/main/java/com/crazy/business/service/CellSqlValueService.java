package com.crazy.business.service;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.crazy.business.enums.*;
import com.crazy.business.model.SqlValue;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
@Service
public class CellSqlValueService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(CellSqlValueService.class);

    private static final List<Transformer> TRANSFORMER_LIST = Lists.newArrayList();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-DD HH:mm:ss");

    @Override
    public void afterPropertiesSet() throws Exception {
        Transformer intTransformer = new Transformer() {
            @Override
            public SqlValue transform(String columnType, Cell cell) {
                for (IntegerEnum integerEnum : IntegerEnum.values()) {
                    if (StringUtils.equals(integerEnum.getName(), columnType) ||
                            StringUtils.equals(integerEnum.getName().toUpperCase(), columnType)) {
                        SqlValue sqlValue = new SqlValue();
                        sqlValue.setSqlValueTypeEnum(SqlValueTypeEnum.NUMBER);
                        sqlValue.setValueStr(String.valueOf(Double.valueOf(cell.getNumericCellValue()).longValue()));
                        return sqlValue;
                    }
                }
                return null;
            }
        };
        TRANSFORMER_LIST.add(intTransformer);
        Transformer floatTransformer = new Transformer() {
            @Override
            public SqlValue transform(String columnType, Cell cell) {
                for (FloatEnum floatEnum : FloatEnum.values()) {
                    if (StringUtils.equals(floatEnum.getName(), columnType) ||
                            StringUtils.equals(floatEnum.getName().toUpperCase(), columnType)) {
                        SqlValue sqlValue = new SqlValue();
                        sqlValue.setSqlValueTypeEnum(SqlValueTypeEnum.NUMBER);
                        sqlValue.setValueStr(String.valueOf(cell.getNumericCellValue()));
                        return sqlValue;
                    }
                }
                return null;
            }
        };
        TRANSFORMER_LIST.add(floatTransformer);
        Transformer charTransformer = new Transformer() {
            @Override
            public SqlValue transform(String columnType, Cell cell) {
                for (CharEnum charEnum : CharEnum.values()) {
                    if (StringUtils.equals(charEnum.getName(), columnType) ||
                            StringUtils.equals(charEnum.getName().toUpperCase(), columnType)) {
                        SqlValue sqlValue = new SqlValue();
                        sqlValue.setSqlValueTypeEnum(SqlValueTypeEnum.STRING);
                        sqlValue.setValueStr(cell.getStringCellValue());
                        return sqlValue;
                    }
                }
                return null;
            }
        };
        TRANSFORMER_LIST.add(charTransformer);
        Transformer timeTransformer = new Transformer() {
            @Override
            public SqlValue transform(String columnType, Cell cell) {
                for (TimeEnum timeEnum : TimeEnum.values()) {
                    if (StringUtils.equals(timeEnum.getName(), columnType) ||
                            StringUtils.equals(timeEnum.getName().toUpperCase(), columnType)) {
                        String timeStr = cell.getStringCellValue().trim();
                        // 校验cell中的时间类型是否符合格式要求
                        DATE_TIME_FORMATTER.parseDateTime(timeStr).toDate();
                        SqlValue sqlValue = new SqlValue();
                        sqlValue.setSqlValueTypeEnum(SqlValueTypeEnum.STRING);
                        sqlValue.setValueStr(timeStr);
                        return sqlValue;
                    }
                }
                return null;
            }
        };
        TRANSFORMER_LIST.add(timeTransformer);
    }

    /**
     *
     *
     * @param columnType 数据库字段类型
     * @param cell excel的cell
     * @return 解析出来的值
     */
    public SqlValue get(String columnType, Cell cell) {
        try {
            SqlValue result = null;
            for (Transformer transformer : TRANSFORMER_LIST) {
                SqlValue transform = transformer.transform(columnType, cell);
                if (transform != null) {
                    result = transform;
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("Get SqlValue from cell by columnType occurs exception!", e);
            throw Throwables.propagate(e);
        }
    }

    interface Transformer {
        SqlValue transform(String columnType, Cell cell);
    }
}
