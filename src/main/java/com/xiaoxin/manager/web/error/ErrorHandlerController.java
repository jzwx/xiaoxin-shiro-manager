package com.xiaoxin.manager.web.error;

import com.xiaoxin.manager.common.utils.LoggerUtil;
import com.xiaoxin.manager.utils.ExceptionEnum;
import com.xiaoxin.manager.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author:jzwx
 * @Desicription: ErrorHandlerController
 * @Date:Created in 2018-11-19 9:37
 * @Modified By:
 */
@Controller
@RequestMapping("error")
public class ErrorHandlerController extends AbstractErrorController {

    private ErrorProperties     errorProperties;

    private static final Logger logger     = LoggerFactory.getLogger(ErrorHandlerController.class);

    private static final String ERROR_PATH = "error";

    /**
     * 初始化errorAttributes
     * @param errorAttributes
     */
    public ErrorHandlerController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        LoggerUtil.debug(logger, "", "统一异常处理【{0}.errorHtml】text/html=普通请求：request={1}",
            getClass().getName(), request);
        ModelAndView mv = new ModelAndView(ERROR_PATH);
        Map<String, Object> model = getErrorAttributes(request,
            isIncludeStackTrace(request, MediaType.TEXT_HTML));
        LoggerUtil.info(logger, "统一异常处理【{0}.errorHtml】统一异常处理：model={1}", getClass().getName(),
            model);
        // 1 获取错误状态码（也可以根据异常对象返回对应的错误信息）
        HttpStatus httpStatus = getStatus(request);
        LoggerUtil.debug(logger, "统一异常处理【{0}.errorHtml】统一异常处理!错误状态码httpStatus：{1}",
            getClass().getName(), httpStatus);
        // 2 返回错误提示
        ExceptionEnum ee = getMessage(httpStatus);
        Result<String> result = new Result<String>(String.valueOf(ee.getType()), ee.getCode(),
            ee.getMsg());
        // 3 将错误信息放入mv中
        mv.addObject("result", result);
        LoggerUtil.info(logger, "统一异常处理【{0}.errorHtml】统一异常处理!错误信息mv：{1}", getClass().getName(), mv);
        return mv;
    }

    @RequestMapping
    //设置响应状态码为：200，结合前端约定的规范处理。也可不设置状态码，前端ajax调用使用error函数进行控制处理
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody Result<String> error(HttpServletRequest request, Exception e){
        LoggerUtil.info(logger,"统一异常处理【{0}.error】text/html=普通请求：request={1}",getClass().getName(),request);
        /** model对象包含了异常信息 */
        Map<String,Object> model = getErrorAttributes(request,isIncludeStackTrace(request,MediaType.TEXT_HTML));
        LoggerUtil.info(logger,"统一异常处理【{0} error】统一异常处理：model={1}",getClass().getName(),model);
        // 1 获取错误状态码（也可以根据异常对象返回对应的错误信息）
        HttpStatus httpStatus = getStatus(request);
        LoggerUtil.info(logger,"统一异常处理【{0}.error】统一异常处理!错误状态码httpStatus：{1}",getClass().getName(),httpStatus);
        // 2 返回错误提示
        ExceptionEnum ee = getMessage(httpStatus);
        Result<String> result = new Result<String>(
                String.valueOf(ee.getType()), ee.getCode(), ee.getMsg());
        // 3 将错误信息返回
        LoggerUtil.info(logger,"统一异常处理【{0}.error】统一异常处理!错误信息result：{1}",getClass().getName(),result);

        return result;
    }


    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    protected ErrorProperties getErrorProperties() {
        //此处也可以通过注入ServerProperties获取ErrorProperties
        return new ErrorProperties();
    }

    /**
     * 根据error状态码，返回不同的错误提示信息
     * @param httpStatus
     * @return
     */
    private ExceptionEnum getMessage(HttpStatus httpStatus) {
        if (httpStatus.is4xxClientError()) {
            // 4开头的错误状态码
            if ("400".equals(HttpStatus.BAD_REQUEST)) {
                return ExceptionEnum.BAD_REQUEST;
            } else if ("403".equals(HttpStatus.FORBIDDEN)) {
                return ExceptionEnum.BAD_REQUEST;
            } else if ("404".equals(HttpStatus.NOT_FOUND)) {
                return ExceptionEnum.NOT_FOUND;
            }
        } else if (httpStatus.is5xxServerError()) {
            // 5开头的错误状态码
            if ("500".equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                return ExceptionEnum.SERVER_EPT;
            }
        }
        // 统一返回：未知错误
        return ExceptionEnum.UNKNOW_ERROR;
    }
}
