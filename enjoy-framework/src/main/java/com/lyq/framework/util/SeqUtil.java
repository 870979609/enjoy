package com.lyq.framework.util;

import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.sequence.KeyStrategy;
import com.lyq.framework.util.sequence.KeyStrategyFactory;

public class SeqUtil {
    private static KeyStrategy keyStrategy = null;

    public static String getId(String seqName) throws AppException {
        init();

        return keyStrategy.generate(seqName);
    }

    public static void init() throws AppException {
        if (keyStrategy == null) {
            keyStrategy = KeyStrategyFactory.getInstance();
        }
    }
}
