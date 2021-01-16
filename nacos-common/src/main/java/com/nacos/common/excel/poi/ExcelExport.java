package com.nacos.common.excel.poi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * excel导出工具
 *
 * @author guos
 * @date 2021/1/16 15:35
 **/
@Slf4j
public class ExcelExport {

    public static <T> void toExcel(HttpServletResponse response, List<T> list, String fileName) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        ExcelKit.$Export(list.get(0).getClass(), response).setMaxSheetRecords(list.size()).toExcel(list, fileName);
    }


}
