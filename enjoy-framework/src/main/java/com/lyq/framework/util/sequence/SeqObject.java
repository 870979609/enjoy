package com.lyq.framework.util.sequence;

import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.DataStore;
import com.lyq.framework.util.StringUtil;
import com.lyq.framework.util.database.Sql;
import com.lyq.framework.util.database.Transaction;
import com.lyq.framework.util.database.TransactionManager;

import java.math.BigDecimal;

/**
 * 锁表方式获取分布式唯一键
 */
public class SeqObject {

    private static final String allDigit = "0123456789";
    private static final String allLetter = "ABCDEFGHJKLMNPQRSTUVWXYZ";

    private String prefix;
    private String currentHead;
    private String currentTail;  // 4
    private String seqname;
    private String seqType;
    private Sql sql = Sql.getInstance();

    public void init(String seqName) throws AppException {
        if (StringUtil.isEmpty(GlobalNames.DBID)) {
            throw new AppException("DBID未设置！");
        }

        prefix = GlobalNames.DBID;
        seqname = seqName.toLowerCase();
        // 之后分布式可能需要调微服务去获取currentHead
        getNewHead();
    }

    public synchronized String nextId() throws AppException {
        if (StringUtil.isEmpty(prefix)) {
            throw new AppException("未初始化seqObject.prefix！");
        }
        if (StringUtil.isEmpty(currentHead)) {
            throw new AppException("未初始化seqObject.currentHead！");
        }
        if (StringUtil.isEmpty(currentTail)) {
            throw new AppException("未初始化seqObject.currentTail！");
        }

        if ("1".equals(seqType)) {
            if ("9999".equals(currentTail)) {
                getNewHead();
                return prefix + currentHead + currentTail;
            }

            currentTail = incValue(currentTail, seqType);

            return prefix + currentHead + currentTail;
        }

        return "";
    }

    private void getNewHead() throws AppException {
        Transaction tm = null;
        try {
            tm = TransactionManager.getTransaction();
            tm.begin();

            sql.clearSql();
            sql.addSql(" select currenthead, maxhead, seqtype from enjoy.seq_info where seqname = ? for update ");
            sql.setString(1, seqname);
            DataStore ds = sql.executeQuery();
            if (ds.rowCount() == 0) {
                throw new AppException("不存在编号为[" + seqname + "]的序列名！");
            }

            String maxHead = ds.getString(0, "maxhead");
            String type = ds.getString(0, "seqtype");
            String head = ds.getString(0, "currentHead");
            if (StringUtil.isEmpty(head)) {
                throw new AppException("配置的序列[" + seqname + "]值不正确！");
            }

            // 是否到了最大值
            if ("1".equals(type)) {
                BigDecimal maxHeadBigDecimal = new BigDecimal(maxHead);
                BigDecimal curHeadBigDecimal = new BigDecimal(head);

                if (curHeadBigDecimal.compareTo(maxHeadBigDecimal) >= 0) {
                    throw new AppException("配置的序列[" + seqname + "]值已经超过最大值！");
                }
            }

            head = incValue(head, type); // 递增1

            // 更新入库
            sql.clearSql();
            sql.addSql(" update enjoy.seq_info set currenthead = ? where seqname = ?  ");
            sql.setString(1, head);
            sql.setString(2, seqname);
            sql.executeUpdate();

            currentHead = head;
            currentTail = "0000";
            seqType = type;
        } finally {
            if (tm != null) {
                tm.commitWithoutStart();
            }
        }
    }

    /**
     * 自增1
     * @param var
     * @param seqType
     * @return
     */
    private String incValue(String var, String seqType) {
        if ("1".equals(seqType)) {// 纯数字

            int i = var.length();
            boolean needCarry = true;
            String preDigital = "", suffixDigital = "";
            while (needCarry && i > 0) {
                preDigital = var.substring(0, i - 1);
                char charI = var.charAt(i - 1);
                if (allDigit.indexOf(charI) != allDigit.length() - 1) {
                    needCarry = false;
                }

                suffixDigital = (allDigit.indexOf(charI) + 1) % allDigit.length() + suffixDigital;

                i--;
            }

            if (needCarry) {
                suffixDigital = "0" + suffixDigital;
            }

            return preDigital + suffixDigital;
        } else {

        }
        return "";
    }
}
