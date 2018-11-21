package com.xiaoxin.manager.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author:jzwx
 * @Desicription: OrderController
 * @Date:Created in 2018-11-21 11:17
 * @Modified By:
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    /**
     * 日志打印器
     */
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping("/orderPage")
    public ModelAndView initOrderPage(){
        return new ModelAndView("/order/orderPage");
    }
}
