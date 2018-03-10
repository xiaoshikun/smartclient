import redis.clients.jedis.Jedis;

/**
 * @author xiaos
 * @create 2018-02-05-11:18
 */
public class RedisTest {

    public static void main(String[] args){
       //
       //  Jedis jedis = new Jedis("192.168.80.92",6379);
         Jedis jedis = new Jedis("10.1.5.219",6379);
         jedis.auth("111111");

         System.out.println(jedis.ping());

         //jedis.set("hello","wolrd");
        String str=  jedis.get("k1");

        System.out.println(str);
         jedis.close();

    }
}
