package com.wudimanong.experiment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wudimanong.experiment.client.entity.AbtestExpInfo;
import com.wudimanong.experiment.client.entity.BusinessCodeEnum;
import com.wudimanong.experiment.client.entity.bo.CreateExpBO;
import com.wudimanong.experiment.client.entity.bo.GetExpInfosBO;
import com.wudimanong.experiment.client.entity.bo.GroupFlowRatioBO;
import com.wudimanong.experiment.client.entity.bo.UpdateFlowRatioBO;
import com.wudimanong.experiment.client.entity.dto.CreateExpDTO;
import com.wudimanong.experiment.client.entity.dto.GetExpInfosDTO;
import com.wudimanong.experiment.client.entity.dto.UpdateFlowRatioDTO;
import com.wudimanong.experiment.convert.AbtestExpConvert;
import com.wudimanong.experiment.dao.mapper.AbtestExpInfoDao;
import com.wudimanong.experiment.dao.mapper.AbtestGroupDao;
import com.wudimanong.experiment.dao.mapper.AbtestLayerDao;
import com.wudimanong.experiment.dao.model.AbtestExpInfoPO;
import com.wudimanong.experiment.dao.model.AbtestGroupPO;
import com.wudimanong.experiment.dao.model.AbtestLayerPO;
import com.wudimanong.experiment.exception.ServiceException;
import com.wudimanong.experiment.service.AbtestExpService;
import com.wudimanong.experiment.utils.BucketAllocate.Request;
import com.wudimanong.experiment.utils.BucketAllocate.Result;
import com.wudimanong.experiment.utils.BucketUtils;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jiangqiao
 */
@Service
@Slf4j
public class AbtestExpServiceImpl implements AbtestExpService {

    /**
     * 依赖注入实验信息表持久层依赖
     */
    @Autowired
    AbtestExpInfoDao abtestExpInfoDao;

    /**
     * 依赖注入实验分层信息表持久层依赖
     */
    @Autowired
    AbtestLayerDao abtestLayerDao;

    /**
     * 依赖注入实验分组持久层依赖
     */
    @Autowired
    AbtestGroupDao abtestGroupDao;

    /**
     * 实验信息列表分页查询逻辑
     *
     * @param getExpInfosDTO
     * @return
     */
    @Override
    public GetExpInfosBO getExpInfos(GetExpInfosDTO getExpInfosDTO) {
        //基于MybatisPlus语法支持拼装查询条件
        QueryWrapper<AbtestExpInfoPO> queryWrapper = new QueryWrapper<>();
        if (getExpInfosDTO.getNameLike() != null && !"".equals(getExpInfosDTO.getNameLike())) {
            queryWrapper.like("name", getExpInfosDTO.getNameLike());
        }
        if (getExpInfosDTO.getFactorTag() != null && !"".equals(getExpInfosDTO.getFactorTag())) {
            queryWrapper.eq("factor_tag", getExpInfosDTO.getFactorTag());
        }
        if (getExpInfosDTO.getStatus() != null) {
            queryWrapper.eq("status", getExpInfosDTO.getStatus());
        }
        //过滤已逻辑删除数据
        queryWrapper.eq("is_delete", 0);
        //进行逆序排序操作
        queryWrapper.orderByDesc("id");
        //MybatisPlus分页支持(设置页码及每页记录数据)
        Page<AbtestExpInfoPO> page = new Page<>(getExpInfosDTO.getPageNo(), getExpInfosDTO.getPageSize());
        //执行分页查询，返回分页查询结果
        IPage<AbtestExpInfoPO> resultPage = abtestExpInfoDao.selectPage(page, queryWrapper);
        //获取具体分页数据
        List<AbtestExpInfoPO> abtestExpInfoPOList = resultPage.getRecords();
        //通过MapStruct数据拷贝插件将持久层列表对象转换为服务层输出对象
        List<AbtestExpInfo> abtestExpInfoList = AbtestExpConvert.INSTANCE.convertAbtestInfo(abtestExpInfoPOList);
        //构造返回输出对象
        GetExpInfosBO getExpInfosBO = GetExpInfosBO.builder().total(Long.valueOf(resultPage.getTotal()).intValue())
                .pageNo(Long.valueOf(resultPage.getCurrent()).intValue()).list(abtestExpInfoList).build();
        return getExpInfosBO;
    }

    /**
     * 实验创建业务实现方法（关键核心方法）
     *
     * @param createExpDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CreateExpBO createExp(CreateExpDTO createExpDTO) {
        //1）、验证实验是否已存在
        QueryWrapper<AbtestExpInfoPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("factor_tag", createExpDTO.getFactorTag());
        AbtestExpInfoPO abtestExpInfoPO = abtestExpInfoDao.selectOne(queryWrapper);
        if (abtestExpInfoPO != null) {
            throw new ServiceException(BusinessCodeEnum.BUSI_LOGICAL_FAIL_2000.getCode(),
                    BusinessCodeEnum.BUSI_LOGICAL_FAIL_2000.getDesc());
        }
        //2）、判断分层信息是否存在，不存在则创建默认流量分层
        AbtestLayerPO abtestLayerPO = null;
        if (createExpDTO.getLayerId() != null) {
            //流量层不存在则抛出异常返回失败
            if (!isExistLayer(createExpDTO.getLayerId())) {
                throw new ServiceException(BusinessCodeEnum.BUSI_LOGICAL_LAYER_IS_NOT_EXIST.getCode(),
                        BusinessCodeEnum.BUSI_LOGICAL_LAYER_IS_NOT_EXIST.getDesc());
            }
        } else {
            //否则创建默认分层
            abtestLayerPO = createAbtestLayer(createExpDTO);
            //持久化分层信息
            abtestLayerDao.insert(abtestLayerPO);
        }
        //3）、生成实验基本信息
        abtestExpInfoPO = createAbtestInfo(createExpDTO, abtestLayerPO);
        //持久化实验基本信息
        abtestExpInfoDao.insert(abtestExpInfoPO);
        //4）、创建实验分组信息并持久化
        List<AbtestGroupPO> groupInfos = createAbtestGroupList(abtestExpInfoPO);
        //批量持久化分组信息
        abtestGroupDao.batchInsert(groupInfos);
        //5）、初始流量占比流量桶计算及更新
        //筛选分配分配占比超过0的分组
        Map<Integer, Integer> flowRatioMap = groupInfos.stream().filter(o -> o.getFlowRatio() > 0)
                .collect(Collectors.toMap(AbtestGroupPO::getId, AbtestGroupPO::getFlowRatio));
        if (flowRatioMap.size() > 0) {
            //调用方法进行流量桶分配(调用之前将GroupList中的流量占比设置为0，确保正常初始分配)
            updateFlowRatio(abtestExpInfoPO, abtestLayerPO,
                    groupInfos.stream().peek(group -> group.setFlowRatio(0)).collect(
                            Collectors.toList()), flowRatioMap);
        }
        return CreateExpBO.builder().isSuccess(true).expId(abtestExpInfoPO.getId()).build();
    }

    /**
     * 修改实验流量占比
     *
     * @param updateFlowRatioDTO
     * @return
     */
    @Override
    public UpdateFlowRatioBO updateFlowRatio(UpdateFlowRatioDTO updateFlowRatioDTO) {
        //获取实验基本信息
        QueryWrapper<AbtestExpInfoPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("factor_tag", updateFlowRatioDTO.getFactorTag());
        queryWrapper.eq("service_name", updateFlowRatioDTO.getAppName());
        AbtestExpInfoPO abtestExpInfoPO = abtestExpInfoDao.selectOne(queryWrapper);
        if (abtestExpInfoPO == null) {
            throw new ServiceException(BusinessCodeEnum.BUSI_LOGICAL_EXP_IS_NOT_EXIST.getCode(),
                    BusinessCodeEnum.BUSI_LOGICAL_EXP_IS_NOT_EXIST.getDesc());
        }
        //获取实验分层信息
        AbtestLayerPO abtestLayerPO = abtestLayerDao.selectById(abtestExpInfoPO.getLayerId());
        if (abtestLayerPO == null) {
            throw new ServiceException(BusinessCodeEnum.BUSI_LOGICAL_LAYER_IS_NOT_EXIST.getCode(),
                    BusinessCodeEnum.BUSI_LOGICAL_LAYER_IS_NOT_EXIST.getDesc());
        }
        //获取实验分组信息
        Map<String, Object> param = new HashMap<>();
        param.put("exp_id", abtestExpInfoPO.getId());
        List<AbtestGroupPO> groupList = abtestGroupDao.selectByMap(param);
        //筛选需要调整的分组(目标分组流量与当前分组流量占比不一致的才需要调整)
        List<AbtestGroupPO> oldGroupList = groupList.stream().filter(group -> !Objects.equals(group.getFlowRatio(),
                updateFlowRatioDTO.getFlowRatioParam().getOrDefault(group.getId(), 0)))
                .collect(Collectors.toList());
        //调用公共流量调整方法进行流量调节（与实验创建共用）
        updateFlowRatio(abtestExpInfoPO, abtestLayerPO, oldGroupList, updateFlowRatioDTO.getFlowRatioParam());
        //封装流量调节后的结果数据
        List<GroupFlowRatioBO> groupFlowRatioResult = groupList.stream()
                .map(group -> GroupFlowRatioBO.builder().groupId(group.getId()).groupType(group.getGroupType())
                        .preFlowRatio(group.getFlowRatio())
                        .flowRatio(updateFlowRatioDTO.getFlowRatioParam().get(group.getId())).build())
                .collect(Collectors.toList());
        //封装返回参数对象
        UpdateFlowRatioBO updateFlowRatioBO = UpdateFlowRatioBO.builder().appName(updateFlowRatioDTO.getAppName())
                .factorTag(updateFlowRatioDTO.getFactorTag()).flowRatio(groupFlowRatioResult).build();
        return updateFlowRatioBO;
    }

    /**
     * 判断是否存在分层信息
     *
     * @param layerId
     * @return
     */
    private Boolean isExistLayer(Integer layerId) {
        AbtestLayerPO abtestLayerPO = abtestLayerDao.selectById(layerId);
        if (abtestLayerPO != null) {
            return true;
        }
        return false;
    }

    /**
     * 生成实验分层持久层对象方法
     *
     * @param createExpDTO
     * @return
     */
    private AbtestLayerPO createAbtestLayer(CreateExpDTO createExpDTO) {
        AbtestLayerPO abtestLayerPO = new AbtestLayerPO();
        //设置名称描述信息
        abtestLayerPO.setName(createExpDTO.getFactorTag());
        abtestLayerPO.setDesc(createExpDTO.getDesc());
        //设置流量分组类型ID
        abtestLayerPO.setGroupFieldId(createExpDTO.getGroupField());
        //初始化流量分桶（这里是最核心的逻辑,通过编写BucketUtils工具类实现）
        //设置每个分层的默认分桶总数
        abtestLayerPO.setBucketTotalNum(BucketUtils.BUCKET_TOTAL_NUM);
        //通过工具方法实现流量分桶号初始化
        abtestLayerPO
                .setUnusedBucketNos(StringUtils.join(BucketUtils.getShuffledBucketNoList().stream().toArray(), ","));
        abtestLayerPO.setIsDelete(0);
        return abtestLayerPO;
    }

    /**
     * 生成实验基本信息持久层对象方法
     *
     * @param createExpDTO
     * @return
     */
    private AbtestExpInfoPO createAbtestInfo(CreateExpDTO createExpDTO, AbtestLayerPO abtestLayerPO) {
        AbtestExpInfoPO abtestExpInfoPO = new AbtestExpInfoPO();
        abtestExpInfoPO.setName(createExpDTO.getDesc());
        abtestExpInfoPO.setFactorTag(createExpDTO.getFactorTag());
        //设置分层信息
        abtestExpInfoPO
                .setLayerId(createExpDTO.getLayerId() == null ? abtestLayerPO.getId() : createExpDTO.getLayerId());
        abtestExpInfoPO.setGroupFieldId(createExpDTO.getGroupField());
        //默认设置抽样
        abtestExpInfoPO.setIsSampling(1);
        //默认设置抽样率为5
        abtestExpInfoPO.setSamplingRatio(5);
        //设置指标等信息(此处主要处于对实验的具体管理需要)
        abtestExpInfoPO.setMetricIds("");
        abtestExpInfoPO.setOwner(createExpDTO.getOwner());
        abtestExpInfoPO.setServiceName(createExpDTO.getAppName());
        //默认设置为已发布状态
        abtestExpInfoPO.setStatus(1);
        abtestExpInfoPO.setIsDelete(0);
        //设置为未上线
        abtestExpInfoPO.setOnline(0);
        return abtestExpInfoPO;
    }

    /**
     * 默认初始流量占比
     */
    public static final Integer defaultGroupInitFlowRatio = 50;

    /**
     * 生成实验分组信息持久层列表对象方法
     *
     * @param abtestExpInfoPO
     * @return
     */

    private List<AbtestGroupPO> createAbtestGroupList(AbtestExpInfoPO abtestExpInfoPO) {
        //生成流量分组信息
        List<AbtestGroupPO> groupInfos = new ArrayList<>();
        //1）、生成实验组
        AbtestGroupPO testGroup = new AbtestGroupPO();
        testGroup.setExpId(abtestExpInfoPO.getId());
        //设置流量占比
        testGroup.setFlowRatio(defaultGroupInitFlowRatio);
        //设置分组名称
        testGroup.setName("实验组");
        //分组类型为0-表示实验组
        testGroup.setGroupType(0);
        //设置默认分流内包含的分桶编号
        testGroup.setGroupPartitionDetails("");
        //设置策略明细
        testGroup.setStrategyDetail("");
        testGroup.setOnline(abtestExpInfoPO.getOnline());
        testGroup.setDilutionRatio(0);
        groupInfos.add(testGroup);
        //2）、生成对照组
        AbtestGroupPO controlGroup = new AbtestGroupPO();
        controlGroup.setExpId(abtestExpInfoPO.getId());
        //设置流量占比
        controlGroup.setFlowRatio(defaultGroupInitFlowRatio);
        //设置分组名称
        controlGroup.setName("对照组");
        //分组类型为1-表示对照组
        controlGroup.setGroupType(1);
        //设置默认分流内包含的分桶编号
        controlGroup.setGroupPartitionDetails("");
        //设置策略明细
        controlGroup.setStrategyDetail("");
        controlGroup.setOnline(abtestExpInfoPO.getOnline());
        controlGroup.setDilutionRatio(0);
        groupInfos.add(0, controlGroup);
        return groupInfos;
    }

    /**
     * 更新流量分桶(关于流量桶分配最核心的公共方法，它的算法是整个Abtest系统的核心)
     *
     * @param expInfo
     * @param layer
     * @param groupList
     * @param flowRatioMap
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateFlowRatio(AbtestExpInfoPO expInfo, AbtestLayerPO layer, List<AbtestGroupPO> groupList,
            Map<Integer, Integer> flowRatioMap) {
        //获取需要进行流量分配的分组信息
        try {
            groupList.stream().filter(group -> flowRatioMap.containsKey(group.getId())).sorted(
                    Comparator.comparing(group -> flowRatioMap.get(group.getId()) - group.getFlowRatio()))
                    .map(group -> (Function<List<Integer>, List<Integer>>) unused -> {
                        Result bucketResult;
                        try {
                            Request bucketRequest = new Request();
                            bucketRequest.setCurrBucketRatio(group.getFlowRatio());
                            bucketRequest.setDestBucketRatio(flowRatioMap.get(group.getId()));
                            bucketRequest.setCurrUnusedBucketNoListOfLayer(unused);
                            //转换String分桶数据为List<Integer>
                            List<Integer> currUsedBucketNoListOfGroup = (group.getGroupPartitionDetails() != null && !""
                                    .equals(group.getGroupPartitionDetails())) ? Arrays
                                    .asList(group.getGroupPartitionDetails().split(",")).stream()
                                    .map(o -> Integer.valueOf(o)).collect(Collectors.toList()) : new ArrayList<>();
                            bucketRequest.setCurrUsedBucketNoListOfGroup(currUsedBucketNoListOfGroup);
                            //执行重新分桶洗牌逻辑
                            bucketResult = BucketUtils.bucketReallocate(bucketRequest);
                            //设置已分配好的分桶编号
                            group.setGroupPartitionDetails(
                                    StringUtils.join(bucketResult.getBucketNoListOfGroup(), ","));
                            //更新当前时间
                            group.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                            //更新流量占比
                            group.setFlowRatio(flowRatioMap.get(group.getId()));
                            abtestGroupDao.updateById(group);
                        } catch (Exception e) {
                            throw new ServiceException(BusinessCodeEnum.BUSI_LOGICAL_OVER_AVAILABLE_FLOW.getCode(),
                                    BusinessCodeEnum.BUSI_LOGICAL_OVER_AVAILABLE_FLOW.getDesc(), e);
                        }
                        //向上返回分层中，当前未分配的分桶编号
                        return bucketResult.getUnusedBucketNoListOfLayer();
                    }).reduce(Function::andThen)
                    .ifPresent(func -> {
                        //更新分层信息中的未使用分桶编号信息
                        List<Integer> layerUnusedBucketNos =
                                (layer.getUnusedBucketNos() != null && !"".equals(layer.getUnusedBucketNos())) ? Arrays
                                        .asList(layer.getUnusedBucketNos().split(","))
                                        .stream().map(o -> Integer.valueOf(o)).collect(Collectors.toList())
                                        : new ArrayList<>();
                        String unused = StringUtils.join(func.apply(layerUnusedBucketNos).toArray(), ",");
                        layer.setUnusedBucketNos(unused);
                        layer.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                        abtestLayerDao.updateById(layer);
                    });
        } catch (Exception e) {
            throw new ServiceException(BusinessCodeEnum.BUSI_LOGICAL_OVER_AVAILABLE_FLOW.getCode(),
                    BusinessCodeEnum.BUSI_LOGICAL_OVER_AVAILABLE_FLOW.getDesc(), e);
        }
    }
}
