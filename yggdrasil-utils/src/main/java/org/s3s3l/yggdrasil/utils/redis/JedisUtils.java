package org.s3s3l.yggdrasil.utils.redis;

import java.io.IOException;
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

import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.json.IJsonHelper;
import org.s3s3l.yggdrasil.utils.json.JacksonUtils;
import org.s3s3l.yggdrasil.utils.redis.JedisUtils.JedisConfiguration;
import org.s3s3l.yggdrasil.utils.redis.base.InitializableRedis;
import org.s3s3l.yggdrasil.utils.redis.base.MessageHandler;
import org.s3s3l.yggdrasil.utils.redis.exception.RedisExcuteException;
import org.s3s3l.yggdrasil.utils.verify.Verify;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

import com.fasterxml.jackson.core.type.TypeReference;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.geo.GeoRadiusParam;

public class JedisUtils implements InitializableRedis<JedisConfiguration> {

    private JedisPool jedisPool;
    private IJsonHelper jsonHelper = JacksonUtils.defaultHelper;

    public static JedisUtils create(JedisConfiguration config) {
        JedisUtils operation = new JedisUtils();
        operation.init(config);
        return operation;
    }

    private void accept(Consumer<Jedis> action) {
        try (Jedis jedis = jedisPool.getResource()) {
            action.accept(jedis);
        }
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void init(JedisConfiguration config) {
        JedisPoolConfig poolConfig;
        poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(config.getMaxTotal());
        poolConfig.setMaxIdle(config.getMaxIdle());
        poolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
        jedisPool = new JedisPool(poolConfig, config.getHostName(), config.getPort(), config.getTimeout(),
                config.getPassword(), config.getDatabase());
    }

    @Override
    public boolean hasKey(String key) {
        return execute(jedis -> jedis.exists(key));
    }

    @Override
    public void set(String key, String value) {
        execute(jedis -> jedis.set(key, value));
    }

    @Override
    public void setnx(String key, String value) {
        execute(jedis -> jedis.setnx(key, value));
    }

    @Override
    public void set(byte[] key, byte[] value) {
        execute(jedis -> jedis.set(key, value));
    }

    @Override
    public void set(String key, String value, long seconds) {
        Verify.notLargerThan(seconds, Integer.MAX_VALUE);
        execute(jedis -> {
            jedis.setex(key, (int) seconds, value);
            return null;
        });
    }

    @Override
    public void set(byte[] key, byte[] value, long seconds) {
        Verify.notLargerThan(seconds, Integer.MAX_VALUE);
        execute(jedis -> {
            jedis.setex(key, (int) seconds, value);
            return null;
        });
    }

    @Override
    public String set(final String key, final String value, final String nxxx, final String expx, final long time) {
        return execute(jedis -> jedis.set(key, value, nxxx, expx, time));
    }

    @Override
    public boolean del(String key) {
        return execute(jedis -> {
            if (!jedis.exists(key)) {
                return false;
            }
            jedis.del(key);
            return true;
        });
    }

    @Override
    public boolean expire(String key, long seconds) {
        return execute(jedis -> expire(jedis, key, seconds));
    }

    @Override
    public boolean expire(byte[] key, long seconds) {
        Verify.notLargerThan(seconds, Integer.MAX_VALUE);
        return execute(jedis -> jedis.expire(key, (int) seconds) == 1);
    }

    @Override
    public String get(String key) {
        return execute(jedis -> {
            if (!jedis.exists(key)) {
                return StringUtils.EMPTY_STRING;
            }
            return jedis.get(key);
        });
    }

    @Override
    public byte[] getBytes(byte[] key) {
        return execute(jedis -> jedis.get(key));
    }

    @Override
    public List<String> lRange(String key) {
        return execute(jedis -> {
            if (!jedis.exists(key)) {
                return Collections.emptyList();
            }
            return jedis.lrange(key, 0, jedis.llen(key));
        });
    }

    @Override
    public List<String> lRange(String key, long start, long stop) {
        return execute(jedis -> {
            if (!jedis.exists(key)) {
                return Collections.emptyList();
            }
            return jedis.lrange(key, start, stop);
        });
    }

    @Override
    public long lPush(String key, String... values) {
        return execute(jedis -> jedis.lpush(key, values));
    }

    @Override
    public long lPush(String key, long seconds, String... values) {
        return execute(jedis -> {
            long count = jedis.lpush(key, values);
            expire(jedis, key, seconds);
            return count;
        });
    }

    @Override
    public long lPushUnique(String key, long seconds, String... values) {
        return execute(jedis -> {
            long count = 0;
            Set<String> set = new HashSet<>();
            if (jedis.exists(key)) {
                set.addAll(jedis.lrange(key, 0, jedis.llen(key)));
            }
            set.addAll(Arrays.asList(values));
            jedis.del(key);
            count = jedis.lpush(key, set.toArray(new String[set.size()]));
            expire(jedis, key, seconds);
            return count;
        });
    }

    @Override
    public long lPushUnique(String key, String... values) {
        return execute(jedis -> {
            long count = 0;
            Set<String> set = new HashSet<>();
            if (jedis.exists(key)) {
                set.addAll(jedis.lrange(key, 0, jedis.llen(key)));
            }
            set.addAll(Arrays.asList(values));
            jedis.del(key);
            count = jedis.lpush(key, set.toArray(new String[set.size()]));
            return count;
        });
    }

    @Override
    public long lPushUnique(String key, List<String> value, long seconds) {
        return lPushUnique(key, seconds, value.toArray(new String[value.size()]));
    }

    @Override
    public boolean existValue(String key, String value) {
        return execute(jedis -> {

            boolean result = false;
            try {
                if (!jedis.exists(key)) {
                    return result;
                }

                long len = jedis.llen(key);
                List<String> list = jedis.lrange(key, 0, len);
                if (list.contains(value)) {
                    result = true;
                }
            } catch (Exception e) {
                result = false;
                throw e;
            }
            return result;
        });
    }

    @Override
    public boolean popSingle(String key, String value) {
        return execute(jedis -> {
            boolean result = false;

            try {
                if (!jedis.exists(key)) {
                    return result;
                }

                long len = jedis.llen(key);
                List<String> list = jedis.lrange(key, 0, len);
                int index = list.indexOf(value);
                if (index < 0) {
                    return result;
                }
                list.remove(index);
                jedis.del(key);
                if (!list.isEmpty()) {
                    jedis.lpush(key, list.toArray(new String[list.size()]));
                }
                result = true;
            } catch (Exception e) {
                result = false;
                throw e;
            }
            return result;
        });
    }

    @Override
    public long geoadd(String geoSetName, double lng, double lat, String itemKey) {
        return execute(jedis -> jedis.geoadd(geoSetName, lng, lat, itemKey));
    }

    @Override
    public long removeGeoKey(String geoSetName, String itemKey) {
        return execute(jedis -> jedis.zrem(geoSetName, itemKey));
    }

    public <T> T execute(Function<Jedis, T> action) {
        if (null == jedisPool) {
            throw new RedisExcuteException("Jedis pool not set.");
        }
        return action.apply(jedisPool.getResource());
    }

    public static class JedisConfiguration {
        private RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        private int timeout;
        private int maxTotal;
        private int maxIdle;
        private long maxWaitMillis;

        public String getHostName() {
            return config.getHostName();
        }

        public JedisConfiguration setHostName(String hostName) {
            config.setHostName(hostName);
            return this;
        }

        public int getPort() {
            return config.getPort();
        }

        public RedisStandaloneConfiguration getConfig() {
            return config;
        }

        public void setConfig(RedisStandaloneConfiguration config) {
            this.config = config;
        }

        public JedisConfiguration setPort(int port) {
            config.setPort(port);
            return this;
        }

        public String getPassword() {
            return new String(config.getPassword()
                    .toOptional()
                    .orElse(new char[] {}));
        }

        public JedisConfiguration setPassword(String password) {
            config.setPassword(RedisPassword.of(password));
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
            return config.getDatabase();
        }

        public JedisConfiguration setDatabase(int database) {
            config.setDatabase(database);
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
        hset(key, hash, jsonHelper.toJsonString(value));
    }

    @Override
    public void hset(String key, String hash, String value) {
        execute(jedis -> jedis.hset(key, hash, value));
    }

    @Override
    public String hget(String key, String hash) {
        return execute(jedis -> jedis.hget(key, hash));
    }

    @Override
    public List<String> hgetList(String key, String hash) {
        String data = hget(key, hash);
        return StringUtils.isEmpty(data) ? null : jsonHelper.toObject(data, new TypeReference<List<String>>() {
        });
    }

    @Override
    public void hmset(String key, Map<String, String> map) {
        execute(jedis -> jedis.hmset(key, map));
    }

    @Override
    public List<String> hmget(String key, String... field) {
        return execute(jedis -> jedis.hmget(key, field));
    }

    @Override
    public void hmsetList(String key, Map<String, List<String>> map) {
        Map<String, String> stringMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            stringMap.put(entry.getKey(), jsonHelper.toJsonString(entry.getValue()));
        }
        hmset(key, stringMap);
    }

    @Override
    public long incr(String key) {
        return execute(jedis -> jedis.incr(key));
    }

    @Override
    public long incr(String key, int seconds) {
        return execute(jedis -> {
            long value = jedis.incr(key);
            jedis.expire(key, seconds);
            return value;
        });
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
        return execute(jedis -> jedis.hgetAll(key));
    }

    @Override
    public long time() {
        return execute(jedis -> Long.valueOf(jedis
                .eval("redis.replicate_commands();" + "local times = redis.call('time');"
                        + "local time = string.format(\"%s%s\",times[1],string.sub(times[2],0,3));" + "return time;")
                .toString()));
    }

    @Override
    public long setToCurrent(String key) {
        return execute(jedis -> Long.valueOf(jedis
                .eval(("redis.replicate_commands();" + "local times = redis.call('time');"
                        + "local time = string.format(\"%s%s\",times[1],string.sub(times[2],0,3));"
                        + "local oldTime = redis.call('get',\"{key}\");"
                        + "if time > oldTime then redis.call('set',\"{key}\",time); end " + "return time;")
                                .replaceAll("\\{key\\}", key))
                .toString()));
    }

    @Override
    public boolean del(byte[] key) {
        return execute(jedis -> jedis.del(key) > 0);
    }

    @Override
    public Object eval(String lua) {
        return execute(jedis -> jedis.eval(lua));
    }

    @Override
    public <T> T multi(Function<Transaction, T> call) {
        return execute(jedis -> {
            try (Transaction transaction = jedis.multi()) {
                return call.apply(transaction);
            } catch (IOException e) {
                throw new RedisExcuteException(e);
            }
        });
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

    private boolean expire(Jedis jedis, String key, long seconds) {
        if (!jedis.exists(key) || seconds > Integer.MAX_VALUE) {
            return false;
        }
        jedis.expire(key, (int) seconds);
        return true;

    }

    @Override
    public Object eval(String lua, String key) {
        return execute(jedis -> jedis.eval(lua, 1, key));
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
