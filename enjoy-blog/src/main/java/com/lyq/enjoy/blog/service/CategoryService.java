package com.lyq.enjoy.blog.service;

import com.lyq.enjoy.blog.entity.Category;
import com.lyq.enjoy.blog.entity.Tag;
import com.lyq.enjoy.blog.entity.User;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category> getAvailableCategorys(Map map);

    Category getCategory(String id);

    Tag getTag(String id);

    List<Tag> getAvailableTags(Map map);
}
