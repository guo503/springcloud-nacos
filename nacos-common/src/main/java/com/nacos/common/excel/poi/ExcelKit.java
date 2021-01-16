/**
 * Copyright (c) 2017, 吴汶泽 (wuwz@live.com).
 * <p>
 * <p>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p>
 * you may not use this file except in compliance with the License.
 * <p>
 * You may obtain a copy of the License at
 * <p>
 * <p>
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * <p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * <p>
 * distributed under the License is distributed on an "AS IS" BASIS,
 * <p>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p>
 * See the License for the specific language governing permissions and
 * <p>
 * limitations under the License.
 */
package com.nacos.common.excel.poi;


import com.nacos.common.excel.poi.annotation.ExportConfig;
import com.nacos.common.excel.poi.convert.ExportConvert;
import com.nacos.common.excel.poi.convert.ExportRange;
import com.nacos.common.excel.poi.core.POIUtils;
import com.nacos.common.excel.poi.core.XlsxReader;
import com.nacos.common.excel.poi.hanlder.ExportHandler;
import com.nacos.common.excel.poi.hanlder.ReadHandler;
import com.nacos.common.excel.poi.pojo.ExportItem;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * ExcelKit.java : Excel快速导入导出工具
 * 简单、好用且轻量级的海量Excel文件导入导出解决方案. (仅提供excel2007以及更高版本的支持)
 * <p>
 *
 * @author wuwz
 * @since 2017年4月10日
 */
public class ExcelKit {
    private static Logger log = Logger.getLogger(ExcelKit.class);

    private Class<?> mClass = null;
    private HttpServletResponse mResponse = null;
    // 默认以此值填充空单元格,可通过 setEmptyCellValue(string)改变其默认值。
    private String mEmptyCellValue = null;
    // 分Sheet机制：每个Sheet最多多少条数据
    private Integer mMaxSheetRecords = 10000;
    // 缓存数据格式器实例,避免多次使用反射进行实例化
    private static final Map<String, ExportConvert> mConvertInstanceCache = new HashMap<>();
    private static final Map<String, ExportRange> mRangeInstanceCache = new HashMap<>();

    protected ExcelKit() {
    }

    protected ExcelKit(Class<?> clazz) {
        this(clazz, null);
    }

    protected ExcelKit(Class<?> clazz, HttpServletResponse response) {
        this.mResponse = response;
        this.mClass = clazz;
    }

    /**
     * 用于生成本地文件
     *
     * @param clazz 实体Class对象
     * @return ExcelKit
     */
    public static ExcelKit $Builder(Class<?> clazz) {
        return new ExcelKit(clazz);
    }

    /**
     * 用于浏览器导出
     *
     * @param clazz    实体Class对象
     * @param response 原生HttpServletResponse对象
     * @return ExcelKit
     */
    public static ExcelKit $Export(Class<?> clazz, HttpServletResponse response) {
        return new ExcelKit(clazz, response);
    }

    /**
     * 用于导入数据解析
     *
     * @return ExcelKit
     */
    public static ExcelKit $Import() {
        return new ExcelKit();
    }

    /**
     * 读取Excel时以该值填充空单元格值 (默认null)
     *
     * @param emptyCellValue 单元格值
     * @return this
     */
    public ExcelKit setEmptyCellValue(String emptyCellValue) {
        this.mEmptyCellValue = emptyCellValue;
        return this;
    }

    /**
     * 分Sheet机制：每个Sheet最多多少条数据(默认10000)
     *
     * @param size 数据条数
     * @return this
     */
    public ExcelKit setMaxSheetRecords(Integer size) {
        this.mMaxSheetRecords = size;
        return this;
    }

    /**
     * 导出Excel(此方式需依赖浏览器实现文件下载,故应先使用$ExportRange()构造器)
     *
     * @param data      数据集合
     * @param sheetName 工作表名字
     * @return true-操作成功,false-操作失败
     */
    public boolean toExcel(List<?> data, String sheetName) {
        required$ExportParams();

        try {
            return toExcel(data, sheetName, mResponse.getOutputStream());
        } catch (IOException e) {
            log.error("导出Excel失败:" + e.getMessage(), e);
        }
        return false;
    }

    /**
     * 针对转换方法的默认实现(提供默认样式和文件命名规则)
     *
     * @param data      数据集合
     * @param sheetName 工作表名字
     * @param out       输出流
     * @return true-操作成功,false-操作失败
     */
    public boolean toExcel(List<?> data, String sheetName, OutputStream out) {

        return toExcel(data, sheetName, new ExportHandler() {

            @Override
            public CellStyle headCellStyle(SXSSFWorkbook wb) {
                CellStyle cellStyle = wb.createCellStyle();
                Font font = wb.createFont();
                cellStyle.setFillForegroundColor((short) 12);
                cellStyle.setFillPattern(cellStyle.getFillPatternEnum());// 填充模式
                cellStyle.setBorderTop(cellStyle.getBorderTopEnum());// 上边框为细边框
                cellStyle.setBorderRight(cellStyle.getBorderRightEnum());// 右边框为细边框
                cellStyle.setBorderBottom(cellStyle.getBorderBottomEnum());// 下边框为细边框
                cellStyle.setBorderLeft(cellStyle.getBorderLeftEnum());// 左边框为细边框
                cellStyle.setAlignment(cellStyle.getAlignmentEnum());// 对齐
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillBackgroundColor(HSSFColor.GREEN.index);
                //font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                // font.setFontHeightInPoints((short) 12);// 字体大小
                font.setColor(HSSFColor.BLACK.index);
                // 应用标题字体到标题样式
                cellStyle.setFont(font);
                //设置单元格文本形式
                DataFormat dataFormat = wb.createDataFormat();
                cellStyle.setDataFormat(dataFormat.getFormat("@"));
                return cellStyle;
            }

            @Override
            public String exportFileName(String sheetName) {
                return String.format("导出-%s-%s", sheetName, System.currentTimeMillis());
            }
        }, out);
    }

    public boolean toExcelWithoutSuffix(List<?> data, String sheetName) {
        required$ExportParams();

        try {
            return toExcelWithoutSuffix(data, sheetName, mResponse.getOutputStream());
        } catch (IOException e) {
            log.error("导出Excel失败:" + e.getMessage(), e);
        }
        return false;
    }

    public boolean toExcelWithoutSuffix(List<?> data, String sheetName, OutputStream out) {

        return toExcel(data, sheetName, new ExportHandler() {

            @Override
            public CellStyle headCellStyle(SXSSFWorkbook wb) {
                CellStyle cellStyle = wb.createCellStyle();
                Font font = wb.createFont();
                cellStyle.setFillForegroundColor((short) 12);
                cellStyle.setFillPattern(cellStyle.getFillPatternEnum());// 填充模式
                cellStyle.setBorderTop(cellStyle.getBorderTopEnum());// 上边框为细边框
                cellStyle.setBorderRight(cellStyle.getBorderRightEnum());// 右边框为细边框
                cellStyle.setBorderBottom(cellStyle.getBorderBottomEnum());// 下边框为细边框
                cellStyle.setBorderLeft(cellStyle.getBorderLeftEnum());// 左边框为细边框
                cellStyle.setAlignment(cellStyle.getAlignmentEnum());// 对齐
                cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
                cellStyle.setFillBackgroundColor(HSSFColor.GREEN.index);
               // font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                // font.setFontHeightInPoints((short) 12);// 字体大小
                font.setColor(HSSFColor.BLACK.index);
                // 应用标题字体到标题样式
                cellStyle.setFont(font);
                //设置单元格文本形式
                org.apache.poi.ss.usermodel.DataFormat dataFormat = wb.createDataFormat();
                cellStyle.setDataFormat(dataFormat.getFormat("@"));
                return cellStyle;
            }

            @Override
            public String exportFileName(String sheetName) {
                return sheetName;
            }
        }, out);
    }

    public boolean toExcel(List<?> data, String sheetName, ExportHandler handler, OutputStream out) {
        required$BuilderParams();
        long begin = System.currentTimeMillis();
        if (data == null) {
            log.error("没有检测到数据,不执行导出操作。");
            return false;
        }

        log.info(String.format("即将导出excel数据：%s条,请稍后..", data.size()));

        // 导出列查询。
        ExportConfig currentExportConfig;
        ExportItem currentExportItem;
        List<ExportItem> exportItems = new ArrayList<>();
        for (Field field : mClass.getDeclaredFields()) {
            currentExportConfig = field.getAnnotation(ExportConfig.class);
            if (currentExportConfig != null) {
                currentExportItem = new ExportItem()
                        .setField(field.getName())
                        .setDisplay("field".equals(currentExportConfig.value()) ? field.getName() : currentExportConfig.value())
                        .setWidth(currentExportConfig.width())
                        .setConvert(currentExportConfig.convert())
                        .setFormat(currentExportConfig.format())
                        .setRange(currentExportConfig.range())
                        .setCellType(currentExportConfig.cellType())
                        .setReplace(currentExportConfig.replace());
                exportItems.add(currentExportItem);
            }
        }

        // 创建新的工作薄。
        SXSSFWorkbook wb = POIUtils.newSXSSFWorkbook();

        double sheetNo = Math.ceil(data.size() / mMaxSheetRecords);// 取出一共有多少个sheet.

        // =====多sheet生成填充数据=====
        for (int index = 0; index <= (sheetNo == 0.0 ? sheetNo : sheetNo - 1); index++) {
            SXSSFSheet sheet = POIUtils.newSXSSFSheet(wb, sheetName + (index == 0 ? "" : "_" + index));

            // 创建表头
            SXSSFRow headerRow = POIUtils.newSXSSFRow(sheet, 0);
            for (int i = 0; i < exportItems.size(); i++) {
                ExportItem exportItem = exportItems.get(i);
                SXSSFCell cell = POIUtils.newSXSSFCell(headerRow, i);
                POIUtils.setColumnWidth(sheet, i, exportItem.getWidth(), exportItem.getDisplay());
                cell.setCellValue(exportItem.getDisplay());
                CellStyle style = handler.headCellStyle(wb);
                if (style != null) {
                    cell.setCellStyle(style);
                }
                //设置下拉列表 下拉格式不是针对整列的需要指定生效的行数 所以我设置的是和数据数量一样的行数，也可以改成mMaxSheetRecords,如果要生成导出模板我的建议增加一个注解这个数量 设置成变量
                String range = exportItem.getRange();
                if (!"".equals(range)) {
                    String[] ranges = rangeCellValues(range);
                    POIUtils.setHSSFValidation(sheet, ranges, 1, data.size(), i, i);
                }
            }

            // 产生数据行
            if (data.size() > 0) {
                int startNo = index * mMaxSheetRecords;
                int endNo = Math.min(startNo + mMaxSheetRecords, data.size());
                //生成Excel表格body各列的样式
                CellStyle[] cellStyles = new CellStyle[exportItems.size()];
                for (int j = 0; j < exportItems.size(); j++) {
                    org.apache.poi.ss.usermodel.DataFormat dataFormat = wb.createDataFormat();
                    CellStyle contextStyle = wb.createCellStyle();
                    contextStyle.setDataFormat(dataFormat.getFormat(exportItems.get(j).getFormat()));
                    cellStyles[j] = contextStyle;
                }

                //生成表格body各行
                for (int i = startNo; i < endNo; i++) {
                    SXSSFRow bodyRow = POIUtils.newSXSSFRow(sheet, i + 1 - startNo);
                    for (int j = 0; j < exportItems.size(); j++) {
                        ExportItem exportItem = exportItems.get(j);
                        SXSSFCell cell = POIUtils.newSXSSFCell(bodyRow, j);
                        cell.setCellStyle(cellStyles[j]);
                        //单元格内容数据类型
                        short cellType = exportItem.getCellType();
                        cell.setCellType(cellType);

                        // 处理单元格值
                        String cellValue = exportItem.getReplace();
                        if ("".equals(cellValue)) {
                            try {
                                cellValue = BeanUtils.getProperty(data.get(i), exportItem.getField());
                            } catch (Exception e) {
                                log.error("获取" + exportItems.get(j).getField() + "的值失败.", e);
                            }
                        }
                        // 单元格值转换
                        if (!"".equals(exportItem.getConvert())) {
                            cellValue = convertCellValue(cellValue, exportItem.getConvert());
                        }

                        //cellValue为null时，设置单元格格式为空白
                        if (cellValue == null) {
                            cell.setCellType(Cell.CELL_TYPE_BLANK);
                            cell.setCellValue(cellValue);
                            continue;
                        }
                        if (cellType == 0) {
                            cell.setCellValue(Double.parseDouble(cellValue));
                        } else {
                            cell.setCellValue(cellValue);
                        }
                    }
                }
            }
        }
        try {
            // 生成Excel文件并下载.(通过response对象是否为空来判定是使用浏览器下载还是直接写入到output中)
            POIUtils.writeByLocalOrBrowser(mResponse, handler.exportFileName(sheetName), wb, out);
        } catch (Exception e) {
            log.error("生成Excel文件失败:" + e.getMessage(), e);
            return false;
        }
        log.info(String.format("Excel处理完成,共生成数据:%s行 (不包含表头),耗时：%s seconds.", (data != null ? data.size() : 0),
                (System.currentTimeMillis() - begin) / 1000F));
        return true;
    }

    public boolean getExcel(HashMap<String, List<?>> dataMap, String sheetName, ExportHandler handler, OutputStream out) {
        SXSSFWorkbook wb = POIUtils.newSXSSFWorkbook();
        Iterator<Map.Entry<String, List<?>>> iterator = dataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<?>> entry = iterator.next();
            String key = entry.getKey();
            List<?> data = entry.getValue();
            toExcel(wb, data, sheetName + key, handler, out);
        }
        try {
            // 生成Excel文件并下载.(通过response对象是否为空来判定是使用浏览器下载还是直接写入到output中)
            POIUtils.writeByLocalOrBrowser(mResponse, handler.exportFileName(sheetName), wb, out);
        } catch (Exception e) {
            log.error("生成Excel文件失败:" + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public boolean toExcel(SXSSFWorkbook wb, List<?> data, String sheetName, ExportHandler handler, OutputStream out) {
        required$BuilderParams();
        if (data == null) {
            log.error("没有检测到数据,不执行导出操作。");
            return false;
        }
        log.info(String.format("即将导出excel数据：%s条,请稍后..", data.size()));
        // 导出列查询。
        ExportConfig currentExportConfig;
        ExportItem currentExportItem;
        List<ExportItem> exportItems = new ArrayList<>();
        for (Field field : mClass.getDeclaredFields()) {
            currentExportConfig = field.getAnnotation(ExportConfig.class);
            if (currentExportConfig != null) {
                currentExportItem = new ExportItem()
                        .setField(field.getName())
                        .setDisplay("field".equals(currentExportConfig.value()) ? field.getName() : currentExportConfig.value())
                        .setWidth(currentExportConfig.width())
                        .setConvert(currentExportConfig.convert())
                        .setFormat(currentExportConfig.format())
                        .setRange(currentExportConfig.range())
                        .setCellType(currentExportConfig.cellType())
                        .setReplace(currentExportConfig.replace());
                exportItems.add(currentExportItem);
            }
        }

        SXSSFSheet sheet = POIUtils.newSXSSFSheet(wb, sheetName);
        // 创建表头
        SXSSFRow headerRow = POIUtils.newSXSSFRow(sheet, 0);
        for (int i = 0; i < exportItems.size(); i++) {
            ExportItem exportItem = exportItems.get(i);
            SXSSFCell cell = POIUtils.newSXSSFCell(headerRow, i);
            POIUtils.setColumnWidth(sheet, i, exportItem.getWidth(), exportItem.getDisplay());
            cell.setCellValue(exportItem.getDisplay());
            CellStyle style = handler.headCellStyle(wb);
            if (style != null) {
                cell.setCellStyle(style);
            }
            //设置下拉列表 下拉格式不是针对整列的需要指定生效的行数 所以我设置的是和数据数量一样的行数，也可以改成mMaxSheetRecords,如果要生成导出模板我的建议增加一个注解这个数量 设置成变量
            String range = exportItem.getRange();
            if (!"".equals(range)) {
                String[] ranges = rangeCellValues(range);
                POIUtils.setHSSFValidation(sheet, ranges, 1, data.size(), i, i);
            }
        }

        // 产生数据行
        if (data.size() > 0) {
            int startNo = 0;
            int endNo = Math.min(startNo + mMaxSheetRecords, data.size());
            //生成Excel表格body各列的样式
            CellStyle[] cellStyles = new CellStyle[exportItems.size()];
            for (int j = 0; j < exportItems.size(); j++) {
                org.apache.poi.ss.usermodel.DataFormat dataFormat = wb.createDataFormat();
                CellStyle contextStyle = wb.createCellStyle();
                contextStyle.setDataFormat(dataFormat.getFormat(exportItems.get(j).getFormat()));
                cellStyles[j] = contextStyle;
            }

            //生成表格body各行
            for (int i = startNo; i < endNo; i++) {
                SXSSFRow bodyRow = POIUtils.newSXSSFRow(sheet, i + 1 - startNo);
                for (int j = 0; j < exportItems.size(); j++) {
                    ExportItem exportItem = exportItems.get(j);
                    SXSSFCell cell = POIUtils.newSXSSFCell(bodyRow, j);
                    cell.setCellStyle(cellStyles[j]);
                    //单元格内容数据类型
                    short cellType = exportItem.getCellType();
                    cell.setCellType(cellType);

                    // 处理单元格值
                    String cellValue = exportItem.getReplace();
                    if ("".equals(cellValue)) {
                        try {
                            cellValue = BeanUtils.getProperty(data.get(i), exportItem.getField());
                        } catch (Exception e) {
                            log.error("获取" + exportItems.get(j).getField() + "的值失败.", e);
                        }
                    }
                    // 单元格值转换
                    if (!"".equals(exportItem.getConvert())) {
                        cellValue = convertCellValue(cellValue, exportItem.getConvert());
                    }

                    //cellValue为null时，设置单元格格式为空白
                    if (cellValue == null) {
                        cell.setCellType(Cell.CELL_TYPE_BLANK);
                        cell.setCellValue(cellValue);
                        continue;
                    }
                    if (cellType == 0) {
                        cell.setCellValue(Double.parseDouble(cellValue));
                    } else {
                        cell.setCellValue(cellValue);
                    }
                }
            }
        }
        return true;
    }


    @Deprecated
    public boolean toExcelOld(List<?> data, String sheetName, ExportHandler handler, OutputStream out) {
        required$BuilderParams();
        long begin = System.currentTimeMillis();

        if (data == null) {
            log.error("没有检测到数据,不执行导出操作。");
            return false;
        }

        log.info(String.format("即将导出excel数据：%s条,请稍后..", data.size()));

        // 导出列查询。
        ExportConfig currentExportConfig;
        ExportItem currentExportItem;
        List<ExportItem> exportItems = new ArrayList<>();
        for (Field field : mClass.getDeclaredFields()) {
            currentExportConfig = field.getAnnotation(ExportConfig.class);
            if (currentExportConfig != null) {
                currentExportItem = new ExportItem()
                        .setField(field.getName())
                        .setDisplay("field".equals(currentExportConfig.value()) ? field.getName() : currentExportConfig.value())
                        .setWidth(currentExportConfig.width())
                        .setConvert(currentExportConfig.convert())
//                        .setColor(currentExportConfig.color())
                        .setRange(currentExportConfig.range())
                        .setCellType(currentExportConfig.cellType())
                        .setReplace(currentExportConfig.replace());
                exportItems.add(currentExportItem);
            }
        }

        // 创建新的工作薄。
        SXSSFWorkbook wb = POIUtils.newSXSSFWorkbook();

        double sheetNo = Math.ceil(data.size() / mMaxSheetRecords);// 取出一共有多少个sheet.

        // =====多sheet生成填充数据=====
        for (int index = 0; index <= (sheetNo == 0.0 ? sheetNo : sheetNo - 1); index++) {
            SXSSFSheet sheet = POIUtils.newSXSSFSheet(wb, sheetName + (index == 0 ? "" : "_" + index));

            // 创建表头
            SXSSFRow headerRow = POIUtils.newSXSSFRow(sheet, 0);
            for (int i = 0; i < exportItems.size(); i++) {
                SXSSFCell cell = POIUtils.newSXSSFCell(headerRow, i);
                POIUtils.setColumnWidth(sheet, i, exportItems.get(i).getWidth(), exportItems.get(i).getDisplay());
                cell.setCellValue(exportItems.get(i).getDisplay());
                CellStyle style = handler.headCellStyle(wb);
                if (style != null) {
                    cell.setCellStyle(style);
                }
                //设置下拉列表 下拉格式不是针对整列的需要指定生效的行数 所以我设置的是和数据数量一样的行数，也可以改成mMaxSheetRecords,如果要生成导出模板我的建议增加一个注解这个数量 设置成变量
                String range = exportItems.get(i).getRange();
                if (!"".equals(range)) {
                    String[] ranges = rangeCellValues(range);
                    POIUtils.setHSSFValidation(sheet, ranges, 1, data.size(), i, i);
                }
            }

            // 产生数据行
            if (data.size() > 0) {
                int startNo = index * mMaxSheetRecords;
                int endNo = Math.min(startNo + mMaxSheetRecords, data.size());
                for (int i = startNo; i < endNo; i++) {
                    SXSSFRow bodyRow = POIUtils.newSXSSFRow(sheet, i + 1 - startNo);
                    for (int j = 0; j < exportItems.size(); j++) {
                        org.apache.poi.ss.usermodel.DataFormat dataFormat = wb.createDataFormat();
                        // 处理单元格值
                        String cellValue = exportItems.get(j).getReplace();
                        if ("".equals(cellValue)) {
                            try {
                                cellValue = BeanUtils.getProperty(data.get(i), exportItems.get(j).getField());
                            } catch (Exception e) {
                                log.error("获取" + exportItems.get(j).getField() + "的值失败.", e);
                            }
                        }

                        // 格式化单元格值
                        if (!"".equals(exportItems.get(j).getConvert())) {
                            cellValue = convertCellValue(cellValue, exportItems.get(j).getConvert());
                        }

                        // 单元格宽度
                        POIUtils.setColumnWidth(sheet, j, exportItems.get(j).getWidth(), cellValue);

                        SXSSFCell cell = POIUtils.newSXSSFCell(bodyRow, j);
                        //单元格内容数据类型
                        short cellType = exportItems.get(j).getCellType();
                        //data是否为数值型
                        boolean isNum = false;
                        //data是否为整数
                        boolean isInteger = false;
                        //data是否为百分数
                        boolean isPercent = false;
                        //cellValue为null时，设置单元格格式为空白
                        if (cellValue == null) {
                            cell.setCellType(Cell.CELL_TYPE_BLANK);
                            cell.setCellValue(cellValue);
                            continue;
                        }
                        if (cellType == 0 && !"".equals(cellValue)) {
                            //判断data是否为数值型(有可能数值太大，为科学计数法类型）
                            isNum = cellValue.matches("^(-?\\d+)(\\.\\d+E?\\d*)?$");
                            //判断data是否为整数（小数部分是否为0）
                            isInteger = cellValue.matches("^[-\\+]?[\\d]*$");
                            //判断data是否为百分数（是否包含“%”）
                            isPercent = cellValue.contains("%");
                        }
                        short format = dataFormat.getFormat("@");
                        //如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
                        CellStyle contextstyle = wb.createCellStyle();
                        cell.setCellType(cellType);
                        if (isNum && !isPercent) {
                            // 此处设置数据格式
                            if (isInteger) {
                                //数据格式只显示整数
                                format = dataFormat.getFormat("0");
                            } else {
                                //保留两位小数点
                                format = dataFormat.getFormat("0.00");
                            }
                            contextstyle.setDataFormat(format);
                            cell.setCellStyle(contextstyle);
                            cell.setCellValue(Double.parseDouble(cellValue));
                        } else {
                            contextstyle.setDataFormat(format);
                            cell.setCellStyle(contextstyle);
                            cell.setCellValue(cellValue);
                        }
                    }
                }
            }
        }
        try {
            // 生成Excel文件并下载.(通过response对象是否为空来判定是使用浏览器下载还是直接写入到output中)
            POIUtils.writeByLocalOrBrowser(mResponse, handler.exportFileName(sheetName), wb, out);
        } catch (Exception e) {
            log.error("生成Excel文件失败:" + e.getMessage(), e);
            return false;
        }
        log.info(String.format("Excel处理完成,共生成数据:%s行 (不包含表头),耗时：%s seconds.", (data != null ? data.size() : 0),
                (System.currentTimeMillis() - begin) / 1000F));
        return true;
    }

    /**
     * 读取Excel数据(使用SAX的方式进行解析,读取所有Sheet数据)
     *
     * @param excelFile excel文件
     * @param handler   数据处理回调
     */
    public void readExcel(File excelFile, ReadHandler handler) {
        // TODO 第一列不能出现空值！！！！！！！
        this.readExcel(excelFile, -1, handler);
    }


    /**
     * 读取Excel数据(使用SAX的方式进行解析,读取所有Sheet数据)
     *
     * @param is      excel文件流
     * @param handler 数据处理回调
     */
    public void readExcel(InputStream is, String fileName, ReadHandler handler) {
        this.readExcel(is, fileName, -1, handler);
    }


    /**
     * 读取Excel数据(使用SAX的方式进行解析,读取所有Sheet数据)
     *
     * @param is      excel文件流
     * @param handler 数据处理回调
     */
    public void readExcel(InputStream is, String fileName, int sheetIndex, ReadHandler handler) {
        // TODO 第一列不能出现空值！！！！！！！
        long begin = System.currentTimeMillis();
        XlsxReader reader = new XlsxReader(handler).setEmptyCellValue(mEmptyCellValue);
        try {
            if (sheetIndex >= 0) {
                // 读取指定sheet
                reader.process(is, fileName, sheetIndex);
            } else {
                // 读取所有sheet
                reader.process(is, fileName);
            }
        } catch (Exception e) {
            log.error("读取excel文件时发生异常：" + e.getMessage(), e);
        }
        log.info(String.format("Excel读取并处理完成,耗时：%s seconds.", (System.currentTimeMillis() - begin) / 1000F));
    }

    /**
     * 读取Excel（使用SAX的方式进行解析,读取指定Sheet数据）
     *
     * @param excelFile  excel文件
     * @param sheetIndex sheet索引,-1为读取所有
     * @param handler    数据处理回调
     */
    public void readExcel(File excelFile, int sheetIndex, ReadHandler handler) {
        long begin = System.currentTimeMillis();
        String fileName = excelFile.getAbsolutePath();
        XlsxReader reader = new XlsxReader(handler).setEmptyCellValue(mEmptyCellValue);
        try {
            if (sheetIndex >= 0) {
                // 读取指定sheet
                reader.process(fileName, sheetIndex);
            } else {
                // 读取所有sheet
                reader.process(fileName);
            }
        } catch (Exception e) {
            log.error("读取excel文件时发生异常：" + e.getMessage(), e);
        }
        log.info(String.format("Excel读取并处理完成,耗时：%s seconds.", (System.currentTimeMillis() - begin) / 1000F));
    }

    /**
     * 读取Excel（不捕获异常）
     *
     * @param excelFile  excel文件
     * @param sheetIndex sheet索引,-1为读取所有
     * @param handler    数据处理回调
     */
    public void readExcel2(File excelFile, int sheetIndex, ReadHandler handler) throws Exception {
        long begin = System.currentTimeMillis();
        String fileName = excelFile.getAbsolutePath();
        XlsxReader reader = new XlsxReader(handler).setEmptyCellValue(mEmptyCellValue);
        if (sheetIndex >= 0) {
            // 读取指定sheet
            reader.process(fileName, sheetIndex);
        } else {
            // 读取所有sheet
            reader.process(fileName);
        }
        log.info(String.format("Excel读取并处理完成,耗时：%s seconds.", (System.currentTimeMillis() - begin) / 1000F));
    }

    private String convertCellValue(String oldValue, String format) {
        try {
            String protocol = format.split(":")[0];

            // 键值对字符串解析：s:1=男,2=女
            if ("s".equalsIgnoreCase(protocol)) {
                int oldIntValue = Integer.parseInt(oldValue);

                String[] pattern = format.split(":")[1].split(",");
                for (String p : pattern) {
                    String[] cp = p.split("=");

                    if (Integer.parseInt(cp[0]) == oldIntValue) {
                        return cp[1];
                    }
                }
            }
            // 使用处理类进行处理：c:com.wuwenze.poi.test.GradeCellFormat
            if ("c".equalsIgnoreCase(protocol)) {
                String clazz = format.split(":")[1];
                ExportConvert export = mConvertInstanceCache.get(clazz);

                if (export == null) {
                    export = (ExportConvert) Class.forName(clazz).newInstance();
                    mConvertInstanceCache.put(clazz, export);
                }

                if (mConvertInstanceCache.size() > 10)
                    mConvertInstanceCache.clear();
                return export.handler(oldValue);
            }
        } catch (Exception e) {
            log.error("出现问题,可能是@ExportConfig.format()的值不规范导致。", e);
        }
        return oldValue;
    }

    // 填充下拉数据验证(maxcess)
    private String[] rangeCellValues(String format) {
        try {
            String protocol = format.split(":")[0];
            if ("c".equalsIgnoreCase(protocol)) {
                String clazz = format.split(":")[1];
                ExportRange range = mRangeInstanceCache.get(clazz);

                if (range == null) {
                    range = (ExportRange) Class.forName(clazz).newInstance();
                    mRangeInstanceCache.put(clazz, range);
                }

                if (mRangeInstanceCache.size() > 10)
                    mRangeInstanceCache.clear();

                if (range != null) {
                    return range.handler();
                }
            }
        } catch (Exception e) {
            log.error("出现问题,可能是@ExportConfig.range()的值不规范导致。", e);
        }
        return new String[]{};
    }

    private void required$BuilderParams() {
        if (mClass == null) {
            throw new IllegalArgumentException("请先使用com.wuwenze.poi.ExcelKit.$Builder(Class<?>)构造器初始化参数。");
        }
    }

    private void required$ExportParams() {
        if (mClass == null || mResponse == null) {
            throw new IllegalArgumentException(
                    "请先使用com.wuwenze.poi.ExcelKit.$Export(Class<?>, HttpServletResponse)构造器初始化参数。");
        }

    }


    /**
     * 判断一个单元格的值是否是空值
     *
     * @param cellValue 单元格值
     * @return TRUE / FALSE
     */
    public static boolean isNullCellValue(String cellValue) {
        return null == cellValue || "".equals(cellValue) || "null".equalsIgnoreCase(cellValue);
    }

    /**
     * 判断整行数据是否都为空
     *
     * @param rowData 行数据
     * @return TRUE / FALSE
     */
    public static boolean isNullRowValue(List<String> rowData) {
        if (rowData == null || rowData.size() == 0) {
            return true;
        }

        int nullCellValueCount = 0;
        for (String cellValue : rowData) {
            if (isNullCellValue(cellValue)) {
                nullCellValueCount++;
            }
        }
        return nullCellValueCount == rowData.size();
    }

}
