package com.lyq.framework.util;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.spring.SpringBeanUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @Description
 * @Author lixinyu
 * @Date 2020/12/6 1:22
 * @Version 1.0
 **/
public class FastDFSUtil {
    private static Logger log = LoggerFactory.getLogger(SpringBeanUtil.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static FastFileStorageClient fastFileStorageClient = null;

    static {
        try {
            fastFileStorageClient = SpringBeanUtil.getBean(FastFileStorageClient.class);
        } catch (Throwable e) {
            log.error("初始化FastDFS工具类失败， " + e.getMessage());
        }
    }

    /**
     * 上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String upload(MultipartFile file) throws IOException, AppException {
        // 上传
        StorePath storePath = getClient().uploadFile(
                file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()),
                null);
        return getFullPath(storePath);
    }

    /**
     * 删除
     *
     * @param path
     */
    public static void delete(String path) throws AppException {
        getClient().deleteFile(path);
    }

    /**
     * 删除
     *
     * @param group
     * @param path
     */
    public static void delete(String group, String path) throws AppException {
        getClient().deleteFile(group, path);
    }

    /**
     * 文件下载
     *
     * @param path     文件路径，例如：/group1/path=M00/00/00/itstyle.png
     * @param filename 下载的文件命名
     * @return
     */
    public static void download(String path, String filename, HttpServletResponse response) throws IOException, AppException {
        // 获取文件
        StorePath storePath = StorePath.parseFromUrl(path);
        if (StringUtils.isBlank(filename)) {
            filename = FilenameUtils.getName(storePath.getPath());
        }
        byte[] bytes = getClient().downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        response.reset();
        response.setContentType("applicatoin/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }

    public static FastFileStorageClient getClient() throws AppException {
        if (fastFileStorageClient == null) {
            throw new AppException("FastDFS初始化失败,请检查！");
        }
        return fastFileStorageClient;
    }

    private static String getFullPath(StorePath storePath){
        String pathPrefix = PropertiesUtil.getString("enjoy.fastdfs.prefix", "localhost:8080");

        return "http://" + pathPrefix + "/" + storePath.getFullPath();
    }
}
