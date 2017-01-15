package cn.sznxkj.cache.works;

import cn.sznxkj.cache.NCacheClient;
import cn.sznxkj.cache.datas.DataProcess;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangll on 2017/1/15.
 */
public class WorkPool {

    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
    private static ConcurrentHashMap<String, DataProcess<Object>> dataProcesses = new ConcurrentHashMap<>();

    /**
     * 循环遍历dataProcesses，拿出dataProcess对象，执行方法，将返回值根据Key值放入cache
     */
    public static void start() {
        pool.scheduleWithFixedDelay(()->{
            dataProcesses.keySet().forEach(
                    (key) -> {
                        HashMap<String, Object> cacheData = new HashMap<>();
                        DataProcess<Object> dp = dataProcesses.get(key);
                        try {
                            cacheData = dp.process(dp.draw());
                        }catch (Exception e){}
                        NCacheClient.putAllInStrongCache(cacheData, dp.isStrongCache());
                    }
            );
        },
                0,2,TimeUnit.SECONDS);
    }

    /**
     * 预留方法，下一版本实现，如果发现softCache中内存已经被回收，将轮询线程阻塞一段时间
     * @return
     */
    static boolean sefControl() {return true;}

    public static void addWork(String key, DataProcess<Object> dp) {
        dataProcesses.put(key, dp);
    }

}
