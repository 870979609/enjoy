package com.lyq.framework.common.handle;

import com.lyq.framework.common.exception.BusinessException;
import com.lyq.framework.common.response.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lyq.framework.util.ExceptionUtil;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**-------- 通用异常处理方法 --------**/

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public R error(BusinessException e) {
    	log.error(ExceptionUtil.getMessage(e));
        return R.error().message(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e) {
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message(e.getMessage());    // 通用异常结果
    }
}