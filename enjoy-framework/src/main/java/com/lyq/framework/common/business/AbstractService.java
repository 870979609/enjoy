package com.lyq.framework.common.business;

import com.lyq.framework.common.exception.BusinessException;

/**
 * @Description
 * @Author lixinyu
 * @Date 2020/10/28 21:25
 * @Version 1.0
 **/
public abstract class AbstractService {
    public void businessException(String errmsg){
        throw new BusinessException(errmsg );
    }
    public void businessException(int code, String errmsg){
        throw new BusinessException(code, errmsg);
    }
}
