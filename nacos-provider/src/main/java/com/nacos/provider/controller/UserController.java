package com.nacos.provider.controller;

import com.nacos.common.excel.poi.ExcelExport;
import com.nacos.common.utils.ModelConvertUtils;
import com.nacos.provider.business.UserBusiness;
import com.nacos.provider.model.User;
import com.nacos.provider.query.UserQuery;
import com.nacos.provider.vo.UserVO;
import mybatis.base.template.bs.controller.BaseController;
import mybatis.base.template.response.Result;
import mybatis.core.entity.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户api类
 *
 * @author guos
 * @date 2020/12/12 16:28
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserBusiness, User, UserQuery, UserVO> {

    @Value("${rsa.publicKey}")
    private String publicKey;

    @Autowired
    protected HttpServletResponse response;


    @GetMapping("/show")
    public Result<Object> show() {
        return Result.success(publicKey);
    }


    /**
     * 导出
     *
     * @param
     * @return
     * @author guos
     * @date 2021/1/16 17:42
     **/
    @GetMapping("/export")
    public void export() {
        Condition<User> condition = new Condition<>();
        List<UserVO> userVOList = baseBusiness.listByCondition(condition);
        ExcelExport.toExcel(response, ModelConvertUtils.convertList(UserQuery.class,userVOList), "用户信息报表");
    }
}
