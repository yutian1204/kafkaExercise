package com.crazy.business.service;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.crazy.business.common.ExcelReader;
import com.crazy.business.model.DbConnectionModel;
import com.crazy.business.model.InsertModel;
import com.crazy.business.model.SqlValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Author: crazy.jack
 * Date:   15-11-24
 */
@Service
public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    @Resource
    private DbService dbService;
    @Resource
    private CellSqlValueService cellValueService;

    public void insert(DbConnectionModel dbConnectionModel, MultipartFile file) {
        try {
            Map<String, String> columnToType = dbService.getColumnToTypeMap(dbConnectionModel);
            InsertModel insertModel = makeInsertModel(file, columnToType);
            dbService.insert(dbConnectionModel, insertModel);
        } catch (Exception e) {
            logger.error("发生异常！", e);
            throw Throwables.propagate(e);
        }
    }

    private InsertModel makeInsertModel(MultipartFile file, Map<String, String> columnToType) {
        ExcelReader excelReader = null;
        try {
            excelReader = new ExcelReader.ExcelReaderBuilder(file.getOriginalFilename(), file.getInputStream())
                    .setSkipFirstRow(false)
                    .setSheetNumList(ImmutableList.of(0))
                    .build();
            List<String> columns = getColumnList(excelReader);
            List<List<SqlValue>> rows = Lists.newArrayList();
            while (excelReader.hasNext()) {
                Row row = excelReader.next();
                List<SqlValue> results = Lists.newArrayList();
                for (int i = 0; i < columns.size(); i++) {
                    String column = columns.get(i);
                    Cell cell = row.getCell(i);
                    String columnType = columnToType.get(column);
                    SqlValue result = cellValueService.get(columnType, cell);
                    results.add(result);
                }
                rows.add(results);
            }
            InsertModel insertModel = new InsertModel();
            insertModel.setColumns(columns);
            insertModel.setRows(rows);
            return insertModel;
        } catch (Exception e) {
            logger.error("发生异常！", e);
            throw Throwables.propagate(e);
        } finally {
            if (excelReader != null) {
                excelReader.close();
            }
        }
    }

    private List<String> getColumnList(ExcelReader excelReader) {
        List<String> columns = Lists.newArrayList();
        if (excelReader.hasNext()) {
            Row row = excelReader.next();
            for (int i = 0; i < row.getLastCellNum(); i++) {
                String currentColumn = row.getCell(i).getStringCellValue();
                columns.add(currentColumn);
            }
        }
        return columns;
    }
}
