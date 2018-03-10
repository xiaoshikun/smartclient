/**
 * @author xiaos
 * @create 2018-03-09-19:49
 */
public class CharSubTest {

    public static void main(String[] args) {
        String str = "http://10.1.5.219//icon/2018/03/09/i201803090717156110.png";

        System.out.println(str.lastIndexOf("icon/"));
        System.out.println(str.startsWith("icon/"));
        System.out.println(str.substring(str.lastIndexOf("icon/")));
        int i = str.lastIndexOf("/i");
        System.out.println(i);
        String s = str.substring((str.lastIndexOf("icon/")+5),str.lastIndexOf("/i"));
        System.out.println(s);

    }
}
