package com.nacos.provider.vo;

import com.nacos.provider.model.User;
import java.io.Serializable;
import lombok.Data;

/**
* 用户显示类
* @author guos
* @date 2020/12/11 20:00
*/
@Data
public class UserVO extends User implements Serializable {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 696148232717672L;
}