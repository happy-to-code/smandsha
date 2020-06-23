package chain.tj.demo;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileOutputStream;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/23 15:00
 * @description：
 * @modified By：
 * @version:
 */
public class MyGmUtil {
    public static void main(String[] args) throws Exception {
        System.out.println("----------生成秘钥对start----------------");
        // 引入BC库
        Security.addProvider(new BouncyCastleProvider());
        // 获取SM2椭圆曲线的参数
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        // 获取一个椭圆曲线类型的密钥对生成器
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        // 使用SM2参数初始化生成器
        kpg.initialize(sm2Spec);
        // 使用SM2的算法区域初始化密钥生成器
        kpg.initialize(sm2Spec, new SecureRandom());
        // 获取密钥对
        KeyPair keyPair = kpg.generateKeyPair();

        System.out.println("公钥串："+new BASE64Encoder().encode(keyPair.getPublic().getEncoded()));
        System.out.println("私钥串："+new BASE64Encoder().encode(keyPair.getPrivate().getEncoded()));

        // 生成公钥
        generatePubKey(new BASE64Encoder().encode(keyPair.getPublic().getEncoded()));
        // 生成私钥文件
        generatePrivateKey(new BASE64Encoder().encode(keyPair.getPrivate().getEncoded()));
        System.out.println("----------生成秘钥对end----------------");
    }

    /**
     * 生成私钥文件
     *
     * @param content
     */
    public static void generatePrivateKey(String content) {
        // 文件名称
        String fileName = "priKey.pem";
        // 文件路径+文件名称
        String strPath = System.getProperty("user.dir") + "\\src\\main\\java\\chain\\tj\\demo\\" + fileName;

        // 待写入字符串
        String sourceString = "-----BEGIN PRIVATE KEY-----\n" + content + "\n-----END PRIVATE KEY-----";
        byte[] sourceByte = sourceString.getBytes();
        writeDataToFile(strPath, sourceByte);
    }

    /**
     * 生成公钥文件
     *
     * @param content
     */
    public static void generatePubKey(String content) {
        // 文件名称
        String fileName = "pubKey.pem";
        // 文件路径+文件名称
        String strPath = System.getProperty("user.dir") + "\\src\\main\\java\\chain\\tj\\demo\\" + fileName;

        // 待写入字符串
        String sourceString = "-----BEGIN PUBLIC KEY-----\n" + content + "\n-----END PUBLIC KEY-----";
        byte[] sourceByte = sourceString.getBytes();
        writeDataToFile(strPath, sourceByte);
    }

    /**
     * 向文件中写数据
     *
     * @param strPath
     * @param sourceByte
     */
    private static void writeDataToFile(String strPath, byte[] sourceByte) {
        if (null != sourceByte) {
            try {
                File file = new File(strPath);        //文件路径（路径+文件名）
                if (!file.exists()) {    //文件不存在则创建文件，先创建目录
                    File dir = new File(file.getParent());
                    dir.mkdirs();
                    file.createNewFile();
                }
                FileOutputStream outStream = new FileOutputStream(file);    //文件输出流用于将数据写入文件
                outStream.write(sourceByte);
                outStream.close();    //关闭文件输出流
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
