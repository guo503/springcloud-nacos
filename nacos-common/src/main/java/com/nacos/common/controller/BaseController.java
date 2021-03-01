package com.nacos.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nacos.common.response.Result;
import com.nacos.common.utils.TableParserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author guos
 * @date 2020/7/4 9:13
 **/
@CrossOrigin
public class BaseController<S extends IService<T>, T, R> {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected S baseService;


    /**
     * 根据id获取详情
     *
     * @param id
     * @return
     * @author guos
     * @date 2020/7/28 15:09
     **/
    @GetMapping("/{id}")
    public Result<R> get(@PathVariable("id") Integer id) {
        if (Objects.isNull(id)) {
            return Result.success();
        }
        T t = baseService.getById(id);
        R r = TableParserUtil.getInstance(this.getClass(), 3);
        if (Objects.isNull(r)) {
            return Result.success();
        }
        BeanUtils.copyProperties(t, r);
        return Result.success(r);
    }


    /**
     * 新增
     *
     * @param r
     * @return
     * @author guos
     * @date 2020/7/28 15:13
     **/
    @PostMapping
    public Result<Object> save(@RequestBody R r) {
        T t = TableParserUtil.getInstance(this.getClass(), 1);
        if (Objects.isNull(t)) {
            throw new RuntimeException("获取实体类失败!");
        }
        BeanUtils.copyProperties(r, t);
        return baseService.save(t) ? Result.success("添加成功") : Result.fail("添加失败");
    }


    /**
     * 更新
     *
     * @param r
     * @return
     * @author guos
     * @date 2020/7/28 15:15
     **/
    @PutMapping
    public Result<Object> update(@RequestBody R r) {
        T t = TableParserUtil.getInstance(this.getClass(), 1);
        if (Objects.isNull(t)) {
            throw new RuntimeException("获取实体类失败!");
        }
        BeanUtils.copyProperties(r, t);
        return baseService.updateById(t) ? Result.success("更新成功") : Result.fail("更新失败");
    }


    /**
     * 根据条件查询列表
     *
     * @param r r
     * @return
     * @author guos
     * @date 2020/7/28 15:17
     **/
    @GetMapping("/list")
    public Result<IPage<T>> list(R r) throws IllegalAccessException {
        T t = TableParserUtil.getInstance(this.getClass(), 1);
        if (Objects.isNull(t)) {
            throw new RuntimeException("获取实体类失败!");
        }
        QueryWrapper<T> wrapper = new QueryWrapper<T>();
        List<Field> fields = this.listField(t);
        for (Field f : fields) {
            f.setAccessible(true);
            if (!StringUtils.isEmpty(f.get(t))) {
                wrapper.eq(f.getName(), f.get(t));
            }
        }
        Page<T> page = new Page<>(this.getPageNum(), this.getPageSize());
        IPage<T> pageList = baseService.page(page, wrapper);
        return Result.success(pageList);
    }


    protected List<Field> listField(Object obj) {
        java.lang.reflect.Field[] declaredFields = obj.getClass().getDeclaredFields();
        List<Field> fields = Arrays.asList(declaredFields);
        fields = fields.stream().filter(f -> Objects.equals(f.getModifiers(), Modifier.PRIVATE) && !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList());
        for (java.lang.reflect.Field field : fields) {
            System.out.println(field.getName());
        }
        return fields;
    }


    protected int getPageNum() {
        return NumberUtils.toInt(this.request.getParameter("pageNum"), 1);
    }

    protected int getPageSize() {
        return NumberUtils.toInt(this.request.getParameter("pageSize"), 10);
    }


}
