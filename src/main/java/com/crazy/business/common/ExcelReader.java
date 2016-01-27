package com.crazy.business.common;

import com.google.common.base.Strings;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Author: crazy.jack
 * Date:   15-11-23
 */
public final class ExcelReader extends AbstractIterator<Row> implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

    private InputStream inputStream;

    private Iterator<Row> rowIterator;

    private ExcelReader(ExcelReaderBuilder excelReaderBuilder) throws IOException {
        this.inputStream = excelReaderBuilder.inputStream;
        Workbook workbook = buildWorkbook(excelReaderBuilder.fileName, inputStream);
        List<Integer> sheetNumList = buildSheetNumList(excelReaderBuilder.sheetNumList, workbook);
        buildRowIterator(excelReaderBuilder.skipFirstRow, workbook, sheetNumList);
    }

    private List<Integer> buildSheetNumList(List<Integer> sheetNumList, Workbook workbook) {
        if (CollectionUtils.isEmpty(sheetNumList)) {
            sheetNumList = Lists.newArrayList();
            for (int i = 0 ; i < workbook.getNumberOfSheets(); i++) {
                sheetNumList.add(i);
            }
        }
        return sheetNumList;
    }

    private Workbook buildWorkbook(String fileName, InputStream inputStream) throws IOException {
        if (Strings.isNullOrEmpty(fileName) || inputStream == null) {
            throw new RuntimeException("文件名不能为空或者输入流不能为空！");
        }
        if (fileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(inputStream);
        } else if (fileName.endsWith(".xls")) {
            return new HSSFWorkbook(inputStream);
        } else {
            throw new RuntimeException("Excel文件名必须以xls或者xlsx结尾");
        }
    }

    private void buildRowIterator(boolean skipFirstRow, Workbook workbook, List<Integer> sheetNumList) {
        List<Iterator<Row>> iteratorList = Lists.newArrayList();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            if (!sheetNumList.contains(i)) {
                continue;
            }
            final Iterator<Row> iterator = workbook.getSheetAt(i).iterator();
            iteratorList.add(!skipFirstRow ? iterator : new AbstractIterator<Row>() {
                boolean first = true;
                @Override
                protected Row computeNext() {
                    // 用于跳过第一行
                    if (first) {
                        first = false;
                        if (!iterator.hasNext()) {
                            return endOfData();
                        }
                        iterator.next();
                    }
                    return iterator.hasNext() ? iterator.next() : endOfData();
                }
            });
        }
        rowIterator = Iterators.concat(iteratorList.iterator());
    }

    @Override
    protected Row computeNext() {
        return rowIterator.hasNext() ? rowIterator.next() : endOfData();
    }

    @Override
    public void close() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("关闭文件发生异常!", e);
            }
        }
    }

    public static class ExcelReaderBuilder {
        private final String fileName;

        private final InputStream inputStream;

        private boolean skipFirstRow;

        private List<Integer> sheetNumList;

        public ExcelReaderBuilder(String fileName, InputStream inputStream) {
            this.fileName = fileName;
            this.inputStream = inputStream;
        }

        public ExcelReaderBuilder setSkipFirstRow(boolean skipFirstRow) {
            this.skipFirstRow = skipFirstRow;
            return this;
        }

        public ExcelReaderBuilder setSheetNumList(List<Integer> sheetNumList) {
            this.sheetNumList = sheetNumList;
            return this;
        }

        public ExcelReader build() throws IOException {
            return new ExcelReader(this);
        }
    }
}