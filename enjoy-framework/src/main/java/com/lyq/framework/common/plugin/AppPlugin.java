package com.lyq.framework.common.plugin;

import com.lyq.framework.common.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lyq.framework.util.database.Sql;

public abstract class AppPlugin{

	protected static Logger logger = LoggerFactory.getLogger(AppPlugin.class);
	protected Sql sql = Sql.getInstance();
	
	public abstract void addContent(String paramString) throws AppException;
}
