package cn.sznxkj.cache.cache;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangll on 2017/1/15.
 * 内存敏感缓存，主要存储数量大，占用内存高的数据，如按地市划分的所有考生对象集合
 */
public class SoftNCache implements NCache {
    /**
     * 单例模式
     */
    private SoftNCache() {}

    private static volatile SoftNCache instance;

    private static ConcurrentHashMap<String, SoftReference<Object>> cache = new ConcurrentHashMap<>();

    public static SoftNCache getInstance() {
        if (null == instance) {
            synchronized (SoftNCache.class) {
                if (null == instance) {
                    instance = new SoftNCache();
                }
            }
        }
        return instance;
    }

    @Override
    public Object getByKey(String key) {
        if (cache.containsKey(key)) {
            SoftReference<Object> sr = cache.get(key);
            Object o;
            if ((o = sr.get()) != null) {
                return o;
            }
        }
        //此处调用DataProcess的方法更新缓存
        return null;
    }

    @Override
    public void putByKeyValue(String key, Object value) {
        cache.put(key, new SoftReference<>(value));
    }

    @Override
    public void clear() {
        cache.keySet().forEach(
                (key)-> cache.get(key).clear()
        );
        cache.clear();
    }
}
