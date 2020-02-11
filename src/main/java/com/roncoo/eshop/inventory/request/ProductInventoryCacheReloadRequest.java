package com.roncoo.eshop.inventory.request;

import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;

/**
 * 重新加载商品库存的缓存
 */
public class ProductInventoryCacheReloadRequest  implements Request{

    private Integer productId;

    private boolean forceRefresh;

    private ProductInventoryService productInventoryService;

    public ProductInventoryCacheReloadRequest(Integer productId,ProductInventoryService productInventoryService,boolean forceRefresh){
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process() {
        //数据库查询商品库存
        ProductInventory productInvent = productInventoryService.findProductInvent(productId);
        System.out.println("==========日志==========  已查询到商品库存数量 id "+productId+"商品数量 "+productInvent.getInventoryCnt());

        //将商品库存刷到Redis中
        productInventoryService.addProductInventoryCache(productInvent);
    }

    public Integer getProductId(){
        return productId;
    }

    public boolean isForceRefresh() {
        return forceRefresh;
    }
}
