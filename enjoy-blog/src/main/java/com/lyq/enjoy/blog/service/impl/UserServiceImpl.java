package com.lyq.enjoy.blog.service.impl;

import com.lyq.enjoy.blog.entity.Article;
import com.lyq.enjoy.blog.entity.User;
import com.lyq.enjoy.blog.mapper.ArticleMapper;
import com.lyq.enjoy.blog.service.ArticleService;
import com.lyq.enjoy.blog.service.UserService;
import com.lyq.enjoy.blog.util.BlogUtil;
import com.lyq.framework.common.business.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends AbstractService implements UserService {

    @Override
    public User getUserByToken(String token) {

        User user = new User();
        user.setId("adminid");
        return user;
    }
}
