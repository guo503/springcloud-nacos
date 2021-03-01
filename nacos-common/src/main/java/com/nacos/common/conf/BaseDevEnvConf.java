package com.nacos.common.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 开发服环境
 *
 * @author guos
 * @date 2020/12/30 10:40
 **/
@Slf4j
public abstract class BaseDevEnvConf implements EnvironmentPostProcessor {

    private static final String PROPERTY_NAME = "dev";

    private final YamlPropertySourceLoader propertySourceLoader = new YamlPropertySourceLoader();

    /**
     * 子类返回模块本地开发配置文件绝对路径
     *
     * @return
     */
    abstract public String getModulePath();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (Files.exists(Paths.get(this.getModulePath()))) {
            this.load(environment, this.getModulePath());
        }
        load(environment, getModulePath());
    }

    private void load(ConfigurableEnvironment environment, String confFilePath) {
        Resource resource = new FileSystemResource(confFilePath);
        load(environment, resource, null);
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length > 0) {
            for (String profile : profiles) {
                load(environment, resource, profile);
            }
        }
    }

    private void load(ConfigurableEnvironment environment, Resource resource, String profile) {
        try {
            List<PropertySource<?>> propertySourceList = propertySourceLoader.load(PROPERTY_NAME, resource);
            if (!CollectionUtils.isEmpty(propertySourceList)) {
                environment.getPropertySources().addFirst(propertySourceList.get(0));
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
