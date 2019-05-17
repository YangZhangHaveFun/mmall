package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    public ServerResponse saveOrUpdateProduct(Product product){
        if (product != null){
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0){
                    return ServerResponse.createBySuccessMessage("Update product successfully!");
                }
                return ServerResponse.createByErrorMessage("Update failed");
            }else{
                int rowCount = productMapper.insert(product);
                if (rowCount > 0){
                    return ServerResponse.createBySuccessMessage("Add a new product successfully!");
                }
                return ServerResponse.createByErrorMessage("Adding a new product failed");
            }

        }
        return ServerResponse.createByErrorMessage("Adding or updating product failed.");
    }

}
