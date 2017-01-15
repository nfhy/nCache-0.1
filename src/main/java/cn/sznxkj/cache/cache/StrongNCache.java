package cn.sznxkj.cache.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangll on 2017/1/15.
 * 永久缓存，主要存储统计数据、字典数据等内存占用小的数据
 */
public class StrongNCache implements NCache {

    private static volatile StrongNCache instance;

    private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    private StrongNCache(){}

    public static StrongNCache getInstance() {
        if (null == instance) {
            synchronized (StrongNCache.class) {
                if (null == instance) {
                    instance = new StrongNCache();
                }
            }
        }
        return instance;
    }

    @Override
    public Object getByKey(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        return null;
    }

    @Override
    public void putByKeyValue(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
