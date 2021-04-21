package com.nacos.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 查询条件工具类
 *
 * @author guos
 * @date 2021/4/21 11:24
 **/
public class WrapperUtil {


    /**
     * 根据实体类查询
     *
     * @param t
     * @return
     * @author guos
     * @date 2021/4/21 11:33
     **/
    public static <T> QueryWrapper<T> getQueryWrapper(T t) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        List<Field> fields = listField(t);
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                if (!StringUtils.isEmpty(f.get(t))) {
                    wrapper.eq(f.getName(), f.get(t));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("获取属性【" + f.getName() + "】值失败!");
            }
        }
        return wrapper;
    }


    private static List<Field> listField(Object obj) {
        java.lang.reflect.Field[] declaredFields = obj.getClass().getDeclaredFields();
        List<Field> fields = Arrays.asList(declaredFields);
        fields = fields.stream().filter(f -> Objects.equals(f.getModifiers(), Modifier.PRIVATE) && !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList());
        return fields;
    }
}
