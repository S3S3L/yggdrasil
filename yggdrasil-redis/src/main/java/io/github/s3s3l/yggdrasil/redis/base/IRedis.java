package io.github.s3s3l.yggdrasil.redis.base;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.resps.GeoRadiusResponse;
import redis.clients.jedis.resps.Tuple;

public interface IRedis {

    boolean hasKey(String key);

    void set(String key, String value);

    void setnx(String key, String value);

    void set(String key, String value, long seconds);

    void set(byte[] key, byte[] value);

    void set(byte[] key, byte[] value, long seconds);

    String set(final String key, final String value, final SetParams params);

    boolean del(String key);

    boolean del(byte[] key);

    boolean expire(String key, long seconds);

    boolean expire(byte[] key, long seconds);

    String get(String key);

    byte[] getBytes(byte[] key);

    List<String> lRange(String key, long start, long stop);

    List<String> lRange(String key);

    long lPush(String key, long seconds, String... values);

    long lPush(String key, String... values);

    long lPushUnique(String key, long seconds, String... values);

    long lPushUnique(String key, String... values);

    long lPushUnique(String key, List<String> value, long seconds);

    boolean existValue(String key, String value);

    boolean popSingle(String key, String value);

    long geoadd(String geoSetName, double lng, double lat, String itemKey);

    long removeGeoKey(String geoSetName, String itemKey);

    List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit);

    List<GeoRadiusResponse> georadiusByParam(String key,
            double longitude,
            double latitude,
            double radius,
            GeoUnit unit,
            GeoRadiusParam params);

    List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit);

    Double geodist(String key, String member1, String member2, GeoUnit unit);

    void hset(String key, String hash, String value);

    void hset(String key, String hash, byte[] value);

    void hset(String key, String hash, List<String> value);

    void hset(byte[] key, byte[] hash, byte[] value);

    Set<String> hkeys(String key);

    String hget(String key, String hash);

    byte[] hget(byte[] key, byte[] hash);

    List<String> hgetList(String key, String hash);

    void hmset(String key, Map<String, String> map);

    List<String> hmget(String key, String... field);

    void hmsetList(String key, Map<String, List<String>> map);

    long incr(String key);

    long incr(String key, int seconds);

    Map<String, String> getMap(String key);

    long time();

    long setToCurrent(String key);

    @Deprecated
    Object eval(String lua);

    Object eval(String lua, String key);

    @Deprecated
    <T> T multi(Function<Transaction, T> call);

    List<GeoCoordinate> geoPos(String key, String... members);

    @Deprecated
    void psubscribe(JedisPubSub jedisPubSub, String... patterns);

    long ttl(String key);

    long sadd(String key, String... members);

    long srem(String key, String... members);

    Set<String> smembers(String key);

    long hdel(String key, String... field);

    long hdel(byte[] key, byte[]... field);

    String lpop(String key);

    long decr(String key);

    String rpop(String key);

    long llen(String key);

    long scard(String key);

    long hlen(String key);

    long zadd(String key, double score, String member);

    List<String> zrange(String key, long start, long stop);

    long zrem(String key, String... member);

    long zremRangeByScore(String key, double min, double max);

    List<Tuple> zrangeByScore(String key, double min, double max);

    Double zscoreBymember(String key, String member);

    boolean sismember(byte[] key, byte[] member);

    boolean sismember(String key, String member);

    void psubscribe(String routingKey, MessageHandler<String> handler, String... patterns);
}
