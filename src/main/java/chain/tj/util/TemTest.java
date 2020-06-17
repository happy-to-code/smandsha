package chain.tj.util;


import static chain.tj.util.FileUtil.readFile;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 13:56
 * @description：
 * @modified By：
 * @version:
 */
public class TemTest {
    public static void main(String[] args) {
        String s = readFile("E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\test.txt");
        System.out.println("s = " + s);
    }
}
