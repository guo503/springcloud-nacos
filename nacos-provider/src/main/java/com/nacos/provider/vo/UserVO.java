package com.nacos.provider.vo;

import com.nacos.provider.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
* 用户显示类
* @author guos
* @date 2020/12/12 16:28
*/
@EqualsAndHashCode(callSuper = false)
@Data
public class UserVO extends User implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 444195981951714L;
}
