package com.lyq.enjoy.blog.quartz;

import com.lyq.enjoy.blog.entity.Article;
import com.lyq.enjoy.blog.service.ArticleService;
import com.lyq.enjoy.global.EnjoyNames;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.quartz.TWO;
import com.lyq.framework.spring.SpringBeanUtil;
import com.lyq.framework.util.RedisUtil;
import com.lyq.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PushPraisedInfoTWO extends TWO {
	private static Logger logger = LoggerFactory.getLogger(PushPraisedInfoTWO.class);
	private static ArticleService articleService = SpringBeanUtil.getBean(ArticleService.class);

	@Override
	protected int doExecute(String parameter) {
		logger.info("--------点赞数据落地--------");

		RedisUtil redisUtil = RedisUtil.getInstance();
		ArrayList processed_key = new ArrayList();
		int count = 0;
		String articleId = (String) redisUtil.spop(EnjoyNames.Key_ArticleLike_Set);
		while (!StringUtil.isEmpty(articleId)) {
			String articleKey = EnjoyNames.Key_ArticleLike_User_Hash_Prefix + articleId;
			String articleNumKey = EnjoyNames.Key_ArticleLike_Num_Prefix + articleId;

			try {
				// 保存点赞数
				int articlePraiseNum = (int) redisUtil.get(articleNumKey);
				HashMap para = new HashMap();
				para.put("articleId", articleId);
				para.put("praiseNum", articlePraiseNum);
				articleService.update(para);

				Map<String, Object> entrys = redisUtil.hEntrys(articleKey);
				Set<String> keySets = entrys.keySet();
				for (String userId : keySets) {
					Map userLikeInfo = (Map) entrys.get(userId);

					articleService.insertArticleUserLike(articleId, userId, userLikeInfo);
				}

				processed_key.add(articleNumKey);
				processed_key.add(articleKey);

				saveException("articleKey:" + articleKey + " articleNumKey:" + articleNumKey + " 更新成功");
			} catch (Throwable e) {
				try {
					saveException("articleKey:" + articleKey + " articleNumKey:" + articleNumKey + " 更新失败，" + e.getMessage());
				} catch (AppException e1) {
					e1.printStackTrace();
				}
			}
		}

		// redis删除key
		redisUtil.del((String[])processed_key.toArray());

		return count;
	}

}
