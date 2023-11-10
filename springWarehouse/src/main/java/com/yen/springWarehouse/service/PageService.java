package com.yen.springWarehouse.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.util.BaseQueryHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PageService<T> {

    public Page<T> getPage(BaseQueryHelper helper, BaseMapper baseMapper, Integer pageNo, Integer pageSize){

        Page<T> page = new Page<>(pageNo,pageSize);
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        IPage elementList = baseMapper.selectPage(page, wrapper);

        if(elementList.getTotal() > 0){
            page.setRecords((List<T>) elementList);
            return page;
        }

        return new Page<>();
    }

}
