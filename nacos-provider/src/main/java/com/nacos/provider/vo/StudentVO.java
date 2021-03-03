package com.nacos.provider.vo;

import com.nacos.provider.entity.Student;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 显示类
* @author guos
* @date 2021/03/03 12:07
*/
@EqualsAndHashCode(callSuper = false)
@Data
public class StudentVO extends Student implements Serializable {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 277797997291684L;
}