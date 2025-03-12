package org.redis.impl;

import org.redis.RedisException.RedisMapException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;


public class RedisMap implements Map<String,String> {
    public final JedisPool jedisPool;

    public RedisMap(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    @Override
    public int size() {
        try (Jedis jedis = jedisPool.getResource()) {
            return (int) jedis.dbSize();
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object o) {
        if(o == null){
            throw new NullPointerException("key is null");
        }
        if(!(o instanceof String)) {
            throw new ClassCastException("Key not instance of String");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists((String) o);
        } catch (RuntimeException e) {
            throw new RedisMapException(e.getMessage());
        }
    }

    @Override
    public boolean containsValue(Object o) {
        if(o == null){
            throw new NullPointerException("key is null");
        }
        if(!(o instanceof String)) {
            throw new ClassCastException("Key not instance of String");
        }
        return values().contains(o);
    }

    @Override
    public String get(Object o) {
        if(o == null){
            throw new NullPointerException("key is null");
        }
        if(!(o instanceof String)) {
            throw new ClassCastException("Key not instance of String");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get((String) o);
        } catch (RuntimeException e) {
            throw new RedisMapException(e.getMessage());
        }
    }

    @Override
    public String put(String s, String s2) {
        if(s == null || s2 == null){
            throw new NullPointerException("key is null");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(s, s2);
        }catch (RuntimeException e) {
            throw new RedisMapException(e.getMessage());
        }
    }

    @Override
    public String remove(Object o) {
        if(o == null){
            throw new NullPointerException("key is null");
        }
        if(!(o instanceof String)) {
            throw new ClassCastException("Key not instance of String");
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.getDel((String) o);
        }catch (RuntimeException e) {
            throw new RedisMapException(e.getMessage());
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
        try (Jedis jedis = jedisPool.getResource()) {
            map.forEach((k, v) -> jedis.mset(k, v));
        } catch (RuntimeException e) {
            throw new RedisMapException(e.getMessage());
        }
    }

    @Override
    public void clear() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushDB();
        }
    }

    @Override
    public Set<String> keySet() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.keys("*");
        } catch (RuntimeException e) {
            throw new RedisMapException(e.getMessage());
        }
    }

    @Override
    public Collection<String> values() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.mget((jedis.keys("*").toArray(new String[size()])));
        } catch (RuntimeException e) {
            throw new RedisMapException(e.getMessage());
        }
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<Entry<String, String>> set = new HashSet<>();
            jedis.keys("*").forEach(
                    e -> set.add(new AbstractMap.SimpleEntry<String, String>(e, get(e))));
            return set;
        } catch (RuntimeException e) {
            throw new RedisMapException(e.getMessage());
        }
    }
}
