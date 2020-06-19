package chain.tj.util;

import chain.tj.model.domain.FileExtInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 14:01
 * @description：
 * @modified By：
 * @version:
 */
public class FileUtil {

    public static void main(String[] args) {
        FileExtInfo fileExtInfo = getFileExtInfo("E:\\200617workproject\\java\\src\\main\\java\\chain\\tj\\file\\test.txt");
        System.out.println("fileExtInfo = " + fileExtInfo);

        // String s = JSON.toJSONString(fileExtInfo);
        // System.out.println("s = " + s);
        // byte[] bytes = s.getBytes();
        // System.out.println("bytes = " + bytes);
        //
        // byte[] bytes1 = sm3Hash(bytes);
        // String s1 = toHexString(bytes1);
        // System.out.println("s1 = " + s1);
    }

    /**
     * 获取文件扩展信息
     *
     * @param filePath
     * @return
     */
    public static FileExtInfo getFileExtInfo(String filePath) {
        FileExtInfo fileExtInfo = new FileExtInfo();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 1获取文件的修改时间
        File f = new File(filePath);
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        cal.setTimeInMillis(time);

        fileExtInfo.setModTime(formatter.format(cal.getTime()));

        // 2获取文件的创建时间
        BasicFileAttributes attrs;
        String createTime = null;
        try {
            attrs = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
            FileTime fileTime = attrs.creationTime();
            createTime = formatter.format(new Date(fileTime.toMillis()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileExtInfo.setCreateTime(createTime);

        // 3文件大小
        fileExtInfo.setFileSizes(getFileSizes(f));

        // 4读取文件内容
        String fileStr = readFile(filePath);
        fileExtInfo.setFileByte(fileStr.getBytes());

        // 4获取文件的扩展名
        String fileName = f.getName();
        String fileExtName = fileName.substring(fileName.lastIndexOf("."));
        fileExtInfo.setExtName(fileExtName);

        return fileExtInfo;

    }

    /**
     * 根据路径读取文件
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
            String lineTxt;
            while ((lineTxt = bfr.readLine()) != null) {
                result.append(lineTxt).append("\n");
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String toString = result.toString();
        String substring = toString.substring(0, toString.lastIndexOf("\n"));
        return substring;
    }


    /**
     * 获取文件修改时间
     *
     * @param filepath 文件路径
     * @return String 文件修改时间
     * @Title: getFileTime
     * @author projectNo
     */
    public static String getFileTime(String filepath) {
        File f = new File(filepath);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

    /**
     * 获取文件大小
     *
     * @param file File
     * @return String 转换后的文件大小
     * @Title: getFileSizes
     * @author projectNo
     */

    private static long getFileSizes(File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (File child : children)
                total += getFileSizes(child);
        return total;
    }

    /**
     * 递归获取目录大小
     *
     * @param f File
     * @return long 目录大小
     * @throws Exception
     * @Title: getFileSize
     * @author projectNo
     */
    public long getFileSize(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS long文件大小值
     * @return String 文件大小
     * @Title: FormetFileSize
     * @author projectNo
     */
    public String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat d = new DecimalFormat("#");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = d.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }


}
