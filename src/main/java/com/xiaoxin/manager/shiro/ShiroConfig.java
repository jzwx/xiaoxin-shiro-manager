package com.xiaoxin.manager.shiro;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author:jzwx
 * @Desicription: ShiroConfig
 * @Date:Created in 2018-11-14 17:08
 * @Modified By:
 */
@Configuration
public class ShiroConfig {

    /**
     * 日志打印器
     */
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    /**
     *  ShiroFilterFactoryBean 处理拦截资源文件过滤器
     *  </br>1,配置shiro安全管理器接口securityManage;
     *  </br>2,shiro 连接约束配置filterChainDefinitions;
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        // ShiroFilterFactoryBean对象
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        logger.debug("----------------------Shiro拦截器工厂类注入开始");
        //配置shiro安全管理器 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 指定要求登录时的链接
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/home");
        // 未授权时跳转的界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");

        // filterChainDefinitions拦截器=map必须用：LinkedHashMap，因为它必须保证有序
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置退出过滤器,具体的退出代码Shiro已经实现
        filterChainDefinitionMap.put("/logout", "logout");
        // 配置记住我或认证通过可以访问的地址
        filterChainDefinitionMap.put("/", "user");

        // 配置不会被拦截的链接 从上向下顺序判断 (不需要认证 能匿名访问的路径)
        filterChainDefinitionMap.put("/login", "anon"); //用户登录页
        filterChainDefinitionMap.put("/css/*", "anon"); //css样式
        filterChainDefinitionMap.put("/js/*", "anon"); //js文件
        filterChainDefinitionMap.put("/js/*/*", "anon"); //js文件
        filterChainDefinitionMap.put("/js/*/*/*", "anon"); //js文件
        filterChainDefinitionMap.put("/images/*/**", "anon"); //image文件
        filterChainDefinitionMap.put("/layui/*", "anon"); //layui文件
        filterChainDefinitionMap.put("/layui/*/**", "anon"); //layui文件
        filterChainDefinitionMap.put("/treegrid/*", "anon"); //treegrid文件
        filterChainDefinitionMap.put("/treegrid/*/*", "anon"); //treegrid文件
        filterChainDefinitionMap.put("/fragments/*", "anon"); //fragments文件
        filterChainDefinitionMap.put("/layout", "anon"); //layout文件

        filterChainDefinitionMap.put("/user/sendMsg", "anon"); //用户短信发送接口
        filterChainDefinitionMap.put("/user/login", "anon"); //用户密码登录接口
        filterChainDefinitionMap.put("/home", "anon"); //主页

//        filterChainDefinitionMap.put("/user/delUser", "authc,perms[usermanage]");

        // authc:所有url都必须认证通过才可以访问; anon:所有url都可以匿名访问【放行】
        filterChainDefinitionMap.put("/*", "authc");
        filterChainDefinitionMap.put("/*/*", "authc");
        filterChainDefinitionMap.put("/*/*/*", "authc");
        filterChainDefinitionMap.put("/*/*/*/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        logger.debug("-------------------------Shiro拦截器工厂类注入成功");
        return shiroFilterFactoryBean;
    }

    /**
     * shiro安全管理器设置realm认证和ehcache缓存管理
     *
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setRealm(shiroRealm());
        // 注入ehcache缓存管理器
        securityManager.setCacheManager(ehCacheManager());
        // 注入Cookie记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * 身份认证realm;(账号密码校验；权限等)
     *
     * @return ShiroRealm
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        //使用自定义的CredentialsMatcher进行密码校验和输错次数限制
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码,更改密码生成规则和校验的逻辑一致即可; ）
     *
     * @return HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(
            ehCacheManager());
        // new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5"); // 散列算法:这里使用MD5算法
        hashedCredentialsMatcher.setHashIterations(1); // 散列的次数,比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    /**
     * ehcache缓存管理器；shiro整合ehcache：
     * 通过安全管理器：securityManager
     * 单例的cache防止热部署重启失败
     * @return EhCacheManager
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        logger.debug("======shiro整合ehcache缓存:ShiroConfiguration.getEhCacheManager()");
        EhCacheManager ehCacheManager = new EhCacheManager();
        CacheManager cacheManager = CacheManager.getCacheManager("es");
        if (cacheManager == null) {
            try {
                cacheManager = CacheManager
                    .create(ResourceUtils.getInputStreamForPath("classpath:config/ehcache.xml"));
            } catch (CacheException | IOException e) {
                e.printStackTrace();
            }
        }
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }

    /**
     * 配置cookie记住我管理器
     * @return CookieRememberMeManager
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        logger.debug("匹配cookie记住我管理器");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(remeberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * 设置记住我cookie过期时间
     * @return SimpleCookie
     */
    @Bean
    public SimpleCookie remeberMeCookie() {
        logger.debug("记住我，设置cookie过期时间！");
        // cookie名称;对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // 记住我cookie生效时间30天,单位秒【1小时】
        simpleCookie.setMaxAge(3600);
        return simpleCookie;
    }
}
