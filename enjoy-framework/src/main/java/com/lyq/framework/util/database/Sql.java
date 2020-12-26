package com.lyq.framework.util.database;

import com.lyq.framework.common.exception.Alert;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.DataStore;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class Sql implements Serializable {

    private static final long serialVersionUID = 6432990036292401793L;
    private StringBuffer sqlString = new StringBuffer();
    private ArrayList<Object> para = new ArrayList<Object>();
    private String dbName = null;
    private ArrayList<Object[]> batchParaList = null;
    private boolean batchFlag = false;

    private JdbcTemplate jdbcTemplate = null;

    private Sql() {
        this.dbName = "dataSource";
    }

    private Sql(String dbName) {
        this.dbName = dbName;
    }

    public static Sql getInstance() {
        return new Sql();
    }

    public static Sql getInstance(String dbName) {
        return new Sql(dbName);
    }

    public void clearSql() {
        this.sqlString.setLength(0);
        para.clear();
    }

    public void addSql(String sql) throws AppException {
        if (this.batchFlag == true) {
            throw new AppException("当前SQL类已经设置了Batch参数，不能重新设置SQL String，请重新实例化SQL或执行ExecuteBatch或执行resetBatch方法后，再设置SQL String。");
        }
        this.sqlString.append(sql);
    }

    public void setString(int index, String value) throws AppException {
        handlePara(index, value);
    }

    public void setInt(int index, int value) throws AppException {
        handlePara(index, value);
    }

    public void setBigDecimal(int index, BigDecimal value) throws AppException {
        handlePara(index, value);
    }

    public void setBigDecimal(int index, String value) throws AppException {
        handlePara(index, new BigDecimal(value));
    }

    public void setBigDecimal(int index, int value) throws AppException {
        handlePara(index, new BigDecimal(value));
    }

    public void setBigDecimal(int index, Integer value) throws AppException {
        handlePara(index, new BigDecimal(value.intValue()));
    }

    public void setBigDecimal(int index, double value) throws AppException {
        handlePara(index, new BigDecimal(Double.toString(value)));
    }

    public void setDouble(int index, double value) throws AppException {
        handlePara(index, value);
    }

    public void setDate(int index, java.util.Date value) throws AppException {
        handlePara(index, new java.sql.Date(value.getTime()));
    }

    public void setDateTime(int index, java.util.Date value) throws AppException {
        handlePara(index, new Timestamp(value.getTime()));
    }

    private void handlePara(int index, Object value) throws AppException {
        if (index > this.para.size())
            this.para.add(index - 1, value);
        else
            this.para.set(index - 1, value);
    }

    public DataStore executeQuery() throws AppException {
        DataStore ds = null;

        return executeSelectSQL(this.sqlString.toString(), this.para);
    }

    @SuppressWarnings("rawtypes")
    private DataStore executeSelectSQL(String sqlString, ArrayList<Object> para) throws AppException {

        PreparedStatementSetter pss = new PreparedStatementSetter() {
            ArrayList<Object> exePara = para;

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                Object[] paras = this.exePara.toArray();

                try {
                    Sql.this.setParas(pstmt, paras);
                } catch (AppException e) {
                    throw new SQLException(e);
                }
            }
        };

        ResultSetExtractor resultSetExtractor = new ResultSetExtractor() {

            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                ResultSetMetaData rsmd = rs.getMetaData();
                int colCount = rsmd.getColumnCount();

                DataStore ds = DataStore.getInstance();

                while (rs.next()) {
                    try {
                        ds.addRow();

                        for (int i = 0; i < colCount; i++) {
                            // String columnName = rsmd.getColumnName(i +
                            // 1).toLowerCase();
                            String columnLabel = rsmd.getColumnLabel(i + 1).toLowerCase();// sql中起的别名

                            ds.put(ds.rowCount() - 1, columnLabel, rs.getObject(i + 1));
                        }
                    } catch (AppException e) {
                        throw new SQLException(e);
                    }
                }

                return ds;
            }
        };

        this.jdbcTemplate = DatabaseSessionUtil.getCurrentSession(this.dbName);
        DataStore ds = (DataStore) this.jdbcTemplate.query(sqlString, pss, resultSetExtractor);

        return ds;
    }

    public int executeUpdate() throws AppException {

        if (this.batchFlag == true) {
            throw new AppException("当前SQL类已经设置了Batch参数，不能重新设置SQL String，请重新实例化SQL或执行ExecuteBatch或执行resetBatch方法后，再设置SQL String。");
        }

        Transaction trans = TransactionManager.getTransaction(this.dbName);
        if (!trans.isUnderTransaction()) {
            throw new AppException("当前数据库操作[" + this.sqlString + "]未正常开启事务，请联系开发人员处理。");
        }

        this.jdbcTemplate = DatabaseSessionUtil.getCurrentSession(this.dbName);
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            ArrayList<Object> exePara = para;

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                Object[] paras = this.exePara.toArray();

                try {
                    Sql.this.setParas(pstmt, paras);
                } catch (AppException e) {
                    throw new SQLException(e);
                }
            }
        };

        int vi = this.jdbcTemplate.update(this.sqlString.toString(), pss);

        return vi;
    }

    protected void setParas(PreparedStatement pstmt, Object[] paras) throws AppException {
        if (paras == null) {
            return;
        }

        try {
            for (int i = 0; i < paras.length; i++) {
                Object o = paras[i];

                if (o instanceof String) {
                    pstmt.setString(i + 1, (String) o);
                } else if (o instanceof Integer) {
                    pstmt.setInt(i + 1, ((Integer) o).intValue());
                } else if (o instanceof Double) {
                    pstmt.setDouble(i + 1, ((Double) o).doubleValue());
                }
            }

        } catch (Exception e) {
            Alert.sqlError("数据库操作时发生异常,异常信息：" + e.getMessage());
        }
    }

    @SuppressWarnings("all")
    public void addBatch() throws AppException {
        if (this.batchParaList == null) {
            this.batchParaList = new ArrayList();
        }
        if (this.para == null) {
            return;
        }
        this.batchParaList.add(this.para.toArray());
        this.para = new ArrayList();
        this.batchFlag = true;
    }

    public void resetBatch() {
        this.batchParaList = null;
        this.batchFlag = false;
    }

    public int[] executeBatch() throws AppException {
        if (this.batchParaList == null) {
            return null;
        }

        Transaction trans = TransactionManager.getTransaction(this.dbName);
        if (!trans.isUnderTransaction()) {
            throw new AppException("当前数据库操作[" + this.sqlString + "]未正常开启事务，请联系开发人员处理。");
        }

        try {
            BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement pstmt, int i) throws SQLException {
                    Object[] paras = (Object[]) (Object[]) Sql.this.batchParaList.get(i);
                    try {
                        Sql.this.setParas(pstmt, paras);
                    } catch (AppException e) {
                        throw new SQLException(e);
                    }
                }

                public int getBatchSize() {
                    return Sql.this.batchParaList.size();
                }
            };

            this.jdbcTemplate = DatabaseSessionUtil.getCurrentSession(this.dbName);
            int[] result = this.jdbcTemplate.batchUpdate(this.sqlString.toString(), setter);

            this.batchParaList = null;

            return result;
        } catch (Exception ex) {
            throw new AppException(ex);
        } finally {
            this.batchFlag = false;
        }
    }
}
