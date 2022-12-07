package io.github.s3s3l.yggdrasil.redis;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import io.github.s3s3l.yggdrasil.bean.redis.HAPClusterNode;
import io.github.s3s3l.yggdrasil.bean.redis.HAPNode;
import io.github.s3s3l.yggdrasil.redis.base.InitializableRedis;
import io.github.s3s3l.yggdrasil.redis.base.MessageHandler;
import io.github.s3s3l.yggdrasil.redis.exception.RedisExcuteException;
import io.github.s3s3l.yggdrasil.utils.stuctural.StructuralHelper;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.SetParams;

public class RedisClusterHelper implements InitializableRedis<HAPClusterNode<Jedis>> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private StructuralHelper jsonHelper = JacksonUtils.JSON;
    private JedisCluster cluster;

    public static RedisClusterHelper create(Set<HAPNode> clusterNodes, GenericObjectPoolConfig<Jedis> poolConfig) {
        RedisClusterHelper operation = new RedisClusterHelper();
        operation.cluster = new JedisCluster(clusterNodes.stream()
                .map(r -> new HostAndPort(r.getHost(), r.getPort()))
                .collect(Collectors.toSet()), poolConfig);
        return operation;
    }

    @Override
    public void init(HAPClusterNode<Jedis> configuration) {
        cluster = new JedisCluster(configuration.getClusterConfig()
                .stream()
                .map(r -> new HostAndPort(r.getHost(), r.getPort()))
                .collect(Collectors.toSet()), configuration.getPoolConfig());
    }

    private void accept(Consumer<JedisCluster> action) {
        action.accept(cluster);
    }

    @Override
    public boolean hasKey(String key) {
        return cluster.exists(key);
    }

    @Override
    public void set(String key, String value) {
        cluster.set(key, value);
    }

    @Override
    public void setnx(String key, String value) {
        cluster.setnx(key, value);
    }

    @Override
    public void set(byte[] key, byte[] value) {
        execute(jedis -> jedis.set(key, value));
    }

    @Override
    public void set(String key, String value, long seconds) {
        Verify.notLargerThan(seconds, Integer.MAX_VALUE);
        execute(jedis -> jedis.setex(key, seconds, value));
    }

    @Override
    public void set(byte[] key, byte[] value, long seconds) {
        logger.trace("cache set. key: {}, value: {}, expire: {}", key, value, seconds);
        execute(jedis -> jedis.setex(key, seconds, value));
    }

    @Override
    public String set(final String key, final String value, final SetParams params) {
        return execute(jedis -> jedis.set(key, value, params));
    }

    @Override
    public boolean del(String key) {
        if (cluster.exists(key)) {
            cluster.del(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean expire(String key, long seconds) {
        if (cluster.exists(key)) {
            cluster.expire(key, seconds);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean expire(byte[] key, long seconds) {
        Verify.notLargerThan(seconds, Integer.MAX_VALUE);
        return execute(jedis -> jedis.expire(key, (int) seconds) == 1);
    }

    @Override
    public String get(String key) {
        if (cluster.exists(key)) {
            return cluster.get(key);
        } else {
            return "";
        }
    }

    @Override
    public byte[] getBytes(byte[] key) {
        byte[] value = execute(jedis -> jedis.get(key));
        logger.trace("cache fetch. key: {}, value: {}", key, value);
        return value;
    }

    @Override
    public List<String> lRange(String key) {
        if (cluster.exists(key)) {
            return cluster.lrange(key, 0, -1);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> lRange(String key, long start, long stop) {
        if (cluster.exists(key)) {
            return cluster.lrange(key, start, stop);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public long lPush(String key, String... values) {
        return cluster.lpush(key, values);
    }

    @Override
    public long lPush(String key, long seconds, String... values) {
        long count = 0;
        count = cluster.lpush(key, values);
        if (cluster.exists(key)) {
            cluster.expire(key, (int) seconds);
        }
        return count;
    }

    @Override
    public long lPushUnique(String key, long seconds, String... values) {
        long count = 0;
        Set<String> set = new HashSet<>();
        if (cluster.exists(key)) {
            set.addAll(cluster.lrange(key, 0, -1));
        }
        set.addAll(Arrays.asList(values));
        cluster.del(key);
        count = cluster.lpush(key, set.toArray(new String[set.size()]));
        if (cluster.exists(key)) {
            cluster.expire(key, (int) seconds);
        }
        return count;
    }

    @Override
    public long lPushUnique(String key, String... values) {
        long count = 0;
        Set<String> set = new HashSet<>();
        if (cluster.exists(key)) {
            set.addAll(cluster.lrange(key, 0, -1));
        }
        set.addAll(Arrays.asList(values));
        cluster.del(key);
        count = cluster.lpush(key, set.toArray(new String[set.size()]));
        return count;
    }

    @Override
    public long lPushUnique(String key, List<String> value, long seconds) {
        return lPushUnique(key, seconds, value.toArray(new String[value.size()]));
    }

    @Override
    public boolean existValue(String key, String value) {
        boolean result = false;
        if (!cluster.exists(key)) {
            return result;
        }

        List<String> list = cluster.lrange(key, 0, -1);
        if (list.contains(value)) {
            result = true;
        }
        return result;
    }

    @Override
    public boolean popSingle(String key, String value) {
        boolean result = false;

        if (!cluster.exists(key)) {
            return result;
        }

        List<String> list = cluster.lrange(key, 0, -1);
        int index = list.indexOf(value);
        if (index < 0) {
            return result;
        }
        list.remove(index);
        cluster.del(key);
        if (!list.isEmpty()) {
            cluster.lpush(key, list.toArray(new String[list.size()]));
        }
        result = true;
        return result;
    }

    @Override
    public long geoadd(String geoSetName, double lng, double lat, String itemKey) {
        return execute(jedis -> jedis.geoadd(geoSetName, lng, lat, itemKey));
    }

    @Override
    public long removeGeoKey(String geoSetName, String itemKey) {
        return execute(jedis -> jedis.zrem(geoSetName, itemKey));
    }

    public <T> T execute(Function<JedisCluster, T> action) {
        if (null == cluster) {
            throw new RedisExcuteException("Jedis cluster not set.");
        }
        return action.apply(cluster);
    }

    public static class JedisConfiguration {
        private String hostName;
        private int port;
        private String password;
        private int timeout;
        private int database;
        private int maxTotal;
        private int maxIdle;
        private long maxWaitMillis;

        public String getHostName() {
            return hostName;
        }

        public JedisConfiguration setHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public int getPort() {
            return port;
        }

        public JedisConfiguration setPort(int port) {
            this.port = port;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public JedisConfiguration setPassword(String password) {
            this.password = password;
            return this;
        }

        public int getTimeout() {
            return timeout;
        }

        public JedisConfiguration setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public int getDatabase() {
            return database;
        }

        public JedisConfiguration setDatabase(int database) {
            this.database = database;
            return this;
        }

        public int getMaxTotal() {
            return maxTotal;
        }

        public JedisConfiguration setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
            return this;
        }

        public int getMaxIdle() {
            return maxIdle;
        }

        public JedisConfiguration setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
            return this;
        }

        public long getMaxWaitMillis() {
            return maxWaitMillis;
        }

        public JedisConfiguration setMaxWaitMillis(long maxWaitMillis) {
            this.maxWaitMillis = maxWaitMillis;
            return this;
        }
    }

    @Override
    public void hset(String key, String hash, List<String> value) {
        hset(key, hash, jsonHelper.toStructuralString(value));
    }

    @Override
    public void hset(String key, String hash, String value) {
        cluster.hset(key, hash, value);
    }

    @Override
    public String hget(String key, String hash) {
        return cluster.hget(key, hash);
    }

    @Override
    public List<String> hgetList(String key, String hash) {
        String data = hget(key, hash);
        return StringUtils.isEmpty(data) ? null : jsonHelper.toObject(data, new TypeReference<List<String>>() {
        });
    }

    @Override
    public void hmset(String key, Map<String, String> map) {
        cluster.hmset(key, map);
    }

    @Override
    public List<String> hmget(String key, String... field) {
        return cluster.hmget(key, field);
    }

    @Override
    public void hmsetList(String key, Map<String, List<String>> map) {
        Map<String, String> stringMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            stringMap.put(entry.getKey(), jsonHelper.toStructuralString(entry.getValue()));
        }
        hmset(key, stringMap);
    }

    @Override
    public long incr(String key) {
        return cluster.incr(key);
    }

    @Override
    public long incr(String key, int seconds) {
        long result = cluster.incr(key);
        cluster.expire(key, seconds);
        return result;
    }

    @Override
    public List<GeoRadiusResponse>
            georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        return execute(jedis -> jedis.georadius(key, longitude, latitude, radius, unit));
    }

    @Override
    public List<GeoRadiusResponse> georadiusByParam(String key,
            double longitude,
            double latitude,
            double radius,
            GeoUnit unit,
            GeoRadiusParam params) {
        return execute(jedis -> jedis.georadius(key, longitude, latitude, radius, unit, params));
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        return execute(jedis -> jedis.georadiusByMember(key, member, radius, unit));
    }

    @Override
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        return execute(jedis -> jedis.geodist(key, member1, member2));
    }

    @Override
    public Map<String, String> getMap(String key) {
        return cluster.hgetAll(key);
    }

    @Override
    public long time() {
        return execute(jedis -> Long.valueOf(jedis
                .eval("redis.replicate_commands();" + "local times = redis.call('time');"
                        + "local time = string.format(\"%s%s\",times[1],string.sub(times[2],0,3));" + "return time;",
                        null)
                .toString()));
    }

    @Override
    public long setToCurrent(String key) {
        return execute(jedis -> Long.valueOf(jedis
                .eval(("redis.replicate_commands();" + "local times = redis.call('time');"
                        + "local time = string.format(\"%s%s\",times[1],string.sub(times[2],0,3));"
                        + "local oldTime = redis.call('get',\"{key}\");"
                        + "if time > oldTime then redis.call('set',\"{key}\",time); end " + "return time;")
                                .replaceAll("\\{key\\}", key),
                        key)
                .toString()));
    }

    @Override
    public boolean del(byte[] key) {
        return execute(jedis -> jedis.del(key) > 0);
    }

    @Override
    public Object eval(String lua) {
        return execute(jedis -> jedis.eval(lua, null));
    }

    @Override
    public <T> T multi(Function<Transaction, T> call) {
        return null;
    }

    @Override
    public List<GeoCoordinate> geoPos(String key, String... members) {
        return execute(jedis -> jedis.geopos(key, members));
    }

    @Override
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        accept(jedis -> jedis.psubscribe(jedisPubSub, patterns));
    }

    @Override
    public void psubscribe(String routingKey, MessageHandler<String> handler, String... patterns) {
        accept(redis -> redis.psubscribe(new JedisPubSub() {
            @Override
            public void onPMessage(String pattern, String channel, String message) {
                handler.handle(message, pattern);
            }
        }, patterns));
    }

    @Override
    public long ttl(String key) {
        return execute(jedis -> jedis.ttl(key));
    }

    @Override
    public long sadd(String key, String... members) {
        return execute(jedis -> jedis.sadd(key, members));
    }

    @Override
    public long srem(String key, String... members) {
        return execute(jedis -> jedis.srem(key, members));
    }

    @Override
    public Set<String> smembers(String key) {
        return execute(jedis -> jedis.smembers(key));
    }

    @Override
    public long hdel(String key, String... field) {
        return execute(jedis -> jedis.hdel(key, field));
    }

    @Override
    public String lpop(String key) {
        return execute(jedis -> jedis.lpop(key));
    }

    @Override
    public long decr(String key) {
        return execute(jedis -> jedis.decr(key));
    }

    @Override
    public String rpop(String key) {
        return execute(jedis -> jedis.rpop(key));
    }

    @Override
    public long llen(String key) {
        return execute(jedis -> jedis.llen(key));
    }

    @Override
    public long scard(String key) {
        return execute(jedis -> jedis.scard(key));
    }

    @Override
    public long hlen(String key) {
        return execute(jedis -> jedis.hlen(key));
    }

    @Override
    public long zadd(String key, double score, String member) {
        return execute(jedis -> jedis.zadd(key, score, member));
    }

    @Override
    public Set<String> zrange(String key, long start, long stop) {
        return execute(jedis -> jedis.zrange(key, start, stop));
    }

    @Override
    public long zrem(String key, String... member) {
        return execute(jedis -> jedis.zrem(key, member));
    }

    @Override
    public long zremRangeByScore(String key, double min, double max) {
        return execute(jedis -> jedis.zremrangeByScore(key, min, max));
    }

    @Override
    public Set<Tuple> zrangeByScore(String key, double min, double max) {
        return execute(jedis -> jedis.zrangeByScoreWithScores(key, min, max));
    }

    @Override
    public Double zscoreBymember(String key, String member) {
        return execute(jedis -> jedis.zscore(key, member));
    }

    @Override
    public Set<String> hkeys(String key) {
        return execute(jedis -> jedis.hkeys(key));
    }

    @Override
    public void hset(String key, String hash, byte[] value) {
        execute(jedis -> jedis.hset(key.getBytes(StandardCharsets.UTF_8), hash.getBytes(StandardCharsets.UTF_8),
                value));
    }

    @Override
    public byte[] hget(byte[] key, byte[] hash) {
        return execute(jedis -> jedis.hget(key, hash));
    }

    @Override
    public void hset(byte[] key, byte[] hash, byte[] value) {
        execute(jedis -> jedis.hset(key, hash, value));
    }

    @Override
    public long hdel(byte[] key, byte[]... field) {
        return execute(jedis -> jedis.hdel(key, field));
    }

    @Override
    public Object eval(String lua, String key) {
        return execute(jedis -> jedis.eval(lua, key));
    }

    @Override
    public boolean sismember(byte[] key, byte[] member) {
        return execute(jedis -> jedis.sismember(key, member));
    }

    @Override
    public boolean sismember(String key, String member) {
        return execute(jedis -> jedis.sismember(key, member));
    }
}
