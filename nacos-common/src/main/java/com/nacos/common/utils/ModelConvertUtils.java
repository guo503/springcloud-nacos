package com.nacos.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 对象转换工具类
 * author: guos
 * date: 2019/6/12 10:33
 **/
public class ModelConvertUtils {

    private static <T> T convert(Class<T> type, Object o) {
        try {
            T t = type.newInstance();
            BeanUtils.copyProperties(o, t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("对象convert出错!");
        }

    }

    /**
     * 对象列表转换工具类
     *
     * @param target
     * @param list
     * date 2019/6/12 10:33
     * return
     **/
    public static <T, V> List<T> convertList(Class<T> target, List<V> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException("list is empty");
        }
        List<T> targetList = new ArrayList<T>();
        list.forEach(e -> {
            targetList.add(convert(target, e));
        });
        return targetList;
    }
}
