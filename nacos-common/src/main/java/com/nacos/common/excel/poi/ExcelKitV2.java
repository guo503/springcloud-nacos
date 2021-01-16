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
import com.nacos.common.excel.poi.hanlder.ExportHandler;
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
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 在ExcelKit的基础上新增
 * 1.多sheet页面导出
 * 2.多excel文件zip导出
 *
 * @author jinguoling
 */
public class ExcelKitV2 extends ExcelKit {
    private static Logger log = Logger.getLogger(ExcelKitV2.class);

    private Class<?> mClass = null;
    // 默认以此值填充空单元格,可通过 setEmptyCellValue(string)改变其默认值。
    private String mEmptyCellValue = null;
    // 分Sheet机制：每个Sheet最多多少条数据
    private Integer mMaxSheetRecords = 10000;
    // 缓存数据格式器实例,避免多次使用反射进行实例化
    private static Map<String, ExportConvert> mConvertInstanceCache = new HashMap<String, ExportConvert>();
    private static Map<String, ExportRange> mRangeInstanceCache = new HashMap<String, ExportRange>();

    protected ExcelKitV2() {
    }


    protected ExcelKitV2(Class<?> clazz) {
        this.mClass = clazz;
    }

    /**
     * 用于浏览器导出
     *
     * @param clazz 实体Class对象
     * @return ExcelKit
     */
    public static ExcelKitV2 $Export(Class<?> clazz) {
        return new ExcelKitV2(clazz);
    }

    /**
     * 分Sheet机制：每个Sheet最多多少条数据(默认10000)
     *
     * @param size 数据条数
     * @return this
     */
    @Override
    public ExcelKitV2 setMaxSheetRecords(Integer size) {
        this.mMaxSheetRecords = size;
        return this;
    }

    /**
     * 导出多sheet页面excel文件集合的zip包
     * map : excelName sheetName Class data
     *
     * @param response
     * @param zipName
     * @param list
     */
    public static void toMultipleSheetsZip(HttpServletResponse response, String zipName, List<Map<String, Map<String, Map<Class, List<?>>>>> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        ZipOutputStream zipOutputStream = null;
        try {
            // 设置导出文件属性
            zipName = URLEncoder.encode(zipName + ".zip", "UTF-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + zipName);
            response.setContentType("multipart/form-data;charset=UTF-8");
            zipOutputStream = new ZipOutputStream(response.getOutputStream());
            for (int i = 0; i < list.size(); i++) {
                Map<String, Map<String, Map<Class, List<?>>>> excelsMap = list.get(i);
                for (Map.Entry<String, Map<String, Map<Class, List<?>>>> entry : excelsMap.entrySet()) {
                    SXSSFWorkbook workbook = POIUtils.newSXSSFWorkbook();
                    String excelName = entry.getKey();
                    Map<String, Map<Class, List<?>>> excelMap = entry.getValue();
                    workbook = getMultipleSheet(workbook, excelMap);
                    zipWrite(workbook, zipOutputStream, excelName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != zipOutputStream) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导出多sheet页面excel文件
     */
    public static void toMultipleSheet(HttpServletResponse response, String excelName, Map<String, Map<Class, List<?>>> map) {
        SXSSFWorkbook workbook = POIUtils.newSXSSFWorkbook();
        workbook = getMultipleSheet(workbook, map);
        if (null == workbook) {
            log.error("导出失败,没有数据!");
            return;
        }
        multipleSheetExport(workbook, excelName, response);
    }

    public static SXSSFWorkbook getMultipleSheet(SXSSFWorkbook workbook, Map<String, Map<Class, List<?>>> map) {
        if (CollectionUtils.isEmpty(map) && null == workbook) {
            return null;
        }
        for (Map.Entry<String, Map<Class, List<?>>> entry : map.entrySet()) {
            String sheetName = entry.getKey();
            Map<Class, List<?>> innerMap = entry.getValue();
            if (CollectionUtils.isEmpty(innerMap)) {
                continue;
            }
            for (Map.Entry<Class, List<?>> innerEntry : innerMap.entrySet()) {
                Class clazz = innerEntry.getKey();
                List<?> list = innerEntry.getValue();
                workbook = ExcelKitV2.$Export(clazz).setMaxSheetRecords(Math.min(list.size() == 0 ? 100 : list.size(), 1000000)).getExcel(list, sheetName, workbook);
            }
        }
        return workbook;
    }

    /**
     * 得到 Workbook
     *
     * @param data      数据集合
     * @param sheetName 工作表名字
     * @return true-操作成功,false-操作失败
     */
    public SXSSFWorkbook getExcel(List<?> data, String sheetName, SXSSFWorkbook wb) {
        return toExcel(data, sheetName, wb);
    }

    /**
     * 针对转换方法的默认实现(提供默认样式和文件命名规则)
     *
     * @param data      数据集合
     * @param sheetName 工作表名字
     * @return true-操作成功,false-操作失败
     */
    public SXSSFWorkbook toExcel(List<?> data, String sheetName, SXSSFWorkbook wb) {

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
        }, wb);
    }


    public SXSSFWorkbook toExcel(List<?> data, String sheetName, ExportHandler handler, SXSSFWorkbook wb) {
        required$BuilderParams();
        if (CollectionUtils.isEmpty(data)) {
            data = new ArrayList<>();
        }
        log.info(String.format("即将导出excel数据：%s条,请稍后..", data.size()));

        // 导出列查询。
        ExportConfig currentExportConfig = null;
        ExportItem currentExportItem = null;
        List<ExportItem> exportItems = new ArrayList<ExportItem>();
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
                    DataFormat dataFormat = wb.createDataFormat();
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
        return wb;
    }

    private String convertCellValue(String oldValue, String format) {
        try {
            String protocol = format.split(":")[0];

            // 键值对字符串解析：s:1=男,2=女
            if ("s".equalsIgnoreCase(protocol)) {
                Integer oldIntValue = Integer.parseInt(oldValue);

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

                if (export != null) {
                    return export.handler(oldValue);
                }
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

    /**
     * 导出excel文件
     *
     * @param workbook
     * @param filename
     * @param response
     */
    public static void multipleSheetExport(SXSSFWorkbook workbook, String filename, HttpServletResponse response) {
        BufferedOutputStream out = null;
        OutputStream outRes = null;
        try {
            filename = filename + ".xlsx";
            filename = URLEncoder.encode(filename, "UTF-8");
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentType("multipart/form-data;charset=UTF-8");
            out = new BufferedOutputStream(response.getOutputStream());
            outRes = response.getOutputStream();
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outRes != null) {
                try {
                    outRes.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 填充 ZipEntry
     *
     * @param workbook
     * @param zipOutputStream
     * @param excelName
     */
    public static void zipWrite(SXSSFWorkbook workbook, ZipOutputStream zipOutputStream, String excelName) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            workbook.write(os);
            byte[] bytes = os.toByteArray();
            ZipEntry entry = new ZipEntry(excelName + ".xlsx");
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != zipOutputStream) {
                try {
                    zipOutputStream.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
