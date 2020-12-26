package com.lyq.framework.util.sequence;

import com.lyq.framework.common.exception.AppException;

public interface KeyStrategy {

    String generate(String seqName) throws AppException;
}
