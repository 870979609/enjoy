package com.lyq.enjoy.blog.service;

import com.lyq.enjoy.blog.entity.Article;
import com.lyq.enjoy.blog.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User getUserByToken(String token);
}
