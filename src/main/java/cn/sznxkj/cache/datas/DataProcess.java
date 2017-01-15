package cn.sznxkj.cache.datas;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangll on 2017/1/15.
 */
public interface DataProcess<T> {

    /**
     * 处理数据
     * @return
     */
    HashMap<String, Object> process(ArrayList<T> rawData);

    /**
     * 查询数据
     * @return
     */
    ArrayList<T> draw();

    /**
     * 放入strongCache还是softCache
     * @return
     */
    boolean isStrongCache();

}
