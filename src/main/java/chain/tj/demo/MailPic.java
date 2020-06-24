package chain.tj.demo;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/24 14:46
 * @description：
 * @modified By：
 * @version:
 */

public final class MailPic {
    public MailPic() {
    }

    public static void pressText(String pressText, String newImg, String targetImg, int fontStyle, Color color, int fontSize, int startX, int startY) {
        try {
            File file = new File(targetImg);
            Image src = ImageIO.read(file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            g.setColor(color);
            g.setFont(new Font(null, fontStyle, fontSize));
            g.drawString(pressText, startX, startY);
            g.dispose();

            FileOutputStream out = new FileOutputStream(newImg);
            ImageIO.write(image, "GIF", out);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
            System.out.println("image press success");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void main(String[] args) {
        pressText("MAX", "d:\\1.png", "d:\\s.png", Font.BOLD, new Color(0xD2, 0xD2, 0xD2), 22, 85, 100);
        pressText("name", "d:\\1.png", "d:\\1.png", Font.PLAIN, new Color(0x69, 0x69, 0x69), 14, 105, 616);
        pressText("Tel", "d:\\1.png", "d:\\1.png", Font.PLAIN, new Color(0x69, 0x69, 0x69), 14, 85, 639);
        pressText("email", "d:\\1.png", "d:\\1.jpg", Font.PLAIN, new Color(0x69, 0x69, 0x69), 14, 105, 662);
    }
}
