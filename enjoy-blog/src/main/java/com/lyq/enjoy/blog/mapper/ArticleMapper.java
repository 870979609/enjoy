package com.lyq.enjoy.blog.mapper;

import com.lyq.enjoy.blog.entity.Article;
import com.lyq.enjoy.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper {
    int deleteByArticleId(String id);

    int insert(Article article);

    Article getByArticleId(String id);

    Article getArticleWithContent(String id);

    int updateByArticleId(Article article);

    List<Article> listAll();

    int saveArticleContent(Map map);

    int saveRelationOfArticleAndCategory(List<Map> list);

    List<User> getArticleLikeUserList(Map map);

    int insertArticleUserLike(Map para);
}