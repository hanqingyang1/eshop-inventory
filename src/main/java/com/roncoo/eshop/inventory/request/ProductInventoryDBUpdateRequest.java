package com.roncoo.eshop.inventory.request;

import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;

/**
 * 数据更新请求
 */
public class ProductInventoryDBUpdateRequest implements Request{

    private ProductInventory productInventory;

    private ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService){
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {

        System.out.println("==========日志========= 数据库更新请求开始工作  商品id "+ productInventory.getProductId()+" 商品数量 "+productInventory.getInventoryCnt());

        //先删除缓存
        productInventoryService.removeProductInventoryCache(productInventory);
        System.out.println("============日志========== 已删除redis 中的缓存");

        try {
            //模拟其他请求进来
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //更新库存
        productInventoryService.updateProductInventory(productInventory);
        System.out.println("============日志=========== 已更新 数据商品库存 商品id "+productInventory.getProductId()+"商品数量 "+productInventory.getInventoryCnt());


    }

    public Integer getProductId(){
        return productInventory.getProductId();
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }
}
