package com.lyq.enjoy.global.controller;

import com.lyq.enjoy.global.mapper.CommonMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginService {

    @Resource
    private CommonMapper CommonMapper;
}
