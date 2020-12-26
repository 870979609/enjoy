package com.lyq.framework.util.sequence;

import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.common.exception.AppException;

import java.util.HashMap;

/**
 * 三位数据库ID + 自定义序列值 + 4位顺序号
 */
public class DBLockStrategy implements KeyStrategy {
    private static volatile HashMap<String, SeqObject> map = new HashMap<String, SeqObject>();

    @Override
    public String generate(String seqName) throws AppException {
        if (!map.containsKey(seqName)) {
            synchronized (map) {
                if (!map.containsKey(seqName)) {
                    SeqObject seqObject = new SeqObject();
                    seqObject.init(seqName);
                    map.put(seqName, seqObject);
                }
            }
        }

        SeqObject seqObject = map.get(seqName);
        if (seqObject == null) {
            throw new AppException("未获取到SeqObject对象！");
        }

        return seqObject.nextId();
    }
}
