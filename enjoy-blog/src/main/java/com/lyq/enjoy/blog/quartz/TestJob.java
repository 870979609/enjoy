package com.lyq.enjoy.blog.quartz;

import com.lyq.framework.quartz.TWO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJob extends TWO {
	private static Logger logger = LoggerFactory.getLogger(TestJob.class);


	@Override
	protected int doExecute(String parameter) {
		logger.info("--------测试job---------");
		return 0;
	}

}
