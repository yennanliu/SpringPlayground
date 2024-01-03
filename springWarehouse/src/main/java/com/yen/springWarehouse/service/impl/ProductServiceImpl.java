package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springWarehouse.bean.Merchant;
import com.yen.springWarehouse.bean.Product;
import com.yen.springWarehouse.bean.ProductType;
import com.yen.springWarehouse.dto.ProductDto;
import com.yen.springWarehouse.mapper.ProductMapper;
import com.yen.springWarehouse.mapper.ProductTypeMapper;
import com.yen.springWarehouse.service.MerchantService;
import com.yen.springWarehouse.service.ProductService;
import com.yen.springWarehouse.service.ProductTypeService;
import com.yen.springWarehouse.util.ProductQueryHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    MerchantService merchantService;

    @Autowired
    ProductTypeService productTypeService;

    @Override
    public Page<Product> getProductPage(ProductQueryHelper helper, Integer pageNo, Integer pageSize) {

        Page<Product> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Product> productWrapper = new QueryWrapper<>();

        if(StringUtils.isNotEmpty(helper.getQryProductName())){
            productWrapper.like("product_name", helper.getQryProductName());
        }

        if(helper.getQryStartPrice()!=null){
            productWrapper.lambda().ge(Product::getPrice, helper.getQryStartPrice());
        }

        if(helper.getQryEndPrice()!=null){
            productWrapper.lambda().le(Product::getPrice, helper.getQryEndPrice());
        }

        if(StringUtils.isNotEmpty(helper.getQryProductType())){
            productWrapper.like("type_id", Integer.parseInt(helper.getQryProductType()));
        }

        List<Product> productList = baseMapper.getProductList(page, productWrapper);

        // TODO : fix below
//        productList.forEach(_prod ->
//                //product.setproductType(productTypeMapper.selectById(product.getTypeId()))
//                _prod.setTypeId(productTypeMapper.selectById(_prod.getTypeId()))
//        );

        if(CollectionUtils.isNotEmpty(productList)){
            //List<ProductDto> productDtoList = getProductDtoListFromProductList(productList);
            page.setRecords(productList);
            return page;
        }

        return new Page<>();

    }

    @Override
    public List<ProductDto> getProductDtoListFromProductList(List<Product> productList) {

        List<ProductDto> productDtoList = productList.stream().map(
                product -> {
                    ProductDto productDto = new ProductDto();
                    Merchant merchant = merchantService.getById(product.getMerchantId());
                    ProductType productType = productTypeService.getById(product.getTypeId());
                    BeanUtils.copyProperties(productDto, product);
                    productDto.setMerchantName(merchant.getName());
                    productDto.setProductTypeName(productType.getTypeName());
                    return productDto;
                }
        ).collect(Collectors.toList());

        return productDtoList;
    }

    @Override
    public void deduct(Integer id) {

        /** V1 */
        System.out.println(">>> id = " + id);
        Product product = productMapper.selectById(id);
        System.out.println(">>> product = " + product);
        int amount = product.getAmount();

//        product.setAmount(amount-1);
//        UpdateWrapper<Product> wrapper = new UpdateWrapper<>(product);
//        // TODO : check why productMapper CAN'T update with UpdateWrapper
//        //productMapper.update(product, wrapper); //baseMapper.update(product, wrapper);
//        productMapper.updateById(product);

        /** V2 : use Mysql lock deal with cluster deployment */

        productMapper.updateProductCount(product.getId(), 1);
        System.out.println(">>> product = " + product);
    }

}
