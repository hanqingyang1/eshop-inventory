package com.roncoo.eshop.inventory.service.impl;

import com.alibaba.fastjson.JSON;
import com.roncoo.eshop.inventory.dao.RedisDAO;
import com.roncoo.eshop.inventory.mapper.ProductInventoryMapper;
import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {


    @Autowired
    ProductInventoryMapper productInventoryMapper;
    @Autowired
    RedisDAO redisDAO;

    private static final String PRIFIX = "product:inventory:";
    /**
     * 更新商品库存
     * @param productInventory
     */
    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
    }

    /**
     * 删除redis商品库存
     * @param productInventory
     */
    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {

        String key = PRIFIX + productInventory.getProductId();
        redisDAO.delete(key);
    }

    /**
     * 根据商品id 查询商品库存
     * @param productId
     * @return
     */
    @Override
    public ProductInventory findProductInvent(Integer productId) {
        ProductInventory productInventory = productInventoryMapper.findProductInventory(productId);
        return productInventory;
    }

    /**
     * 将商品库存信息保存到redis
     * @param productInventory
     */
    @Override
    public void addProductInventoryCache(ProductInventory productInventory) {
        String key = PRIFIX + productInventory.getProductId();
//        String jsonString = JSON.toJSONString(productInventory);
        redisDAO.set(key,String.valueOf(productInventory.getInventoryCnt()));
        System.out.println("==========日志=========  已更新商品库存的缓存  商品id "+productInventory.getProductId()+"商品数量 "+productInventory.getInventoryCnt());

    }

    /**
     * 从缓存中查询商品库存
     * @param productId
     * @return
     */
    @Override
    public ProductInventory getProductInventoryCache(Integer productId) {
        Long inventoryCnt = 0L;
        String key = PRIFIX+productId;

        String s = redisDAO.get(key);
//        ProductInventory productInventory = null;
        if(!StringUtils.isEmpty(s)){
            try {
                inventoryCnt = Long.valueOf(s);
                return new ProductInventory(productId,inventoryCnt);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


}
