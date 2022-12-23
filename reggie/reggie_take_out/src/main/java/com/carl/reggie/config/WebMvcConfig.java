package com.carl.reggie.config;

import com.carl.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @program: reggie
 * @description: SpringMVC配置类
 * @author: Mr.Carl
 * @create: 2022-12-19 12:36
 **/
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /***
     * create time: 2022/12/19 12:37
     * @Description: 设置自定义静态资源映射
     * @param: [org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry]
     * @return: void
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始静态资源映射");
        //资源处理器
        registry.addResourceHandler("/backend/**")
                .addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**")
                .addResourceLocations("classpath:/front/");
    }

    /***
     * create time: 2022/12/19 21:24
     * @Description: 扩展MVC消息转换器
     * @param:
     * @return:
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        //设置对象转换器,底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }
}
