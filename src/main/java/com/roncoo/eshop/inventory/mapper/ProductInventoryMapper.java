package com.roncoo.eshop.inventory.mapper;

import com.roncoo.eshop.inventory.model.ProductInventory;
import org.apache.ibatis.annotations.Param;

public interface ProductInventoryMapper {

    /**
     * 更新商品库存
     * @param productInventory
     */
    public void updateProductInventory(ProductInventory productInventory);

    /**
     * 根据商品id 查询商品库存信息
     * @param productId
     * @return
     */
    ProductInventory findProductInventory(@Param("productId") Integer productId);
}
