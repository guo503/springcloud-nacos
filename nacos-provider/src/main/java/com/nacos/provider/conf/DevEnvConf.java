package com.nacos.provider.conf;

import com.nacos.common.conf.BaseDevEnvConf;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wenrui on 2017/12/4.
 */
@Slf4j
public class DevEnvConf extends BaseDevEnvConf {

    @Override
    public String getModulePath() {
        return "D:\\project\\test\\conf\\nacos-provider.yml";
    }
}
