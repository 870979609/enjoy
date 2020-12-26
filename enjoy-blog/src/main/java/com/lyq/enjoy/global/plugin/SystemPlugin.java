package com.lyq.enjoy.global.plugin;

import com.lyq.enjoy.global.EnjoyNames;
import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.common.plugin.AppPlugin;
import com.lyq.framework.util.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 此类是加载该应用的系统参数
 * 
 * @author lixinyu 2020-05-22
 * @version 1.0
 */
public class SystemPlugin extends AppPlugin{
	private static Logger logger = LoggerFactory.getLogger(SystemPlugin.class);

	public void addContent(String name) throws AppException {
		
		/**
		 * 查系统参数表
		 */
		this.sql.clearSql();
		this.sql.addSql(" select csbh, csz ");
		this.sql.addSql("   from enjoy.system_para ");
		this.sql.addSql("  where appid = ? ");
		this.sql.setString(1, GlobalNames.WEB_APPID);
		DataStore ds = this.sql.executeQuery();
		
		for (int i = 0; i < ds.rowCount(); i++) {
			String csbh = ds.getString(i, "csbh");
			String csz = ds.getString(i, "csz");

			if ("AUTHOR".equals(csbh.toUpperCase())) {
				EnjoyNames.Author = csz;
			}
		}
		
		logger.info("-------加载系统参数完毕-------");
	}
}
