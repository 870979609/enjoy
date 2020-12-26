package com.lyq.enjoy.blog.service.impl;

import com.lyq.enjoy.blog.entity.Category;
import com.lyq.enjoy.blog.entity.Tag;
import com.lyq.enjoy.blog.entity.User;
import com.lyq.enjoy.blog.mapper.CategoryMapper;
import com.lyq.enjoy.blog.util.BlogUtil;
import com.lyq.enjoy.global.EnjoyNames;
import com.lyq.framework.common.business.AbstractService;
import com.lyq.enjoy.blog.entity.Article;
import com.lyq.enjoy.blog.mapper.ArticleMapper;
import com.lyq.enjoy.blog.service.ArticleService;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.CurrentUser;
import com.lyq.framework.util.RedisUtil;
import com.lyq.framework.util.SeqUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl extends AbstractService implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public Map insert(Map para) throws AppException {
        List<Map> relationList = new ArrayList<>();
        Map<String, String> tempMap = new HashMap<>();
        checkForSaveArticle(para);

        CurrentUser currentUser_ = (CurrentUser) para.get("currentUser_");
        List<String> categoryList = (List) para.get("categorys");
        List<String> tagList = (List) para.get("tags");
        String content = (String) para.get("content");

        // 生成主键
        String article_id = SeqUtil.getId("article_id");

        String categorysNameStr = "";
        for (int i = 0; i < categoryList.size(); i++) {
            String categoryId = categoryList.get(i);
            Category category = categoryMapper.getByCategoryId(categoryId);

            if (category != null) {
                // 已存在的分类
                categorysNameStr += "," + category.getName();
            } else {
                // 需要新增的分类
                String categoryName = categoryId;
                categoryId = SeqUtil.getId("category_id");

                Category categoryNew = new Category();
                categoryNew.setId(categoryId);
                categoryNew.setName(categoryName);
                categoryNew.setUserId(currentUser_.getUserId());
                categoryNew.setDescription(null);
                categoryNew.setImgUrl(null);
                categoryMapper.save(categoryNew);

                categoryList.set(i, categoryId);
            }

            HashMap relationMap = new HashMap();
            relationMap.put("articleid", article_id);
            relationMap.put("categoryid", categoryId);
            relationList.add(relationMap);
        }

        categorysNameStr = categorysNameStr.length() > 0 ? categorysNameStr.substring(1) : "";

        String tagsNameStr = "";
        for (int i = 0; i < tagList.size(); i++) {
            String tagId = tagList.get(i);
            Tag tag = categoryMapper.getByTagId(tagId);

            if (tag != null) {
                // 已存在的
                tagsNameStr += "," + tag.getName();
            } else {
                // 需要新增的
                String tagName = tagId;
                tagId = SeqUtil.getId("tag_id");

                Tag tagNew = new Tag();
                tagNew.setId(tagId);
                tagNew.setName(tagName);
                tagNew.setUserId(currentUser_.getUserId());
                categoryMapper.saveTag(tagNew);
            }
        }

        Article article = new Article();
        article.setUserId(currentUser_.getUserId());
        article.setTitle((String) para.get("title"));
        article.setCategorys(categoryList);
        article.setCategorysName(categorysNameStr);
        article.setTags(tagList);
        article.setTagsName(tagsNameStr);
        article.setIsTop(false);
        article.setOrigin((String) para.getOrDefault("origin", "1"));// 默认原创
        article.setSummary((String) para.get("summary"));
        article.setVisibility((String) para.getOrDefault("visibility", "1"));// 默认私密
        article.setStatus("2");// 审核中
        article.setId(article_id);

        // 保存文章主表
        articleMapper.insert(article);

        // 保存内容表
        tempMap.clear();
        tempMap.put("article_id", article_id);
        tempMap.put("content", content);
        articleMapper.saveArticleContent(tempMap);

        // 保存关系表
        articleMapper.saveRelationOfArticleAndCategory(relationList);

        return null;
    }

    @Override
    public Article get(String id) {
        Article articleWithContent = articleMapper.getArticleWithContent(id);


        return null;
    }

    @Override
    public int update(Map para) {
        BlogUtil.checkRequiredPara("articleId:文章ID", para);

        String articleId = (String) para.get("articleId");
        int praiseNum = (int) para.getOrDefault("praiseNum", 0);

        Article article = new Article();
        article.setId(articleId);
        article.setPraiseNum(praiseNum);
        return articleMapper.updateByArticleId(article);
    }

    @Override
    public List<Article> listAll() {
        return articleMapper.listAll();
    }

    /*
     * @Description
     *
     * @Author lixinyu
     * @Date 2020/10/28 20:59
     **/
    public void checkForSaveArticle(Map para) {
        // 校验必填
        BlogUtil.checkRequiredPara("title:文章标题,categorys:文章分类,origin:文章来源,visibility:可见范围", para);
    }

    /*
     * @Description
     * 文章点赞
     * @Author lixinyu
     * @Date 2020/11/15 16:11
     **/
    public void thumbsUp(String articleId, String userId) {
        String articleKey = EnjoyNames.Key_ArticleLike_User_Hash_Prefix + articleId;
        String articleNumKey = EnjoyNames.Key_ArticleLike_Num_Prefix + articleId;

        RedisUtil redisUtil = RedisUtil.getInstance();

        redisUtil.sset(EnjoyNames.Key_ArticleLike_Set, articleId);

        // 如果redis不存在此文章key
        if (!redisUtil.hasKey(articleKey)) {
            HashMap para = new HashMap();
            para.put("time", new Date());
            redisUtil.hset(articleKey, userId, para);
            redisUtil.incr(articleNumKey, 1);
        }

        // 如果redis存在此文章key，再判断是点赞还是取消赞
        if (alreadyPraised(articleKey, articleId, userId)) {
            // 取消赞
            redisUtil.hdel(articleKey, userId);
            redisUtil.decr(articleNumKey, 1);
        } else {
            // 点赞
            HashMap para = new HashMap();
            para.put("time", new Date());
            redisUtil.hset(articleKey, userId, para);
            redisUtil.incr(articleNumKey, 1);
        }

    }

    /*
     * @Description
     * 是否此用户已经点赞了
     * @Author lixinyu
     * @Date 2020/11/15 16:21
     **/
    public boolean alreadyPraised(String articleKey, String articleId, String userId) {
        RedisUtil redisUtil = RedisUtil.getInstance();

        if (redisUtil.hHasKey(articleKey, userId)) {
            return true;
        }

        HashMap para = new HashMap();
        para.clear();
        para.put("articleId", articleId);
        para.put("userId", userId);
        List<User> articleLikeUserList = getArticleLikeUserList(para);
        return articleLikeUserList == null ? false : articleLikeUserList.size() > 0;
    }

    /*
     * @Description
     * 获取文章的点赞用户列表
     * @Author lixinyu
     * @Date 2020/11/15 18:16
     **/
    public List<User> getArticleLikeUserList(Map para) {
        String articleId = (String) para.get("articleId");
        String userId = (String) para.getOrDefault("userId", "");

        para.clear();
        para.put("articleId", articleId);
        para.put("userId", userId);
        List<User> articleLikeUserList = articleMapper.getArticleLikeUserList(para);

        return articleLikeUserList;
    }

    /*
     * @Description
     * 插入点赞文章和用户关系表
     * @Author lixinyu
     * @Date 2020/11/15 18:16
     **/
    public int insertArticleUserLike(String articleId, String userId, Map para) {
        Date praiseTime = (Date) para.get("praiseTime");

        HashMap map = new HashMap();
        map.put("articleId", articleId);
        map.put("userId", userId);
        map.put("praiseTime", praiseTime);
        return articleMapper.insertArticleUserLike(map);
    }
}
