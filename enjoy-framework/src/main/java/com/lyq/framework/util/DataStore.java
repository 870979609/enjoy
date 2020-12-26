package com.lyq.framework.util;

import com.lyq.framework.common.exception.Alert;
import com.lyq.framework.common.exception.AppException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Primary
@Component
public class DataStore extends ArrayList<DataObject>{

	private static final long serialVersionUID = 1L;

	protected DataStore() {}

	protected DataStore(int rowInit) {
		super(rowInit);
	}

	public static DataStore getInstance() {
		return new DataStore();
	}

	public static DataStore getInstance(int rowInit) {
		return new DataStore(rowInit);
	}

	private void checkRow(int i) throws AppException {
		if (i < 0 || i >= rowCount()) {
			Alert.notExist("无效行号【" + i + "】");
		}
	}

	public void addRow() throws AppException {
		add(new DataObject());
	}

	public void addRow(DataObject o) {
		add(o);
	}

	public final int rowCount() {
		return super.size();
	}

	public DataObject getRow(int row) throws AppException {
		checkRow(row);
		Object o = super.get(row);
		if ((o instanceof DataObject)) {
			return (DataObject) o;
		}

		Alert.formatError("DataStore中不是DataObject类型，无法获取！");

		return null;
	}

	public Object put(int i, String columnName, Object value) throws AppException {
		if (i == rowCount()) {
			addRow();
		} else {
			checkRow(i);
		}

		DataObject rdo = getRow(i);
		Object ret = rdo.put(columnName, value);

		return ret;
	}

	public Object getObject(int i, String column) throws AppException {
		return getRow(i).getObject(column);
	}

	public String getString(int i, String column) throws AppException {
		return getRow(i).getString(column);
	}

	public String getString(int i, String column, String pDefault) throws AppException {
		return getRow(i).getString(column, pDefault);
	}

	public DataStore subDataStore(int beginRow, int endRow) throws AppException {
		if (rowCount() == 0) {
			DataStore newDataStore = new DataStore();
			return newDataStore;
		}
		checkRow(beginRow);

		DataStore newDataStore = null;
		if (endRow >= rowCount()) {
			newDataStore = new DataStore(rowCount() - beginRow + 1);
			for (int i = beginRow; i < rowCount(); i++)
				newDataStore.addRow(getRow(i).clone());
		} else {
			newDataStore = new DataStore(endRow - beginRow + 1);
			for (int i = beginRow; i < endRow; i++) {
				newDataStore.addRow(getRow(i).clone());
			}
		}

		return newDataStore;
	}

	public void combineDatastore(DataStore otherds) throws AppException {
		for (int i = 0; i < otherds.rowCount(); i++) {
			DataObject row = otherds.getRow(i).clone();
			addRow(row);
		}
	}
}
