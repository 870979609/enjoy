package com.lyq.framework.util.sequence;

import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.util.StringUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZooKeeperStrategy implements KeyStrategy {

    private static RetryPolicy retryPolicy;

    private static CuratorFramework client;

    private static ExecutorService executorService;

    private static String ROOT = "/enjoy/generateId";

    private static String prefix = "/p";

    private static String ClusterNode = "192.168.0.101:2181";

    static {
        retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client =
                CuratorFrameworkFactory.builder()
                        .connectString(ClusterNode)
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();

        client.start();

        executorService = Executors.newFixedThreadPool(10);

        try {
            Stat stat = client.checkExists().forPath(ROOT);

            if (stat == null) {
                client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(ROOT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generate(String seqName) throws AppException {

        String ID = "";

        String fullPath = ROOT.concat("/").concat(seqName.toLowerCase()).concat(prefix);
        try {
            // 关键点：创建持久顺序节点
            String backPath = client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(fullPath, null);
            //为防止生成的节点浪费系统资源，故生成后异步删除此节点
            String finalBackPath = backPath;
            executorService.execute(() -> {
                try {
                    client.delete().forPath(finalBackPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            ID = splitID(backPath, seqName.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtil.isEmpty(ID)) {
            throw new AppException("未获取到ID！");
        }

        return ID;
    }

    public String splitID(String path, String seqName) {
        int index = path.lastIndexOf(seqName);
        if (index >= 0) {
            index += (seqName + prefix).length();
            return index <= path.length() ? path.substring(index) : "";
        }
        return path;
    }
}
