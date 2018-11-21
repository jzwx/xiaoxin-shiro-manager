package com.xiaoxin.manager.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author:jzwx
 * @Desicription: ChannelController
 * @Date:Created in 2018-11-21 11:21
 * @Modified By:
 */
@Controller
@RequestMapping("/channel")
public class ChannelController {
    /**
     * 日志打印器
     */
    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @RequestMapping("/channelListPage")
    public ModelAndView initChannelListPage() {
        return new ModelAndView("/channel/channelListPage");
    }

    @RequestMapping("/channelUserListPage")
    public ModelAndView initChannelUserListPage() {
        return new ModelAndView("/channel/channelUserListPage");
    }
}
