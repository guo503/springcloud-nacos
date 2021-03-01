package com.nacos.provider;

import com.nacos.provider.entity.User;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author guos
 * @date 2021/3/1 12:20
 **/
public class Test {


    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass("com.nacos.provider.entity.User");
        System.out.println(aClass);

    }

    public static List<Field> listField() {
        Field[] declaredFields = User.class.getDeclaredFields();
        List<Field> fields = Arrays.asList(declaredFields);
        fields = fields.stream().
                filter(f -> Objects.equals(f.getModifiers(), Modifier.PRIVATE) && !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList());
        return fields;
    }
}
