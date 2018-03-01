import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class JedisTest {

    @Test
    public void testJedis(){
        //创建一个连接jedis的连接对象，参数：host,port
        Jedis jedis = new Jedis("192.168.25.129", 6379);
        //直接是用jdeis操作redis,所有jedis的命令都对应一个方法
        jedis.set("test123","my first jedis test");
        String test123 = jedis.get("test123");
        System.out.println(test123);
        //关闭连接
        jedis.close();
    }

    @Test
    public void testJedisPool(){
        //创建一个连接jedis的连接池对象，参数：host,port
        JedisPool jedisPool = new JedisPool("192.168.25.129", 6379);
        //从连接池获得一个连接对象，就是一个jedis对象
        Jedis resource = jedisPool.getResource();
        //使用jedis操作redis
        String str = resource.get("test123");
        System.out.println(str);
        //关闭连接，每次使用完毕关闭连接，连接池回收资源，连接为单例模式
        resource.close();
        //关闭连接池
        jedisPool.close();
    }

    @Test
    public void testJedisCluster(){
        //创建一个jedisCluster对象，有一个参数nodes是set类型，set中包含若干个HostAndPort对象
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.25.129", 7001));
        nodes.add(new HostAndPort("192.168.25.129", 7002));
        nodes.add(new HostAndPort("192.168.25.129", 7003));
        nodes.add(new HostAndPort("192.168.25.129", 7004));
        nodes.add(new HostAndPort("192.168.25.129", 7005));
        nodes.add(new HostAndPort("192.168.25.129", 7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //直接使用jedisCluster对象操作redis
        jedisCluster.set("test","123");
        String test = jedisCluster.get("test");
        System.out.println(test);
        //关闭jedisCluster对象
        jedisCluster.close();
    }
}
