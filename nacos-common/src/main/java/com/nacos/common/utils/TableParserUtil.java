package com.nacos.common.utils;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;


/**
 * 实体类解析成数据库表
 *
 * @author lgt
 * @date 2019/4/30 : 11:55 AM
 */
public class TableParserUtil {

    public static <T> T getInstance(Class<?> paramClz, int index) {
        try {
            Type type = paramClz.getGenericSuperclass();
            if (Objects.isNull(type)) {
                return null;
            }
            ParameterizedType parameterizedType = null;
            if (type instanceof ParameterizedType) {
                parameterizedType = (ParameterizedType) type;
            }
            if (parameterizedType == null) {
                return null;
            }
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types.length == 0) {
                return null;
            }
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (index >= actualTypeArguments.length) {
                return null;
            }
            Type realType = parameterizedType.getActualTypeArguments()[index];
            Class<T> clz = (Class<T>) realType;
            return clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
