package com.roncoo.eshop.inventory.controller;

import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.request.ProductInventoryCacheReloadRequest;
import com.roncoo.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.roncoo.eshop.inventory.request.Request;
import com.roncoo.eshop.inventory.service.ProductInventoryService;
import com.roncoo.eshop.inventory.service.RequestAsyncProcessService;
import com.roncoo.eshop.inventory.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
public class ProductInventoryController {

    @Autowired
    ProductInventoryService productInventoryService;

    @Autowired
    RequestAsyncProcessService requestAsyncProcessService;


    /**
     * 更新商品库存
     * @param productInventory
     * @return
     */
    @RequestMapping("/updateProductInventory")
    public Response updateProductInventory(ProductInventory productInventory){

        // 为了简单起见，我们就不用log4j那种日志框架去打印日志了
        // 其实log4j也很简单，实际企业中都是用log4j去打印日志的，自己百度一下
        System.out.println("===========日志===========: 接收到更新商品库存的请求，商品id=" + productInventory.getProductId() + ", 商品库存数量=" + productInventory.getInventoryCnt());

        Response response = null;

        try {
            Request request = new ProductInventoryDBUpdateRequest(
                    productInventory, productInventoryService);
            requestAsyncProcessService.process(request);
            response = new Response(Response.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(Response.FAILURE);
        }

        return response;
    }


    @GetMapping("/getProductInventory")
    public ProductInventory getProductInventory(Integer productId){

        System.out.println("=================日志============= 接收到一个商品库存的 读请求 商品id "+productId);
        ProductInventory productInventory = null;
        try {
            Request request = new ProductInventoryCacheReloadRequest(productId,productInventoryService,false);
            requestAsyncProcessService.process(request);

            Long startTime = System.currentTimeMillis();
            Long endTime = 0L;
            Long waitTime = 0L;
            while (true){

                if(waitTime > 200){
                    break;
                }

                //从缓存中读取商品库存
                productInventory = productInventoryService.getProductInventoryCache(productId);

                if(productInventory != null){
                    System.out.println("=========日志======== 在 200ms 内存读取到了redis 缓存中的数据");
                    return productInventory;
                }else {
                    TimeUnit.MILLISECONDS.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime -startTime;
                }
            }

            ProductInventory productInvent = productInventoryService.findProductInvent(productId);
            if(productInvent != null){
                System.out.println("==========日志=========== 200ms内没有从redis中读取到数据，从数据库中取出并刷新的缓存");
//                productInventoryService.addProductInventoryCache(productInventory);
                 request = new ProductInventoryCacheReloadRequest(productId,productInventoryService,true);
                requestAsyncProcessService.process(request);
                return productInvent;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProductInventory(productId,-1L);
    }




}
