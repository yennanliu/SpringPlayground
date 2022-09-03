package com.wudimanong.experiment.utils;

import java.util.List;
import lombok.Data;

/**
 * @author jiangqiao
 */
public class BucketAllocate {

    /**
     * 分桶操作辅助输入对象定义
     */
    @Data
    public static class Request {

        // 当前的分桶占比(举例: 25)
        private int currBucketRatio;
        // 目标的分桶占比
        private int destBucketRatio;
        // 分层中, 当前的未分配分桶编号
        private List<Integer> currUnusedBucketNoListOfLayer;
        // 分组内, 已经分配的分桶编号
        private List<Integer> currUsedBucketNoListOfGroup;
    }

    /**
     * 分桶操作辅助输出对象定义
     */
    @Data
    public static class Result {

        // 分桶占比
        private int bucketRatio;
        // 分层中, 当前的未分配分桶编号
        private List<Integer> unusedBucketNoListOfLayer;
        // 分组内, 已经分配的分桶编号
        private List<Integer> bucketNoListOfGroup;
    }
}
