package com.lyq.enjoy.blog.controller;

import com.lyq.enjoy.blog.entity.Article;
import com.lyq.enjoy.blog.entity.Category;
import com.lyq.enjoy.blog.entity.Tag;
import com.lyq.enjoy.blog.service.ArticleService;
import com.lyq.enjoy.blog.service.CategoryService;
import com.lyq.framework.common.business.AbstractController;
import com.lyq.framework.common.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController extends AbstractController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
	public R getByCategoryId(@PathVariable String id) throws Exception {
		Category category = categoryService.getCategory(id);

		return R.ok().data(category);
	}

	@RequestMapping(value = "/category/user/{userid}", method = RequestMethod.GET)
	public R getByUserId(@PathVariable String userid) throws Exception {
		HashMap para = new HashMap();
		para.put("userid", userid);
		List<Category> categoryList = categoryService.getAvailableCategorys(para);

		return R.ok().data(categoryList);
	}

	@RequestMapping(value = "/categorys", method = RequestMethod.GET)
	public R categorysAndTags(HttpServletRequest request) throws Exception {
		HashMap para = new HashMap();
		para.put("userid", getCurrentUser(request).getUserId());

		List<Category> categoryList = categoryService.getAvailableCategorys(para);

		List<Tag> tagList = categoryService.getAvailableTags(para);

		para.clear();
		para.put("categorys", categoryList);
		para.put("tags", tagList);
		return R.ok().data(para);
	}
}
