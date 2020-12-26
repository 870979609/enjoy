package com.lyq.enjoy.blog.controller;

import com.lyq.enjoy.blog.entity.Article;
import com.lyq.enjoy.blog.service.ArticleService;
import com.lyq.enjoy.global.EnjoyNames;
import com.lyq.framework.common.business.AbstractController;
import com.lyq.framework.common.response.R;
import com.lyq.framework.util.CurrentUser;
import com.lyq.framework.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController extends AbstractController {

	@Autowired
	private ArticleService articleService;

	@RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
	public R get(@PathVariable String id) throws Exception {
		Article article = articleService.get(id);

		return R.ok().data(article);
	}

	@RequestMapping(value = "/articles", method = RequestMethod.GET)
	public R listAll() throws Exception {
		List<Article> articleList = articleService.listAll();

		return R.ok().data(articleList);
	}

	@RequestMapping(value = "/article", method = RequestMethod.POST)
	public R insert(@RequestBody Map para, HttpServletRequest request) throws Exception {
		para.put("currentUser_", getCurrentUser(request));
		articleService.insert(para);

		return R.ok();
	}

	@RequestMapping(value = "/article", method = RequestMethod.PUT)
	public R update(@RequestBody Map para, HttpServletRequest request) throws Exception {
		articleService.update(para);

		return R.ok();
	}

	@RequestMapping(value = "/article/{id}", method = RequestMethod.DELETE)
	public R delete(@PathVariable String id) throws Exception {
		articleService.delete(id);

		return R.ok().data("删除成功");
	}

	/*
	 * @Description 
	 * 为某个文章点赞，保存到redis
	 * @Author lixinyu
	 * @Date 2020/11/13 21:46
	 **/
	@RequestMapping(value = "/article/thumbsup", method = RequestMethod.POST)
	public R thumbsUp(@RequestParam Map para, HttpServletRequest request) throws Exception {
		String articleId = (String) para.get("articleId");
		String userId = getCurrentUser(request).getUserId();

		articleService.thumbsUp(articleId, userId);
		return R.ok().data("点赞成功");
	}
}
