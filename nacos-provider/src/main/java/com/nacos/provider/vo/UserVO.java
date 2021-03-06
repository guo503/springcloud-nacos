package com.nacos.provider.vo;

import com.nacos.provider.entity.User;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 显示类
* @author guos
* @date 2021/03/03 12:01
*/
@EqualsAndHashCode(callSuper = false)
@Data
public class UserVO extends User implements Serializable {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 773445786572826L;
}