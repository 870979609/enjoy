package com.lyq.framework.util;

/**
 * @Description
 * @Author lixinyu
 * @Date 2020/11/22 19:03
 * @Version 1.0
 **/
public class Stub {

    public String dealRequestURL(String post_url)
    {
        if (!post_url.startsWith("http://")) {
            post_url = "http://" + post_url;
        }
        if (!post_url.endsWith("/")) {
            post_url = post_url + "/";
        }
        return post_url;
    }
}
