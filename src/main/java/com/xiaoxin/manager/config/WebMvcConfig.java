package com.xiaoxin.manager.config;

import com.xiaoxin.manager.interceptor.UserActionInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

/**
 * @Author:jzwx
 * @Desicription: WebMvcConfig
 * @Date:Created in 2018-11-14 16:44
 * @Modified By:
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    @Value("${spring.thymeleaf.cache}")
    private boolean thymeleafCacheEnable = true;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 静态资源加载配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     *
     * @描述：在Spring添加拦截器之前先创建拦截器对象，这样就能在Spring映射这个拦截器前，把拦截器中的依赖注入的对象给初始化完成了。
     * </br>避免拦截器中注入的对象为null问题。
     * @创建人：wyait
     * @创建时间：2018年5月3日 上午10:07:36
     * @return
     */
    @Bean
    public UserActionInterceptor userActionInterceptor(){
        return new UserActionInterceptor();
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 路径根据后期项目的扩展，进行调整
        registry.addInterceptor(userActionInterceptor())
                .addPathPatterns("/user/**", "/auth/**","/channel/**","/order/**","/shop/**")
                .excludePathPatterns("/user/login");
        super.addInterceptors(registry);
    }

    /**
     * 模板资源解析器
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.thymeleaf")
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(thymeleafCacheEnable);
        return templateResolver;
    }

    /**
     * Thymeleaf标准方言解释器
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    /**
     * 视图解析器
     */
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }

    /**
     * Bean Util
     * @return
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
