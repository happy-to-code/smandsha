package chain.tj.util;

import chain.tj.model.domain.FileExtInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.StringUtils;

import static chain.tj.util.PeerUtil.*;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 15:02
 * @description：
 * @modified By：
 * @version:
 */
public class FileDtoSerial {

    public static byte[] serialFileDto(FileExtInfo fileExtInfo) {
        ByteBuf buf = Unpooled.buffer();

        if (null != fileExtInfo.getFileByte()) {
            buf.writeBytes(int2Bytes(fileExtInfo.getFileByte().length));
            buf.writeBytes(fileExtInfo.getFileByte());
        } else {
            buf.writeInt(0);
        }

        if (StringUtils.isNoneBlank(fileExtInfo.getExtName())) {
            buf.writeBytes(int2Bytes(fileExtInfo.getExtName().getBytes().length));
            buf.writeBytes(fileExtInfo.getExtName().getBytes());
        } else {
            buf.writeInt(0);
        }

        if (StringUtils.isNoneBlank(fileExtInfo.getModTime())) {
            buf.writeBytes(int2Bytes(fileExtInfo.getModTime().getBytes().length));
            buf.writeBytes(fileExtInfo.getModTime().getBytes());
        } else {
            buf.writeInt(0);
        }

        if (StringUtils.isNoneBlank(fileExtInfo.getCreateTime())) {
            buf.writeBytes(int2Bytes(fileExtInfo.getCreateTime().getBytes().length));
            buf.writeBytes(fileExtInfo.getCreateTime().getBytes());
        } else {
            buf.writeInt(0);
        }

        if (null != fileExtInfo.getFileSizes()) {
            buf.writeBytes(longToBytes(fileExtInfo.getFileSizes()));
        } else {
            buf.writeInt(0);
        }

        return convertBuf(buf);
    }
}
