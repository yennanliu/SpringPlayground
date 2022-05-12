package com.jsh.erp.service.materialAttribute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.datasource.entities.MaterialAttribute;
import com.jsh.erp.datasource.entities.MaterialAttributeExample;
import com.jsh.erp.datasource.mappers.MaterialAttributeMapper;
import com.jsh.erp.datasource.mappers.MaterialAttributeMapperEx;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.exception.JshException;
import com.jsh.erp.service.log.LogService;
import com.jsh.erp.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaterialAttributeService {
    private Logger logger = LoggerFactory.getLogger(MaterialAttributeService.class);

    @Resource
    private LogService logService;

    @Resource
    private MaterialAttributeMapper materialAttributeMapper;

    @Resource
    private MaterialAttributeMapperEx materialAttributeMapperEx;

    public MaterialAttribute getMaterialAttribute(long id)throws Exception {
        MaterialAttribute result=null;
        try{
            result=materialAttributeMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<MaterialAttribute> getMaterialAttribute() throws Exception{
        MaterialAttributeExample example = new MaterialAttributeExample();
        example.createCriteria().andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialAttribute> list=null;
        try{
            list=materialAttributeMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public List<MaterialAttribute> select(String attributeField, int offset, int rows)
            throws Exception{
        String[] arr = {"manyColor","manySize","other1","other2","other3"};
        Map<String, String> map = new HashMap<>();
        map.put("manyColor", "多颜色");
        map.put("manySize", "多尺寸");
        map.put("other1", "自定义1");
        map.put("other2", "自定义2");
        map.put("other3", "自定义3");
        List<MaterialAttribute> list = new ArrayList<>();
        try{
            List<MaterialAttribute> maList = materialAttributeMapperEx.selectByConditionMaterialAttribute(attributeField, offset, rows);
            for(String field: arr) {
                MaterialAttribute materialAttribute = new MaterialAttribute();
                materialAttribute.setAttributeField(field);
                materialAttribute.setAttributeName(map.get(field));
                for(MaterialAttribute ma: maList) {
                    if(field.equals(ma.getAttributeField())){
                        materialAttribute.setId(ma.getId());
                        materialAttribute.setAttributeName(ma.getAttributeName());
                        materialAttribute.setAttributeValue(ma.getAttributeValue());
                    }
                }
                list.add(materialAttribute);
            }
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list;
    }

    public Long countMaterialAttribute(String attributeField)throws Exception {
        Long result =null;
        try{
            result= 5L;
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertMaterialAttribute(JSONObject obj, HttpServletRequest request)throws Exception {
        MaterialAttribute m = JSONObject.parseObject(obj.toJSONString(), MaterialAttribute.class);
        try{
            materialAttributeMapper.insertSelective(m);
            logService.insertLog("商品属性",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_ADD).append(m.getAttributeName()).toString(), request);
            return 1;
        }
        catch (BusinessRunTimeException ex) {
            throw new BusinessRunTimeException(ex.getCode(), ex.getMessage());
        }
        catch(Exception e){
            JshException.writeFail(logger, e);
            return 0;
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateMaterialAttribute(JSONObject obj, HttpServletRequest request) throws Exception{
        MaterialAttribute materialAttribute = JSONObject.parseObject(obj.toJSONString(), MaterialAttribute.class);
        try{
            materialAttributeMapper.updateByPrimaryKeySelective(materialAttribute);
            logService.insertLog("商品属性",
                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(materialAttribute.getAttributeName()).toString(), request);
            return 1;
        }catch(Exception e){
            JshException.writeFail(logger, e);
            return 0;
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteMaterialAttribute(Long id, HttpServletRequest request)throws Exception {
        return batchDeleteMaterialAttributeByIds(id.toString());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterialAttribute(String ids, HttpServletRequest request)throws Exception {
        return batchDeleteMaterialAttributeByIds(ids);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteMaterialAttributeByIds(String ids) throws Exception{
        String [] idArray=ids.split(",");
        try{
            return materialAttributeMapperEx.batchDeleteMaterialAttributeByIds(idArray);
        }catch(Exception e){
            JshException.writeFail(logger, e);
            return 0;
        }
    }

    public int checkIsNameExist(Long id, String name)throws Exception {
        MaterialAttributeExample example = new MaterialAttributeExample();
        example.createCriteria().andIdNotEqualTo(id).andAttributeNameEqualTo(name).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialAttribute> list =null;
        try{
            list = materialAttributeMapper.selectByExample(example);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return list==null?0:list.size();
    }

    public JSONObject getAll() {
        JSONObject obj = new JSONObject();
        //属性名
        obj.put("manyColorName", getNameByField("manyColor"));
        obj.put("manySizeName", getNameByField("manySize"));
        obj.put("other1Name", getNameByField("other1"));
        obj.put("other2Name", getNameByField("other2"));
        obj.put("other3Name", getNameByField("other3"));
        //属性值
        obj.put("manyColorValue", getValueArrByField("manyColor"));
        obj.put("manySizeValue", getValueArrByField("manySize"));
        obj.put("other1Value", getValueArrByField("other1"));
        obj.put("other2Value", getValueArrByField("other2"));
        obj.put("other3Value", getValueArrByField("other3"));
        return obj;
    }

    public MaterialAttribute getInfoByField(String field) {
        MaterialAttributeExample example = new MaterialAttributeExample();
        example.createCriteria().andAttributeFieldEqualTo(field).andDeleteFlagNotEqualTo(BusinessConstants.DELETE_FLAG_DELETED);
        List<MaterialAttribute> list = materialAttributeMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public String getNameByField(String field) {
        String res = "";
        if("manyColor".equals(field)){
            res = "多颜色";
        } else if("manySize".equals(field)){
            res = "多尺寸";
        } else if("other1".equals(field)){
            res = "自定义1";
        } else if("other2".equals(field)){
            res = "自定义2";
        } else if("other3".equals(field)){
            res = "自定义3";
        }
        MaterialAttribute ma = getInfoByField(field);
        if(ma!=null && StringUtil.isNotEmpty(ma.getAttributeName())) {
            res = ma.getAttributeName();
        }
        return res;
    }

    public JSONArray getValueArrByField(String field) {
        JSONArray valueArr = new JSONArray();
        MaterialAttribute ma = getInfoByField(field);
        if(ma!=null) {
            String value = ma.getAttributeValue();
            if(StringUtil.isNotEmpty(value)){
                String[] arr = value.split("\\|");
                for(String v: arr) {
                    JSONObject item = new JSONObject();
                    item.put("value",v);
                    item.put("name",v);
                    valueArr.add(item);
                }
            }
        }
        return valueArr;
    }
}
