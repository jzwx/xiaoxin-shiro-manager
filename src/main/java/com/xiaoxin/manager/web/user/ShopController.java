package com.xiaoxin.manager.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author:jzwx
 * @Desicription: ShopController
 * @Date:Created in 2018-11-21 11:11
 * @Modified By:
 */
@Controller
@RequestMapping("/shop")
public class ShopController {
    /**
     * 日志打印器
     */
    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @RequestMapping("/shopPage")
    public ModelAndView initShopPage(){
        return new ModelAndView("/shop/shopPage");
    }
}
