package com.lyq.enjoy.blog.service.impl;

import com.lyq.enjoy.blog.entity.Category;
import com.lyq.enjoy.blog.entity.Tag;
import com.lyq.enjoy.blog.entity.User;
import com.lyq.enjoy.blog.mapper.CategoryMapper;
import com.lyq.enjoy.blog.service.CategoryService;
import com.lyq.enjoy.blog.service.UserService;
import com.lyq.framework.common.business.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl extends AbstractService implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getAvailableCategorys(Map map) {
        String userid = (String) map.get("userid");

        List<Category> categoryList = categoryMapper.getAvailableCategorys(userid);
        return categoryList;
    }

    @Override
    public Category getCategory(String id) {
        Category category = categoryMapper.getByCategoryId(id);

        return category;
    }

    @Override
    public Tag getTag(String id) {
        Tag tag = categoryMapper.getByTagId(id);

        return tag;
    }

    @Override
    public List<Tag> getAvailableTags(Map map) {
        String userid = (String) map.get("userid");
        List<Tag> tags = categoryMapper.getAvailableTags(userid);

        return tags;
    }
}
