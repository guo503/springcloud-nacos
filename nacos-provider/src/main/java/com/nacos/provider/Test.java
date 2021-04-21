package com.nacos.provider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author guos
 * @date 2021/3/1 12:20
 **/
public class Test {


    public static void main(String[] args) {
        String unit = "13903|15417531|13908|8";
        String bucketId = "13903|15417531|13907|3|1";
        //System.out.println(unit + "|" + bucketId.substring(bucketId.lastIndexOf('|') + 1));
        //System.out.println(bucketId.substring(bucketId.lastIndexOf('|') + 1));

        JSONObject a=new JSONObject();
        a.put("k",1);

        JSONObject b=new JSONObject();
        b.put("k",0);

        JSONArray array=new JSONArray();
        array.add(a);
        array.add(b);
        List<Object> list = array.stream().sorted(Comparator.comparing(o -> JSONObject.parseObject(o.toString()).getInteger("k"))).collect(Collectors.toList());
        list.forEach(System.out::println);

        //System.out.println("2020-12".compareTo("2021-1"));
    }
}
