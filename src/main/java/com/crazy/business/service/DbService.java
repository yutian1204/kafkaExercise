package com.crazy.business.service;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.crazy.business.enums.SqlValueTypeEnum;
import com.crazy.business.model.DbConnectionModel;
import com.crazy.business.model.InsertModel;
import com.crazy.business.model.SqlValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static com.crazy.business.enums.SqlValueTypeEnum.NUMBER;
import static com.crazy.business.enums.SqlValueTypeEnum.STRING;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
@Service
public class DbService {
    private static final Logger logger = LoggerFactory.getLogger(DbService.class);

    private static final Joiner JOINER = Joiner.on(",");
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://%s:%d/%s?zeroDateTimeBehavior=convertToNull&autoReconnect=true";
    private static final String META_SQL = "SELECT COLUMN_NAME, DATA_TYPE FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ?;";
    private static final String INSERT_SQL = "INSERT INTO %s.%s %s VALUES %s;";

    private Connection getConnection(DbConnectionModel dbConnectionModel) {
        try {
            String url = String.format(URL, dbConnectionModel.getHost(), dbConnectionModel.getPort(), dbConnectionModel.getDb());
            Class.forName(DRIVER);
            return DriverManager.getConnection(url, dbConnectionModel.getUser(), dbConnectionModel.getPassword());
        } catch (Exception e) {
            logger.error("获取connection发生异常！", e);
            throw Throwables.propagate(e);
        }
    }

    /**
     * 获取一张数据库表的字段名称和类型的映射
     *
     * @param dbConnectionModel 对象
     * @return 映射
     */
    public Map<String, String> getColumnToTypeMap(DbConnectionModel dbConnectionModel) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(dbConnectionModel);
            preparedStatement = (PreparedStatement) connection.prepareStatement(META_SQL);
            preparedStatement.setString(1, dbConnectionModel.getDb());
            preparedStatement.setString(2, dbConnectionModel.getTable());
            resultSet = preparedStatement.executeQuery();
            Map<String, String> columnToType = Maps.newHashMap();
            while (resultSet.next()) {
                columnToType.put(resultSet.getString(1), resultSet.getString(2));
            }
            return columnToType;
        } catch (Exception e) {
            logger.error("getColumnToTypeMap occurs exception!", e);
            throw Throwables.propagate(e);
        } finally {
            try {
                if (resultSet != null) {
                    preparedStatement.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                logger.error("close connection occurs exception!", e);
            }
        }
    }

    /**
     * 将数据插入指定数据库
     *
     * @param dbConnectionModel 数据库信息对象
     * @param insertModel       插入对象
     */
    public void insert(DbConnectionModel dbConnectionModel, InsertModel insertModel) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection(dbConnectionModel);
            connection.setAutoCommit(false);
            List<List<List<SqlValue>>> partitions = Lists.partition(insertModel.getRows(), 1000);
            for (List<List<SqlValue>> partition : partitions) {
                String valueList = makeSqlValueListStr(partition);
                String columns = "(" + JOINER.join(insertModel.getColumns()) + ")";
                String realSql = String.format(INSERT_SQL, dbConnectionModel.getDb(), dbConnectionModel.getTable(), columns, valueList);
                preparedStatement = connection.prepareStatement(realSql);
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (Exception e) {
            logger.error("realInsert occurs exception!", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    logger.error("rollback occurs exception!", e);
                }
            }
            throw Throwables.propagate(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                logger.error("close connection occurs exception!", e);
            }
        }
    }

    private String makeSqlValueListStr(List<List<SqlValue>> rowSqlValueList) {
        List<String> rows = Lists.newArrayList();
        for (List<SqlValue> row : rowSqlValueList) {
            List<String> columnValues = Lists.newArrayList();
            for (SqlValue sqlValue : row) {
                SqlValueTypeEnum sqlValueTypeEnum = Preconditions.checkNotNull(sqlValue.getSqlValueTypeEnum(), "");
                if (sqlValueTypeEnum == STRING) {
                    columnValues.add("'" + sqlValue.getValueStr() + "'");
                } else if (sqlValueTypeEnum == NUMBER) {
                    columnValues.add(sqlValue.getValueStr());
                }
            }
            rows.add("(" + JOINER.join(columnValues) + ")");
        }
        return JOINER.join(rows);
    }


    public static void main(String[] args) {
        DbService dbService = new DbService();
        DbConnectionModel dbConnectionModel = new DbConnectionModel();
        dbConnectionModel.setHost("l-qinvoice.qss.dev.cn6.qunar.com");
        dbConnectionModel.setPort(3306);
        dbConnectionModel.setDb("qinvoice");
        dbConnectionModel.setTable("invoices");
        dbConnectionModel.setUser("dev");
        dbConnectionModel.setPassword("qunar.com1234");
        try {
            Map<String, String> columnToTypeMap = dbService.getColumnToTypeMap(dbConnectionModel);
            System.out.println();
        } catch (Exception e) {
            logger.error("error;{}", e);
        }
    }
}
