package com.leopard.kafka.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */
@Component
@Slf4j
public class RedisUtil {

    public static final int DEFAULT_CACHE_TIME = 300;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> redisString;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> redisObject;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Object> redisHash;

    @Resource(name = "redisTemplate")
    private SetOperations<String, Object> redisSet;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Object> redisList;

    @Resource(name = "redisTemplate")
    private GeoOperations redisGeo;

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, Object> redisZSet;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间（秒）
     * @return true / false
     */
    public boolean expire(String key, long time) {
        try {
            if (0 < time) {
                redisString.getOperations().expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据 key 获取过期时间
     *
     * @param key 键
     * @return
     */
    public long getExpire(String key) {
        return redisString.getOperations().getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 键
     * @return true / false
     */
    public boolean hasKey(String key) {
        try {
            return redisString.getOperations().hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键（一个或者多个）
     * @SuppressWarnings("unchecked") 忽略类型转换警告
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (null != key && 0 < key.length) {
            if (1 == key.length) {
                String redisKey = key[0];
                redisString.getOperations().delete(redisKey);
                if (redisKey.endsWith("*")) {
                    Set<String> fuzzyKeyList = getFuzzyKeyList(redisKey);
                    fuzzyKeyList.forEach(keyStr -> {
                        redisString.getOperations().delete(keyStr);
                    });
                }
            } else {
//                传入一个 Collection<String> 集合
                List<String> list = CollectionUtils.arrayToList(key);
                for (String redisKey : list) {
                    if (redisKey.endsWith("*")) {
                        Set<String> fuzzyKeyList = getFuzzyKeyList(redisKey);
                        fuzzyKeyList.forEach(keyStr -> {
                            redisString.getOperations().delete(keyStr);
                        });
                    } else {
                        redisString.getOperations().delete(redisKey);
                    }
                }
            }
        }
    }

//    ============================== String ==============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        try {
            return null == key ? null : redisString.getOperations().opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true / false
     */
    public boolean setString(String key, String value) {
        try {
            redisString.getOperations().opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true / false
     */
    public boolean setString(String key, int value) {
        return setString(key, value + "");
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒），如果 time < 0 则设置无限时间
     * @return true / false
     */
    public boolean setString(String key, String value, long time) {
        try {
            if (0 < time) {
                redisString.getOperations().opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                setString(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒），如果 time < 0 则设置无限时间
     * @return true / false
     */
    public boolean setString(String key, int value, long time) {
        return setString(key, value + "", time);
    }

    public boolean cacheDefaultTime(String key, String value) {
        try {
            redisString.getOperations().opsForValue().set(key, value, DEFAULT_CACHE_TIME, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


//    ============================== Object ==============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object getObject(String key) {
        try {
            return null == key ? null : redisObject.getOperations().opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true / false
     */
    public boolean setObject(String key, Object value) {
        try {
            redisObject.getOperations().opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒），如果 time < 0 则设置无限时间
     * @return true / false
     */
    public boolean setObject(String key, Object value, long time) {
        try {
            if (0 < time) {
                redisObject.getOperations().opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                setObject(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    ============================== Map ==============================

    /**
     * HashGet
     *
     * @param key  键（no null）
     * @param item 项（no null）
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisHash.getOperations().opsForHash().get(key, item);
    }

    /**
     * 获取 key 对应的 map
     *
     * @param key 键（no null）
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisHash.getOperations().opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 值
     * @return true / false
     */
    public boolean hmset(String key, Map<Object, Object> map) {
        try {
            redisHash.getOperations().opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  值
     * @param time 时间
     * @return true / false
     */
    public boolean hmset(String key, Map<Object, Object> map, long time) {
        try {
            redisHash.getOperations().opsForHash().putAll(key, map);
            if (0 < time) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张 Hash表 中放入数据，如不存在则创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true / false
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisHash.getOperations().opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张 Hash表 中放入数据，并设置时间，如不存在则创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间（如果原来的 Hash表 设置了时间，这里会覆盖）
     * @return true / false
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisHash.getOperations().opsForHash().put(key, item, value);
            if (0 < time) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 Hash表 中的值
     *
     * @param key  键
     * @param item 项（可以多个，no null）
     */
    public void hdel(String key, Object... item) {
        redisHash.getOperations().opsForHash().delete(key, item);
    }

    /**
     * 判断 Hash表 中是否有该键的值
     *
     * @param key  键（no null）
     * @param item 值（no null）
     * @return true / false
     */
    public boolean hHasKey(String key, String item) {
        return redisHash.getOperations().opsForHash().hasKey(key, item);
    }

//    ============================== Set ==============================

    /**
     * 获取 Set 模糊查询结果
     *
     * @param key 键
     * @return Set<String>
     */
    public Set<String> getFuzzyKeyList(String key) {
        try {
            return redisSet.getOperations().keys(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据 key 获取 set 中的所有值
     *
     * @param key 键
     * @return 值
     */
    public Set<Object> sGet(String key) {
        try {
            return redisSet.getOperations().opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从键为 key 的 set 中，根据 value 查询是否存在
     *
     * @param key   键
     * @param value 值
     * @return true / false
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisSet.getOperations().opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入 set缓存
     *
     * @param key    键值
     * @param values 值（可以多个）
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisSet.getOperations().opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将数据放入 set缓存，并设置时间
     *
     * @param key    键
     * @param time   时间
     * @param values 值（可以多个）
     * @return 成功放入个数
     */
    public long sSet(String key, long time, Object... values) {
        try {
            long count = redisSet.getOperations().opsForSet().add(key, values);
            if (0 < time) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取 set缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long sGetSetSize(String key) {
        try {
            return redisSet.getOperations().opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除 set缓存中，值为 value 的
     *
     * @param key    键
     * @param values 值
     * @return 成功移除个数
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisSet.getOperations().opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

//    ============================== List ==============================

    /**
     * 获取 list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束（0 到 -1 代表所有值）
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisList.getOperations().opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long lGetListSize(String key) {
        try {
            return redisList.getOperations().opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据索引 index 获取键为 key 的 list 中的元素
     *
     * @param key   键
     * @param index 索引
     *              当 index >= 0 时 {0:表头, 1:第二个元素}
     *              当 index < 0 时 {-1:表尾, -2:倒数第二个元素}
     * @return 值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisList.getOperations().opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将值 value 插入键为 key 的 list 中，如果 list 不存在则创建空 list
     *
     * @param key   键
     * @param value 值
     * @return true / false
     */
    public boolean lSet(String key, Object value) {
        try {
            redisList.getOperations().opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将值 value 插入键为 key 的 list 中，并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     * @return true / false
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisList.getOperations().opsForList().rightPush(key, value);
            if (0 < time) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将 values 插入键为 key 的 list 中
     *
     * @param key    键
     * @param values 值
     * @return true / false
     */
    public boolean lSetList(String key, List<Object> values) {
        try {
            redisList.getOperations().opsForList().rightPushAll(key, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将 values 插入键为 key 的 list 中，并设置时间
     *
     * @param key    键
     * @param values 值
     * @param time   时间
     * @return true / false
     */
    public boolean lSetList(String key, List<Object> values, long time) {
        try {
            redisList.getOperations().opsForList().rightPushAll(key, values);
            if (0 < time) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引 index 修改键为 key 的值
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true / false
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisList.getOperations().opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 在键为 key 的 list 中删除值为 value 的元素
     *
     * @param key   键
     * @param count 如果 count == 0 则删除 list 中所有值为 value 的元素
     *              如果 count > 0 则删除 list 中最左边那个值为 value 的元素
     *              如果 count < 0 则删除 list 中最右边那个值为 value 的元素
     * @param value
     * @return
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisList.getOperations().opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


//    ============================== GEO ==============================

    /**
     * GEOADD 缓存放入
     *
     * @param key       键
     * @param longitude 键
     * @param latitude  键
     * @param value     值
     * @return true / false
     */
    public boolean geoAdd(String key, double longitude, double latitude, Object value) {
        try {
            redisGeo.add(key, new Point(longitude, latitude), value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * GEORADIUS 计算最近的100KM以内的推荐
     *
     * @param key       键
     * @param longitude 键
     * @param latitude  键
     * @param distance  值
     * @return true / false
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoRadius(String key, double longitude, double latitude, double distance) {
        try {
            Circle circle = new Circle(new Point(longitude, latitude), new Distance(distance, Metrics.KILOMETERS));
            return redisGeo.radius(key, circle, RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().sortAscending());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 返回指定位置按距离排序的所有地址信息
     *
     * @param key       键
     * @param longitude 键
     * @param latitude  键
     * @param distance  值
     * @return true / false
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoRadiusAll(String key, double longitude, double latitude, double distance) {
        try {
            Circle circle = new Circle(new Point(longitude, latitude), new Distance(distance, Metrics.KILOMETERS));
            return redisGeo.radius(key, circle, RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().sortAscending());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    ============================== ZSET ==============================

    /**
     * 查询队列内key的数值
     *
     * @param redisKeyName
     * @param key
     * @return
     */
    public double getScore(String redisKeyName, String key) {
        try {
            return redisZSet.getOperations().opsForZSet().score(redisKeyName, key);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 设置key的数值存放到队列内
     *
     * @param redisKeyName 缓存key
     * @param key          对应的对象
     * @param score        分数值
     * @return
     */
    public Boolean setZsetScore(String redisKeyName, String key, double score) {
        Boolean result = redisZSet.getOperations().opsForZSet().add(redisKeyName, key, score);
        return result;
    }

    /**
     * 移除区间内键值
     *
     * @param redisKeyName 缓存键
     * @param start        起始位置  0为第一位
     * @param end          结束位置
     * @return
     */
    public Long zsetRemoveRange(String redisKeyName, int start, int end) {
        Long aLong = redisZSet.getOperations().opsForZSet().removeRange(redisKeyName, start, end);
        return aLong;
    }

    /**
     * 查询有序排序队列内的个数
     *
     * @param redisKeyName 缓存键
     * @return
     */
    public Long zgetZcard(String redisKeyName) {
        Long aLong = redisZSet.getOperations().opsForZSet().zCard(redisKeyName);
        return aLong;
    }

    /**
     * 查询区间内键值
     *
     * @param redisKeyName 缓存键
     * @param start        起始位置  0为第一位
     * @param end          结束位置
     * @return
     */
    public Set zsetZrange(String redisKeyName, int start, int end) {
        Set<Object> range = redisZSet.getOperations().opsForZSet().range(redisKeyName, start, end);
        return range;
    }

    /**
     * 移除有序集合中的一个或多个成员
     *
     * @param keyName 缓存键
     * @return
     */
    public Long zRemove(String redisKeyName, String keyName) {
        Long remove = redisZSet.getOperations().opsForZSet().remove(redisKeyName, keyName);
        return remove;
    }


}
