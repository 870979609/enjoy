package com.lyq.enjoy.blog.mapper;

import com.lyq.enjoy.blog.entity.Article;
import com.lyq.enjoy.blog.entity.Category;
import com.lyq.enjoy.blog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {
    Category getByCategoryId(String id);
    List<Category> getByCategoryIds(List categorys);
    int save(Category category);
    List<Category> getAvailableCategorys(String userid);
    List<Tag> getAvailableTags(String userid);
    Tag getByTagId(String id);
    int saveTag(Tag tag);
}