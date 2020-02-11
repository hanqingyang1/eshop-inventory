package com.roncoo.eshop.inventory.thread;

import com.roncoo.eshop.inventory.request.ProductInventoryCacheReloadRequest;
import com.roncoo.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.roncoo.eshop.inventory.request.Request;
import com.roncoo.eshop.inventory.request.RequestQueue;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 执行请求的工作线程
 */
public class RequestProcessorThread implements Callable<Boolean> {

    //自己监控的内存队列
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue){
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            while (true){
                //获取请求
                Request request = queue.take();
                boolean forceRefresh = request.isForceRefresh();

                if(!forceRefresh) {
                    //请求过滤
                    RequestQueue requestQueue = RequestQueue.getInstance();
                    Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();

                    if (request instanceof ProductInventoryDBUpdateRequest) {
                        //如果是一个更新数据库请求，那么就将对应的productId 设置成true
                        flagMap.put(request.getProductId(), true);
                    } else if (request instanceof ProductInventoryCacheReloadRequest) {
                        //如果是一个读请求，如果flag 不为空且 flag 为 true 说明已经有一个写请求就将productId 对应的 设置成fasle
                        Boolean flag = flagMap.get(request.getProductId());

                        if (flag == null) {
                            flagMap.put(request.getProductId(), false);
                        }
                        if (flag != null && flag) {
                            flagMap.put(request.getProductId(), false);
                        }

                        if (flag != null && !flag) {
                            return true;
                        }
                    }
                }
                System.out.println("===========日志=========== ： 动作线程处理请求 ，商品 id"+request.getProductId());
                //处理请求（根据request的类型，来执行对应的process方法）
                request.process();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
