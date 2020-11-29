package com.how2java;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    JedisPool pool;
    Jedis jedis;

    @Before
    public void setUp(){
        jedis=new Jedis("localhost");
    }

    /**
     * Redis存储初级的字符串
     * CRUD
     */

    @Test
    public void testBasicString(){
        //添加数据
        jedis.set("name","meepo");
        System.out.println(jedis.get("name"));

        //修改数据
        //1.原来的基础上修改

        jedis.append("name","dota");
        System.out.println(jedis.get("name"));

        //2.覆盖原来的数据
        jedis.set("name","poofu");
        System.out.println(jedis.get("name"));

        //删除key对应的记录
        jedis.del("name");
        System.out.println(jedis.get("name"));

        jedis.mset("name","meepo","dota","poofu");
        System.out.println(jedis.mget("name","dota"));
    }


    /**
     * jedis操作map
     */
    @Test
    public void testMap(){
        Map<String,String> user=new HashMap<>();
        user.put("name","meepo");
        user.put("pwd","password");
        jedis.hmset("user",user);
        List<String> rsmap=jedis.hmget("user","name");
        System.out.println(rsmap);

        jedis.hdel("user","pwd");
        System.out.println(jedis.hmget("user","pwd"));
        System.out.println(jedis.hlen("user"));
        System.out.println(jedis.exists("user"));
        System.out.println(jedis.hkeys("user"));
        System.out.println(jedis.hvals("user"));

        Iterator<String> iter=jedis.hkeys("user").iterator();
        while(iter.hasNext()){
            String key=iter.next();
            System.out.println(key+":"+jedis.hmget("user",key));
        }


    }


    @Test
    public void testList(){
        //移除所有内容
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework",0,-1));

        jedis.lpush("java framework","spring");
        jedis.lpush("java framework","struts");
        jedis.lpush("java framework","hibernate");

        System.out.println(jedis.lrange("java framework",0,-1));
    }


    @Test
    public void testSet(){
        jedis.sadd("sname","meepo");
        jedis.sadd("sname","dota");
        jedis.sadd("sname","poofu");
        jedis.sadd("sname","noname");

        jedis.srem("sname","noname");

        System.out.println(jedis.smembers("sname"));//获取加入的所有key
        System.out.println(jedis.sismember("sname","meepo"));//判断meepo是否是sname集合的元素
        System.out.println(jedis.srandmember("sname"));
        System.out.println(jedis.scard("sname"));//返回元素的个数
    }


    @Test
    public void test() throws InterruptedException {
        //keys中传入通配符
        System.out.println(jedis.keys("*"));
        System.out.println(jedis.keys("*name"));
        System.out.println(jedis.del("sanmdde"));
        System.out.println(jedis.ttl("sname"));//返回key的有效时间 -1为永久有效
        jedis.setex("timekey",10,"min");
        Thread.sleep(5000);
        System.out.println(jedis.ttl("timekey"));
        jedis.setex("timekey",1,"min");
        System.out.println(jedis.ttl("timekey"));
        System.out.println(jedis.exists("key"));
        System.out.println(jedis.rename("timekey","time"));
        System.out.println(jedis.get("timekey"));
        System.out.println(jedis.get("time"));

        //jedis排序
        jedis.del("a");
        jedis.rpush("a","1");
        jedis.lpush("a","6");
        jedis.lpush("a","3");
        jedis.lpush("a","9");
        System.out.println(jedis.lrange("a",0,-1));
        System.out.println(jedis.sort("a"));
        System.out.println(jedis.lrange("a",0,-1));
    }
}
