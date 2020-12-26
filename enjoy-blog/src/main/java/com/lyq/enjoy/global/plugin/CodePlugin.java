package com.lyq.enjoy.global.plugin;

import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.common.plugin.AppPlugin;
import com.lyq.framework.common.plugin.CodeCache;
import com.lyq.framework.util.DataStore;

/**
 * 加载一些code项到内存中
 * 
 * @author lixinyu 2020-05-22
 * @version 1.0
 */
public class CodePlugin extends AppPlugin {

	public void addContent(String name) throws AppException {

		/**
		 * 查系统code表
		 */
		this.sql.clearSql();
		this.sql.addSql(" select dmbh, code, content ");
		this.sql.addSql("   from enjoy.code_config ");
		this.sql.addSql("  where appid = ? ");
		this.sql.setString(1, GlobalNames.WEB_APPID);
		DataStore ds = this.sql.executeQuery();

		for (int i = 0; i < ds.rowCount(); i++) {
			String dmbh = ds.getString(i, "dmbh");
			String code = ds.getString(i, "code");
			String content = ds.getString(i, "content");

			CodeCache.addCode(dmbh, code, content);
		}
		
		logger.info("-------加载code完毕-------");
	}
}
