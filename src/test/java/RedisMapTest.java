import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redis.impl.RedisMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RedisMapTest {
    private JedisPool jedis;
    private RedisMap redisMap;

    @BeforeEach
    public void setUp() {
        jedis = new JedisPool("localhost", 6379);
        redisMap = new RedisMap(jedis);
        redisMap.clear();
    }

    @AfterEach
    public void tearDown() {
        jedis.close();
    }

    @Test
    public void testSize() {
        assertEquals(0, redisMap.size());
        redisMap.put("key1", "value1");
        assertEquals(1, redisMap.size());
        redisMap.put("key2", "value2");
        assertEquals(2, redisMap.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(redisMap.isEmpty());
        redisMap.put("key1", "value1");
        assertFalse(redisMap.isEmpty());
    }

    @Test
    public void testContainsKey() {
        redisMap.put("key1", "value1");
        assertTrue(redisMap.containsKey("key1"));
        assertFalse(redisMap.containsKey("key2"));
    }

    @Test
    public void testContainsValue() {
        redisMap.put("key1", "value1");
        assertTrue(redisMap.containsValue("value1"));
        assertFalse(redisMap.containsValue("value2"));
    }

    @Test
    public void testGet() {
        redisMap.put("key1", "value1");
        assertEquals("value1", redisMap.get("key1"));
        assertNull(redisMap.get("key2"));
    }

    @Test
    public void testPut() {
        redisMap.put("key1", "value1");
        assertEquals("value1", redisMap.get("key1"));
        redisMap.put("key1", "value2");
        assertEquals("value2", redisMap.get("key1"));
    }

    @Test
    public void testRemove() {
        redisMap.put("key1", "value1");
        assertEquals("value1", redisMap.remove("key1"));
        assertNull(redisMap.get("key1"));
    }

    @Test
    public void testPutAll() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        redisMap.putAll(map);
        assertEquals(2, redisMap.size());
        assertEquals("value1", redisMap.get("key1"));
        assertEquals("value2", redisMap.get("key2"));
    }

    @Test
    public void testClear() {
        redisMap.put("key1", "value1");
        redisMap.clear();
        assertTrue(redisMap.isEmpty());
    }

    @Test
    public void testKeySet() {
        redisMap.put("key1", "value1");
        redisMap.put("key2", "value2");
        assertTrue(redisMap.keySet().contains("key1"));
        assertTrue(redisMap.keySet().contains("key2"));
    }

    @Test
    public void testValues() {
        redisMap.put("key1", "value1");
        redisMap.put("key2", "value2");
        assertTrue(redisMap.values().contains("value1"));
        assertTrue(redisMap.values().contains("value2"));
    }

    @Test
    public void testEntrySet() {
        redisMap.put("key1", "value1");
        redisMap.put("key2", "value2");
        assertEquals(2, redisMap.entrySet().size());
    }
}