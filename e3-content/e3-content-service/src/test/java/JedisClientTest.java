import cn.e3mall.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisClientTest {

    @Test
    public void testjedisClient(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("mytest","jedisClientTest");
        String str = jedisClient.get("mytest");
        System.out.println(str);
    }
}
