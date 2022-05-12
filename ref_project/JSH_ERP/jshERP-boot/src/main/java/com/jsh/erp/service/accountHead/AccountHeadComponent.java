package com.jsh.erp.service.accountHead;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.utils.Constants;
import com.jsh.erp.utils.QueryUtils;
import com.jsh.erp.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "accountHead_component")
@AccountHeadResource
public class AccountHeadComponent implements ICommonQuery {

    @Resource
    private AccountHeadService accountHeadService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return accountHeadService.getAccountHead(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getAccountHeadList(map);
    }

    private List<?> getAccountHeadList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String type = StringUtil.getInfo(search, "type");
        String roleType = StringUtil.getInfo(search, "roleType");
        String billNo = StringUtil.getInfo(search, "billNo");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        Long organId = StringUtil.parseStrLong(StringUtil.getInfo(search, "organId"));
        Long creator = StringUtil.parseStrLong(StringUtil.getInfo(search, "creator"));
        Long handsPersonId = StringUtil.parseStrLong(StringUtil.getInfo(search, "handsPersonId"));
        Long accountId = StringUtil.parseStrLong(StringUtil.getInfo(search, "accountId"));
        String status = StringUtil.getInfo(search, "status");
        String remark = StringUtil.getInfo(search, "remark");
        return accountHeadService.select(type, roleType, billNo, beginTime, endTime, organId, creator, handsPersonId,
                accountId, status, remark, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String type = StringUtil.getInfo(search, "type");
        String roleType = StringUtil.getInfo(search, "roleType");
        String billNo = StringUtil.getInfo(search, "billNo");
        String beginTime = StringUtil.getInfo(search, "beginTime");
        String endTime = StringUtil.getInfo(search, "endTime");
        Long organId = StringUtil.parseStrLong(StringUtil.getInfo(search, "organId"));
        Long creator = StringUtil.parseStrLong(StringUtil.getInfo(search, "creator"));
        Long handsPersonId = StringUtil.parseStrLong(StringUtil.getInfo(search, "handsPersonId"));
        Long accountId = StringUtil.parseStrLong(StringUtil.getInfo(search, "accountId"));
        String status = StringUtil.getInfo(search, "status");
        String remark = StringUtil.getInfo(search, "remark");
        return accountHeadService.countAccountHead(type, roleType, billNo, beginTime, endTime, organId, creator, handsPersonId,
                accountId, status, remark);
    }

    @Override
    public int insert(JSONObject obj, HttpServletRequest request) throws Exception{
        return accountHeadService.insertAccountHead(obj, request);
    }

    @Override
    public int update(JSONObject obj, HttpServletRequest request)throws Exception {
        return accountHeadService.updateAccountHead(obj, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return accountHeadService.deleteAccountHead(id, request);
    }

    @Override
    public int deleteBatch(String ids, HttpServletRequest request)throws Exception {
        return accountHeadService.batchDeleteAccountHead(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return accountHeadService.checkIsNameExist(id, name);
    }

}
