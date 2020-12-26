package com.lyq.enjoy.blog.util;

import com.lyq.framework.common.exception.BusinessException;
import com.lyq.framework.common.exception.ExceptionCode;
import com.lyq.framework.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BlogUtil {

    /*
     * @Description
     * 校验Map中参数列是否为空
     * columnList格式： "id:主键,name:姓名,birthday:出生日期"
     * @Author lixinyu
     * @Date 2020/10/28 21:38
     **/
    public static void checkRequiredPara(String columnList, Map para) {

        if (StringUtil.isEmpty(columnList)) {
            throw new BusinessException("校验列格式串为空!");
        }

        if (isNull(para)) {
            throw new BusinessException("被校验参数Map为空!");
        }

        String[] columnArr = columnList.split(",");

        for (int i = 0; i < columnArr.length; i++) {
            String column = columnArr[i];

            String[] columnInfo = column.split(":");
            if (columnInfo.length < 1) {
                throw new BusinessException("校验列格式串错误!");
            }

            String key = columnInfo[0].toLowerCase();
            String description = columnInfo.length > 1 ? columnInfo[1] : key;

            if (!para.containsKey(key)) {
                throw new BusinessException("不存在入参【" + description + "】!");
            } else {
                Object value = para.get(key);
                if (((value instanceof String) && StringUtil.isEmpty((String) value))
                        || ((value instanceof Date) && value == null)
                        || ((value instanceof List) && ((List) value).size() == 0)
                        ) {
                    throw new BusinessException("入参【" + description + "】为空!");
                }
            }
        }
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }
}
