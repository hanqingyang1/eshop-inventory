package com.roncoo.eshop.inventory.thread;


import com.roncoo.eshop.inventory.request.Request;
import com.roncoo.eshop.inventory.request.RequestQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 请求处理线程池
 * 单例 方式
 */
public class RequestProcessorThreadPool {

    //创建一个指定大小的线程池
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);



    //标准单例模式
  public RequestProcessorThreadPool(){
      RequestQueue instance = RequestQueue.getInstance();
      for (int i = 0; i < 10; i++) {
          ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(100);
          instance.addQueue(queue);
          threadPool.submit(new RequestProcessorThread(queue));
      }
  }

    private static class SingletonHolder{
        private static final RequestProcessorThreadPool INSTANCE = new RequestProcessorThreadPool();
    }

    public static RequestProcessorThreadPool getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 初始化便捷方法
     */
    public static void init(){
        getInstance();
    }


     /*public static class singleton{
        private static RequestProcessorThreadPool instance;

        static {
            instance = new RequestProcessorThreadPool();
        }
        public static RequestProcessorThreadPool getInstance(){
            return instance;
        }
    }*/
}
