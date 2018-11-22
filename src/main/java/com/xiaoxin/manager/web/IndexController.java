package com.xiaoxin.manager.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:jzwx
 * @Desicription: IndexController
 * @Date:Created in 2018-11-14 9:03
 * @Modified By:
 */
@Controller
@RequestMapping("/")
public class IndexController {
    /**
     * 日志打印器
     */
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/index")
    public String index() {
        logger.debug("---------------index----------------------");
        return "index";
    }

    @RequestMapping("/home")
    public String toHome() {
        logger.debug("---------------home------------------------");
        return "home";
    }

    @RequestMapping("/login")
    public String login() {
        logger.debug("---------------login--------------------------");
        return "login";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        logger.debug("---------------toLogin--------------------------");
        return "toLogin";
    }

    @RequestMapping("/403")
    public String toError() {
        return "/error/403";
    }

}
