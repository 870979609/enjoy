package com.lyq.framework.spring.listener;

import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.PropertiesUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.util.*;

//@Component

/**
 * 主要是系统启动时加载application.properties和jvm启动参数配置
 * <p>
 * 配置中心地址，环境参数等
 *
 * @author lixinyu 2020-05-22
 * @version 1.0
 */
public class XYStartingPlugInListener implements ApplicationListener<ApplicationStartingEvent> {

    private static SpringApplication atx = null;

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        System.out.println("--------XYStartingPlugInListener-----");
        if (atx != null) {
            return;
        }
        atx = event.getSpringApplication();

        loadPara();
        loadJVMPara();

        // 加载测试appurl
        loadTestUrl();
    }

    /**
     * 初始化全局变量
     *
     * @author lixinyu 2020-05-22
     * @version 1.0
     */
    private void loadPara() {
        try {
            Properties applicationProp = PropertiesUtil.getProp("enjoy");

            if ((applicationProp == null) || (applicationProp.isEmpty())) {
                throw new AppException("【enjoy.properties】配置文件不存在");
            }

            Set keySet = applicationProp.keySet();
            for (Iterator localIterator1 = keySet.iterator(); localIterator1.hasNext(); ) {
                Object key = localIterator1.next();

                String itemName = key.toString();
                String itemValue = applicationProp.getProperty(itemName);

                if ("enjoy.APPID".equalsIgnoreCase(itemName)) {
                    GlobalNames.WEB_APPID = itemValue;
                } else if ("enjoy.CONFIGINWAR".equalsIgnoreCase(itemName)) {
                    GlobalNames.CONFIGINWAR = itemValue;
                } else if ("enjoy.USE_CONFIG_CENTER".equalsIgnoreCase(itemName)) {
                    GlobalNames.USE_CONFIG_CENTER = Boolean.valueOf(itemValue);
                } else if ("enjoy.DEBUGMODE".equalsIgnoreCase(itemName)) {
                    GlobalNames.DEBUGMODE = Boolean.valueOf(itemValue);
                }

                PropertiesUtil.put(itemName, itemValue);
            }

        } catch (AppException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * jvm参数优先级最高
     *
     * @author lixinyu 2020-05-21
     * @version 1.0
     */
    private void loadJVMPara() {
        try {
            String para1 = System.getProperty("para1");
            String para2 = System.getProperty("para2");

            if ((StringUtils.isEmpty(para1) && !StringUtils.isEmpty(para2)) || (StringUtils.isEmpty(para2) && !StringUtils.isEmpty(para1))) {
                return;
            }

            if ((StringUtils.isEmpty(para1)) && (StringUtils.isEmpty(para2))) {
                Properties prop = PropertiesUtil.getProp("application");

                para1 = prop.getProperty("enjoy.配置中心地址");
                para2 = prop.getProperty("enjoy.cuid");
            }

            // GlobalNames.CURRENTCUID = cuid;
            // GlobalNames.POLARISURL = polarisurl;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void loadTestUrl() {
        try {
            if (!GlobalNames.DEBUGMODE) {
                return ;
            }

            Properties applicationProp = PropertiesUtil.getProp("testUrl");

            if ((applicationProp == null) || (applicationProp.isEmpty())) {
                throw new AppException("【testUrl.properties】配置文件不存在");
            }

            Set keySet = applicationProp.keySet();
            Map map = GlobalNames.TESTURL_MAP == null ? new HashMap() : GlobalNames.TESTURL_MAP;
            for (Iterator localIterator1 = keySet.iterator(); localIterator1.hasNext(); ) {
                Object key = localIterator1.next();

                String itemName = key.toString();
                String itemValue = applicationProp.getProperty(itemName);


                map.put(itemName.toUpperCase(), itemValue);
            }

            GlobalNames.TESTURL_MAP = map;
        } catch (AppException e) {
            e.printStackTrace();
        }
    }
}
