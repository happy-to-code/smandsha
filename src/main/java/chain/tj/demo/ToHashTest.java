package chain.tj.demo;

import java.io.IOException;

import static chain.tj.util.GmUtils.sm3Hash;
import static chain.tj.util.PeerUtil.toByteArray;
import static chain.tj.util.ShaUtil.*;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/29 15:52
 * @description：
 * @modified By：
 * @version:
 */
public class ToHashTest {
    public static void main(String[] args) throws IOException {
        // 视频文件路径
        String video1Path = "E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\video\\418.mp4";
        String video2Path = "E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\video\\837.mp4";
        // 根据路径读取视频文件
        byte[] video1Bytes = toByteArray(video1Path);
        byte[] video2Bytes = toByteArray(video2Path);

        for (int i = 0; i < 5; i++) {
            // md5计算hash值
            long l1 = System.currentTimeMillis();
            md5Encode(video1Bytes);
            long l2 = System.currentTimeMillis();

            System.out.println("md5 视频1时间 = " + ((41.8 / ((l2 - l1)))*1000));
            long l11 = System.currentTimeMillis();
            md5Encode(video2Bytes);
            long l22 = System.currentTimeMillis();
            System.out.println("md5 视频2时间 = " + (83.7 / ((l22 - l11)))*1000);

            // sha1计算hash值
            long l3 = System.currentTimeMillis();
            sha1Encode(video1Bytes);
            long l4 = System.currentTimeMillis();
            System.out.println("sha1 视频1时间 = " + (41.8 / ((l4 - l3)))*1000);
            long l33 = System.currentTimeMillis();
            sha1Encode(video2Bytes);
            long l44 = System.currentTimeMillis();
            System.out.println("sha1 视频2时间 = " + (83.7 / ((l44 - l33)))*1000);

            // sha256计算hash值
            long l5 = System.currentTimeMillis();
            getSHA256Str(video1Bytes);
            long l6 = System.currentTimeMillis();
            System.out.println("sha256 视频1时间 = " + (41.8 / ((l6 - l5)))*1000);
            long l55 = System.currentTimeMillis();
            getSHA256Str(video2Bytes);
            long l66 = System.currentTimeMillis();
            System.out.println("sha256 视频2时间 = " + (83.7 / ((l66 - l55)))*1000);

            // sm3计算hash值
            long l7 = System.currentTimeMillis();
            sm3Hash(video1Bytes);
            long l8 = System.currentTimeMillis();
            System.out.println("sm3 视频1时间 = " + (41.8 / ((l8 - l7)))*1000);

            long l77 = System.currentTimeMillis();
            sm3Hash(video2Bytes);
            long l88 = System.currentTimeMillis();
            System.out.println("sm3 视频2时间 = " + (83.7 / ((l88 - l77)))*1000);


            // sm3CountHash计算hash值
            long l9 = System.currentTimeMillis();
            sm3CountHash(video1Bytes);
            long l10 = System.currentTimeMillis();
            System.out.println("sm3CountHash 视频1时间 = " + (41.8 / ((l10 - l9)))*1000);

            long l111 = System.currentTimeMillis();
            sm3CountHash(video2Bytes);
            long l122 = System.currentTimeMillis();
            System.out.println("sm3CountHash 视频2时间 = " + (83.7 / ((l122 - l111)))*1000);
        }
    }


}
