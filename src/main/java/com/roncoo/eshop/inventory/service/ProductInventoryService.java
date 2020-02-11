package com.roncoo.eshop.inventory.service;

import com.roncoo.eshop.inventory.model.ProductInventory;

public interface ProductInventoryService {

    void updateProductInventory(ProductInventory productInventory);

    void removeProductInventoryCache(ProductInventory productInventory);

    ProductInventory findProductInvent(Integer productId);

    void addProductInventoryCache(ProductInventory productInventory);

    ProductInventory getProductInventoryCache(Integer productId);
}
