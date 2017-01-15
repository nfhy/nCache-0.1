package cn.sznxkj.cache;

import cn.sznxkj.cache.cache.NCache;
import cn.sznxkj.cache.cache.SoftNCache;
import cn.sznxkj.cache.cache.StrongNCache;
import cn.sznxkj.cache.datas.DataProcess;
import cn.sznxkj.cache.works.WorkPool;

import java.util.HashMap;

/**
 * Created by wangll on 2017/1/15.
 *
 */
public class NCacheClient {

    private static SoftNCache softCache;
    private static StrongNCache strongCache;


    NCacheClient() {}

    /**
     * 初始化ncache，获取所有work，启动工作线程，开始循环更新缓存
     */
    void init() {
        softCache = SoftNCache.getInstance();
        strongCache = StrongNCache.getInstance();
        WorkPool.start();
    }

    public Object getByKey(String key) {
        Object o;
        if ((o = softCache.getByKey(key)) == null) {
            o = strongCache.getByKey(key);
        }
        return o;
    }

    public static void putAllInStrongCache(HashMap<String, Object> data, boolean isStrong) {
        if (null != data && ! data.isEmpty()) {
            data.keySet().forEach((key) -> {
                NCache cache = isStrong?strongCache:softCache;
                cache.putByKeyValue(key, data.get(key));
            });
        }
    }

    public static void addWork(String key, DataProcess<Object> dp) {
        WorkPool.addWork(key, dp);
    }

}
