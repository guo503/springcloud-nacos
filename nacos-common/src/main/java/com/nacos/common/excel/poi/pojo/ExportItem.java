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
package com.nacos.common.excel.poi.pojo;

public class ExportItem {

    private String field; // 属性名
    private String display; // 显示名
    private short width; // 宽度
    private String convert;
    //	private short color;
    private String replace;
    private String range;//数据有效性 下拉框
    private short cellType; //单元格数据类型
    /**
     * DataFormat 格式化输出样式  取值如下：
     * "@"表示格式化为文本，默认格式化为文本
     * "0"表示格式化为数字，
     * "0.00"表示格式化为两位小数
     *
     * @return
     */
    private String format;

    public ExportItem setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public String getField() {
        return field;
    }

    public ExportItem setField(String field) {
        this.field = field;
        return this;
    }

    public String getDisplay() {
        return display;
    }

    public ExportItem setDisplay(String display) {
        this.display = display;
        return this;
    }

    public short getWidth() {
        return width;
    }

    public ExportItem setWidth(short width) {
        this.width = width;
        return this;
    }

    public String getConvert() {
        return convert;
    }

    public ExportItem setConvert(String convert) {
        this.convert = convert;
        return this;
    }

//	public short getColor() {
//		return color;
//	}
//
//	public ExportItem setColor(short color) {
//		this.color = color;
//		return this;
//	}

    public String getReplace() {
        return replace;
    }

    public ExportItem setReplace(String replace) {
        this.replace = replace;
        return this;
    }

    public String getRange() {
        return range;
    }

    public ExportItem setRange(String range) {
        this.range = range;
        return this;
    }

    public short getCellType() {
        return cellType;
    }

    public ExportItem setCellType(short cellType) {
        this.cellType = cellType;
        return this;
    }
}
