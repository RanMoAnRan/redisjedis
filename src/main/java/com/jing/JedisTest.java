package com.jing;

import org.junit.Test;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName:jedis
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/5/30 19:42
 * @Version 1.0
 */
public class JedisTest {

    @Test
    public void jedisTest01() {
        //创建jedis对象
        Jedis jedis = new Jedis("192.168.72.142", 6379);
        //测试是否连通
        String ping = jedis.ping();
        System.out.println(ping);
        jedis.close();
    }


    // 使用jedis操作redis_string
    @Test
    public void jedisByStringTest02() {
        Jedis jedis = new Jedis("192.168.72.142", 6379);
        //添加元素
        jedis.set("name", "靖哥");
        jedis.set("age", "20");
        //获取元素
        String name = jedis.get("name");
        String age = jedis.get("age");
        System.out.println(name + "..." + age);

        //删除key
        // jedis.del("age");

        //将指定的key的value原子性的递增1.如果该key不存在，其初始值为0，在incr之后其值为1。如果value的值不能转成整型，如hello，该操作将执行失败并返回相应的错误信息
        jedis.incr("age");
        System.out.println(jedis.get("age"));

        //将指定的key的value原子性的递减1.如果该key不存在，其初始值为0，在incr之后其值为-1。如果value的值不能转成整型，如hello，该操作将执行失败并返回相应的错误信息
        jedis.decr("age");
        System.out.println(jedis.get("age"));

        //将指定的key的value原子性增加increment，如果该key不存在，器初始值为0，在incrby之后，该值为increment。如果该值不能转成整型，如hello则失败并返回错误信息
        jedis.incrBy("age", 10);
        System.out.println(jedis.get("age"));

        //减少固定的值
        jedis.decrBy("age", 10);
        System.out.println(jedis.get("age"));

        //拼凑字符串。如果该key存在，则在原有的value后追加该值；如果该key不存在，则重新创建一个key|value
        jedis.append("name", "shuai");
        System.out.println(jedis.get("name"));

        //为新创建的key设置时长
        String email = jedis.setex("email", 5, "453485453@qq.com");
        while (jedis.exists("email")) {
            System.out.println(jedis.ttl("email"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 用来查看所有的key
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        jedis.close();

    }


    //使用jedis操作redis_hash
    @Test
    public void jedisByhashTest03() {
        Jedis jedis = new Jedis("192.168.72.142", 6379);
        //存值
        jedis.hset("list1", "name", "靖哥");
        jedis.hset("list1", "age", "18");
        jedis.hset("list1", "email", "453485453@qq.com");
        //取值
        String name = jedis.hget("list1", "name");
        System.out.println(name);
        System.out.println("-----------------");

        //获取指定key的多个field值
        List<String> list1 = jedis.hmget("list1", "name", "age", "email");
        for (String s : list1) {
            System.out.println(s);
        }
        System.out.println("-----------------");

        //获取指定key中的所有的field与value的值
        Map<String, String> map = jedis.hgetAll("list1");
        for (String s : map.keySet()) {
            System.out.println(s);
        }
        for (String value : map.values()) {
            System.out.println(value);
        }
        System.out.println("------------");

        //获取指定key中map的所有的field
        Set<String> hkeys = jedis.hkeys("list1");
        for (String hkey : hkeys) {
            System.out.println(hkey);
        }
        System.out.println("--------------");

        //获取指定key中map的所有的value
        List<String> hvals = jedis.hvals("list1");
        for (String hval : hvals) {
            System.out.println(hval);
        }

        //删除
        jedis.hset("student", "name", "zhangsan");
        jedis.hset("student", "age", "20");
        //删除name
        jedis.hdel("student", "name");
        //删除整个student
        jedis.del("student");

        //为某个key的某个属性增加值、
        jedis.hincrBy("list1", "age", 1);
        String age = jedis.hget("list1", "age");
        System.out.println(age);
        System.out.println("--------------------");

        //判断某个key中的filed是否存在
        Boolean hexists = jedis.hexists("student", "name");
        System.out.println(hexists);

        //获取key中所包含的field的数量
        Long hlen = jedis.hlen("list1");
        System.out.println(hlen);

        jedis.close();

    }


    //使用jedis操作redis_list
    @Test
    public void jedisByListTest04() {
        Jedis jedis = new Jedis("192.168.72.142", 6379);

        jedis.del("list1");

        //从左侧添加数据, 从右侧将数据取出
        jedis.lpush("list1", "A", "B", "C", "D", "E", "F", "G");
        String rpop = jedis.rpop("list1"); //每次都是弹出一个元素
        System.out.println(rpop);
        List<String> list = jedis.lrange("list1", 0, -1);
        System.out.println(list);

        //获取整个列表的个数
        Long size = jedis.llen("list1");
        System.out.println(size);

        //在  D 元素的前面添加一个0元素
        jedis.linsert("list1", BinaryClient.LIST_POSITION.BEFORE,"D","0");
        List<String> list2 = jedis.lrange("list1", 0, -1);
        System.out.println(list2);

        //从元素的尾(右)部弹出一个元素, 将这个元素添加头(左)部去
        jedis.rpoplpush("list1","list1");
        List<String> list3 = jedis.lrange("list1", 0, -1);
        System.out.println(list3);

        jedis.close();
    }


    //使用jedis操作redis_set :  无序 去重
    @Test
    public void jedisBySetTest05(){
        //1. 创建jedis对象
        Jedis jedis = new Jedis("192.168.72.142", 6379);

        //2. 执行相关的操作
        //2.1 添加数据
        jedis.sadd("set1","A","B","C","D","C","B","E");
        jedis.sadd("set2","A","B","G","E","Q");

        //2.2 获取整个列表
        Set<String> set = jedis.smembers("set1");
        System.out.println(set);

        //2.3 判断  E  元素 是否存在
        Boolean flag = jedis.sismember("set1", "E");
        System.out.println(flag); // 如果存在, 返回true

        //2.4 求两个交集
        Set<String> sinter = jedis.sinter("set1", "set2");
        System.out.println(sinter);

        //2.5 获取 set1 中一共有多少个元素
        Long size = jedis.scard("set1");
        System.out.println(size);

        //2.6 求两个并集, 将并集的结果放置在set3中
        jedis.sunionstore("set3","set1","set2");

        set = jedis.smembers("set3");
        System.out.println(set);

        //2.7 删除 set3 中的  Q 元素
        jedis.srem("set3","Q");

        set = jedis.smembers("set3");
        System.out.println(set);

        //3. 释放资源
        jedis.close();

    }

    //使用jedis操作redis_sortedSet :  有序 去重
    @Test
    public void jedisBySortedSetTest06(){
        Jedis jedis = new Jedis("192.168.72.142", 6379);
        //添加
        jedis.zadd("bookList",500,"斗破苍穹");
        jedis.zadd("bookList",480,"坏蛋是怎样练成的");
        jedis.zadd("bookList",600,"圣墟");
        jedis.zadd("bookList",300,"诛仙");

        //2.2 获取圣墟 在整个排行榜中是排名第几的:
        Long rank = jedis.zrevrank("bookList", "圣墟");  //从大到小
        //Long rank =  jedis.zrank("bookList", "圣墟");  // 从小到大
        System.out.println(rank);  // 0  表示就是第一位

        //2.3 获取整个排行榜的数据(从大到小)
        Set<String> set = jedis.zrevrange("bookList", 0, -1);
        System.out.println(set);
        System.out.println("--------------");

        // 2.4 获取整个排行榜的数据(从大到小) 并且包含得分数据
        Set<Tuple> bookList = jedis.zrevrangeWithScores("bookList", 0, -1);
        for (Tuple tuple : bookList) {
            String element = tuple.getElement();
            double score = tuple.getScore();

            System.out.println("书籍的名称为:"+ element +"; 点击量:"+score);

        }
        //3. 释放资源
        jedis.close();
    }


    //jedis的连接池
    @Test
    public void jedisPoolTest07(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100); // 最大的连接数
        jedisPoolConfig.setMinIdle(25);  // 最小的闲时的数量
        jedisPoolConfig.setMaxIdle(50);  // 最大的闲时的数量

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.72.142", 6379);

        //获取连接对象: jedis对象
        Jedis jedis = jedisPool.getResource();
        String ping = jedis.ping();
        System.out.println(ping);

        //释放资源(归还连接)
        jedis.close();
    }

    //测试工具类
    @Test
    public void testUtils(){
        Jedis jedis = JedisPoolUtils.getJedis();
        String ping = jedis.ping();
        System.out.println(ping);
        jedis.close();
    }

}
