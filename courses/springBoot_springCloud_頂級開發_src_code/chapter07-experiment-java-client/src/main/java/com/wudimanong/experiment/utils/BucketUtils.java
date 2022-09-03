package com.wudimanong.experiment.utils;

import java.util.Base64;
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jiangqiao
 */
public class BucketUtils {

    /**
     * 每个分层的 bucket 总数
     */
    public static final Integer BUCKET_TOTAL_NUM = 1000;

    /**
     * 原始分桶编号
     */
    private static final List<Integer> ORIGINAL_BUCKET_NOS = new LinkedList<>();

    // 初始化原始分桶编号
    static {
        for (int index = 0; index < BUCKET_TOTAL_NUM; index++) {
            ORIGINAL_BUCKET_NOS.add(index);
        }
    }

    /**
     * 洗牌，获取bucket的分桶编号
     *
     * @return
     */
    public static List<Integer> getShuffledBucketNoList() {
        List<Integer> currentBucketNos = new LinkedList<>(ORIGINAL_BUCKET_NOS);
        //这里通过集合对象提供的shuffle方法（使用指定的随机源对指定列表进行置换，所有置换发生的可能性都是大致相等的）来进行分桶编号洗牌
        Collections.shuffle(currentBucketNos);
        return currentBucketNos;
    }

    /**
     * 核心流量分桶调整逻辑（输入: 当前百分比、目标百分比, 未分配分桶号、 已分配分桶号. 输出: 当前百分比, 未分配分桶号、调整后已分配的分桶号）
     *
     * @param request
     * @return
     */
    public static BucketAllocate.Result bucketReallocate(BucketAllocate.Request request) throws Exception {
        //流量占比值在0～100之间
        if (request.getCurrBucketRatio() < 0 || request.getDestBucketRatio() < 0 || request.getCurrBucketRatio() > 100
                || request.getDestBucketRatio() > 100) {
            throw new Exception("flowRatio value is invalid", null);
        }
        //定义流量计算结果对象
        BucketAllocate.Result result = new BucketAllocate.Result();
        //计算目标占比与当前占比的差（单位：%）
        int gapPercent = request.getDestBucketRatio() - request.getCurrBucketRatio();
        if (gapPercent == 0) {
            //1）、目标占比与当前占比一致，则分组内分桶数量不变
            result.setBucketRatio(request.getDestBucketRatio());
            result.setUnusedBucketNoListOfLayer(request.getCurrUnusedBucketNoListOfLayer());
            result.setBucketNoListOfGroup(request.getCurrUsedBucketNoListOfGroup());
        } else if (gapPercent > 0) {
            //2）、目标分组内，需要扩充分桶数量
            //2-1）、这是一个核心公式，计算当前分组需要扩充的分桶数量
            int needAddBucketNumOfGroup = gapPercent * BUCKET_TOTAL_NUM / 100;
            //2-2）、检查当前流量层未使用分桶数量是否满足扩充需要
            List<Integer> currUnusedBucketNoListOfLayer = request.getCurrUnusedBucketNoListOfLayer();
            int unusedBucketNumOfLayer = currUnusedBucketNoListOfLayer == null ? 0
                    : currUnusedBucketNoListOfLayer.size();
            if (needAddBucketNumOfGroup > unusedBucketNumOfLayer) {
                throw new Exception("needAddBucketNumOfGroup > unusedBucketNumOfLayer", null);
            }
            //2-3)、调整流量分层中未使用的分桶编号，将其分配至相应分组
            //继续计算流量分层中应该持有的未分配桶数量
            int unusedBucketRemainNum = unusedBucketNumOfLayer - needAddBucketNumOfGroup;
            //根据比例将之前流量分层中未被使用的分桶按照新的占比进行重新分配
            List<Integer> currUsedBucketNoListOfGroup = request.getCurrUsedBucketNoListOfGroup();
            List<Integer> destUnusedBucketNoListOfLayer = new LinkedList<>();
            List<Integer> destUsedBucketNoListOfGroup = new LinkedList<>(currUsedBucketNoListOfGroup);
            for (int index = 0; index < unusedBucketNumOfLayer; index++) {
                Integer currBucketNo = currUnusedBucketNoListOfLayer.get(index);
                if (index < unusedBucketRemainNum) {
                    destUnusedBucketNoListOfLayer.add(currBucketNo);
                } else {
                    destUsedBucketNoListOfGroup.add(currBucketNo);
                }
            }
            //填充计算结果
            result.setBucketRatio(request.getDestBucketRatio());
            result.setUnusedBucketNoListOfLayer(destUnusedBucketNoListOfLayer);
            result.setBucketNoListOfGroup(destUsedBucketNoListOfGroup);
        } else {
            //3）、目标分组内，需要收缩分桶数量
            //3-1）、计算当前分组内需要收缩的分桶数量
            int needMinusBucketNumOfGroup = -1 * gapPercent * BUCKET_TOTAL_NUM / 100;
            //3-2）、检查当前分组内已使用的分桶数量是否满足待收缩数量的要求
            List<Integer> currUsedBucketNoListOfGroup = request.getCurrUsedBucketNoListOfGroup();
            int usedBucketNumOfGroup = currUsedBucketNoListOfGroup == null ? 0 : currUsedBucketNoListOfGroup.size();
            if (needMinusBucketNumOfGroup > usedBucketNumOfGroup) {
                throw new Exception("needMinusBucketNumOfGroup > usedBucketNumOfGroup", null);
            }
            //3-3)、调整当前分组内已使用的分桶编号，将其回收至流量分层中未使用分桶编号池中
            int usedBucketRemainNum = usedBucketNumOfGroup - needMinusBucketNumOfGroup;
            List<Integer> currUnusedBucketNoListOfLayer = request.getCurrUnusedBucketNoListOfLayer();
            List<Integer> destUnusedBucketNoListOfLayer = new LinkedList<>(currUnusedBucketNoListOfLayer);
            List<Integer> destUsedBucketNoListOfGroup = new LinkedList<>();
            for (int index = 0; index < usedBucketNumOfGroup; index++) {
                Integer currBucketNo = currUsedBucketNoListOfGroup.get(index);
                if (index < usedBucketRemainNum) {
                    destUsedBucketNoListOfGroup.add(currBucketNo);
                } else {
                    destUnusedBucketNoListOfLayer.add(currBucketNo);
                }
            }
            //填充计算结果
            result.setBucketRatio(request.getDestBucketRatio());
            result.setUnusedBucketNoListOfLayer(destUnusedBucketNoListOfLayer);
            result.setBucketNoListOfGroup(destUsedBucketNoListOfGroup);
        }
        return result;
    }

    /**
     * 将桶编号列表进行Base64压缩
     *
     * @param bucketNos
     * @return
     */
    public static String bucketsToBitStr(Iterable<Integer> bucketNos) {
        BitSet bitSet = new BitSet(BUCKET_TOTAL_NUM);
        bucketNos.forEach(bitSet::set);
        return Base64.getUrlEncoder().encodeToString(bitSet.toByteArray());
    }

    /**
     * 将经过Base64压缩的分桶数据解压成Set<Integer>类型
     *
     * @param str
     * @return
     */
    public static Set<Integer> bitStr2buckets(String str) {
        BitSet bitSet = BitSet.valueOf(Base64.getUrlDecoder().decode(str));
        return bitSet.stream().boxed().collect(Collectors.toSet());
    }
}
