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
package com.nacos.common.excel.poi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel导出项配置
 *
 * @author wuwz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExportConfig {

    /**
     * @return 表头显示名(如 ： id字段显示为 " 编号 ") 默认为字段名
     */
    String value() default "field";

    /**
     * @return 单元格宽度 默认-1(自动计算列宽)
     */
    short width() default -1;

    /**
     * <pre>将单元格值进行转换后再导出：
     * 目前支持以下几种场景：
     * 1. 固定的数值转换为字符串值（如：1代表男，2代表女）
     * <b>表达式:</b> "s:1=男,2=女"
     *
     * 2. 数值对应的值需要查询数据库才能进行映射(实现ExportConvert接口)
     * <b>表达式:</b> "c:ExportConvert实现类"
     * </pre>
     *
     * @return 默认不启用
     */
    String convert() default "";


//	/**
//	 * @return 当前单元格的字体颜色 (默认 HSSFColor.BLACK.index)
//	 */
//	short color() default HSSFColor.BLACK.index;

    /**
     * DataFormat 格式化输出样式
     * 取值："@"表示格式化为文本，默认格式化为文本
     *      "0"表示格式化为数字，
     *      "0.00"表示格式化为两位小数
     *
     * @return
     */
    String format() default "@";

    /**
     * 将单元格的值替换为当前配置的值：
     * 应用场景:
     * 密码字段导出为："******"
     *
     * @return 默认true
     */
    String replace() default "";

    /**
     * 设置单元格数据验证（下拉框）
     * <b>表达式:</b> "c:ExportRange实现类"
     *
     * @return 默认不启用
     */
    String range() default "";

    /**
     * 设置单元格数据类型
     * 0.数字
     * 1.字符串
     *
     * @return 默认字符串
     */
    short cellType() default 1;
}
