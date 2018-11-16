package com.xiaoxin.manager.common.utils;

import java.text.MessageFormat;

import org.slf4j.Logger;

/**
 * 日志工具类
 *
 * @Author:jzwx
 * @Desicription: LoggerUtil
 * @Date:Created in 2018-10-08 15:46
 * @Modified By:
 */
public class LoggerUtil {
    /**
     * 私有构造函数，防止实例化
     */
    private LoggerUtil() {

    }

    /**
     * 生成调试级别日志
     * <p>
     * 根据带参数的日志模板和参数集合，生成debug级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示参数集合中index为0的参数替换{0}
     * <p/>
     *
     * @param logger
     * @param template
     * @param parameters
     */
    public static void debug(Logger logger, String template, Object... parameters) {
        if (logger.isDebugEnabled()) {
            logger.debug(addThreadId(MessageFormat.format(template, parameters)));
        }
    }

    /**
     * 生成信息级别日志
     * <p>
     * 根据带参数的日志模板和参数集合，生成info级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示参数集合中index为0的参数替换{0}
     * <p/>
     *
     * @param logger
     * @param template
     * @param parameters
     */
    public static void info(Logger logger, String template, Object... parameters) {
        if (logger.isInfoEnabled()) {
            logger.info(addThreadId(MessageFormat.format(template, parameters)));
        }
    }

    /**
     * 生成警告级别日志，带异常堆栈
     * <p>
     * 根据带参数的日志模板和参数集合，生成error级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示参数集合中index为0的参数替换{0}
     * <p/>
     *
     * @param logger
     * @param template
     * @param parameters
     */
    public static void warn(Throwable e, Logger logger, String template, Object... parameters) {
        if (logger.isWarnEnabled()) {
            logger.warn(addThreadId(MessageFormat.format(template, parameters)), e);
        }
    }

    /**
     * 生成警告级别日志
     * <p>
     * 根据带参数的日志模板和参数集合，生成warn级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示参数集合中index为0的参数替换{0}
     * <p/>
     *
     * @param logger
     * @param template
     * @param parameters
     */
    public static void warn(Logger logger, String template, Object... parameters) {
        if (logger.isWarnEnabled()) {
            logger.warn(addThreadId(MessageFormat.format(template, parameters)));
        }
    }

    /**
     * 生成错误级别日志，带异常堆栈
     * <p>
     * 根据带参数的日志模板和参数集合，生成error级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示参数集合中index为0的参数替换{0}
     * <p/>
     *
     * @param logger
     * @param template
     * @param parameters
     */
    public static void error(Throwable e, Logger logger, String template, Object... parameters) {
        if (logger.isErrorEnabled()) {
            logger.error(addThreadId(MessageFormat.format(template, parameters)), e);
        }
    }

    /**
     * 生成错误级别日志
     * <p>
     * 根据带参数的日志模板和参数集合，生成error级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示参数集合中index为0的参数替换{0}
     * <p/>
     *
     * @param logger
     * @param template
     * @param parameters
     */
    public static void error(Logger logger, String template, Object... parameters) {
        if (logger.isErrorEnabled()) {
            logger.error(addThreadId(MessageFormat.format(template, parameters)));
        }
    }

    /**
     * 日志中增加当前线程ID
     *
     * @param msg
     * @return
     */
    private static String addThreadId(String msg) {

        final StringBuilder sb = new StringBuilder();
        sb.append("[").append(Thread.currentThread().getId()).append("]---").append(msg);
        return sb.toString();

    }

    /**
     * 日志中增加线程上下文中的用户信息
     *
     * @param msg
     * @return
     */
    private static String addUserInfo(String msg) {
        return msg;
    }
}
