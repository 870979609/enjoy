package com.lyq.enjoy.blog.service;

import com.lyq.enjoy.blog.entity.Article;
import com.lyq.enjoy.blog.entity.User;
import com.lyq.framework.common.exception.AppException;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    int delete(String id);

    Map insert(Map para) throws AppException;

    Article get(String id);

    int update(Map para);

    List<Article> listAll();

    List<User> getArticleLikeUserList(Map para);

    boolean alreadyPraised(String cacheKey, String articleId, String userId);

    void thumbsUp(String articleId, String userId);

    int insertArticleUserLike(String articleId, String userId, Map para);
}
