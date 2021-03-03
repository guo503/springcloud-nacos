package com.nacos.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 实体类
* @author guos
* @date 2021/03/03 12:07
*/
@TableName("student")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student implements Serializable {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 995149823294518L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer age;
}