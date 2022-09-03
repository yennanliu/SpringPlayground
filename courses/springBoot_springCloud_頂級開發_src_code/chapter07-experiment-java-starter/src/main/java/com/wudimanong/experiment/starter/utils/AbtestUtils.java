package com.wudimanong.experiment.starter.utils;

/**
 * @author jiangqiao
 */
public class AbtestUtils {

    /**
     * 计算当前层流量分桶编号工具方法
     *
     * @param currIdStr
     * @param layerId
     * @return
     */
    public static Long getBucketNo(String currIdStr, Integer layerId) {
        //将分流标示ID与流量分层ID拼装
        String destKey = currIdStr + layerId;
        //取Md5哈希值
        String md5Hex = Md5Utils.md5Hex(destKey, "UTF-8");
        //获取Hash数值类型
        Long hash = Long.parseLong(md5Hex.substring(md5Hex.length() - 16, md5Hex.length() - 1), 16);
        if (hash < 0) {
            hash *= -1;
        }
        // 取模
        return (hash % 1000L);
    }
}
