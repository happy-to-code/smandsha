package chain.tj.util;

import chain.tj.common.StatusCode;
import chain.tj.common.exception.ServiceException;
import chain.tj.model.dto.TransactionDto;
import chain.tj.model.dto.TransactionHeaderDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static chain.tj.util.GmUtils.sm2Sign;
import static chain.tj.util.GmUtils.sm3Hash;
import static chain.tj.util.PeerUtil.int2Bytes;
import static chain.tj.util.PeerUtil.long2Bytes;

/**
 * @Describe:
 * @Author: zhangyifei
 * @Date: 2020/5/18 16:50
 */
public class TransactionUtil {
    /**
     * 获取基础TransactionDto
     *
     * @param currentTime
     * @return
     */
    public static TransactionDto getTransactionDto(long currentTime) {
        // 创建返回对象
        TransactionDto transactionDto = new TransactionDto();
        // 创建头对象
        TransactionHeaderDto transactionHeaderDto = new TransactionHeaderDto();
        transactionHeaderDto.setTimestamp(currentTime);
        // 默认版本
        transactionHeaderDto.setVersion(0);
        transactionHeaderDto.setType(4);
        transactionHeaderDto.setSubType(0);

        transactionDto.setTransactionHeader(transactionHeaderDto);
        return transactionDto;
    }

    /**
     * 转换buf
     *
     * @param buf
     * @return
     */
    public static byte[] convertBuf(ByteBuf buf) {
        byte[] bytes1 = new byte[buf.writerIndex()];

        byte[] array = buf.array();
        for (int i = 0; i < bytes1.length; i++) {
            bytes1[i] = array[i];
        }
        return bytes1;
    }

    /**
     * 序列化transactionDto
     *
     * @param transactionDto
     * @return
     */
    public static byte[] serialTransactionDto(TransactionDto transactionDto) {
        ByteBuf buf = Unpooled.buffer();
        if (null != transactionDto.getTransactionHeader()) {
            if (null != transactionDto.getTransactionHeader().getVersion()) {
                buf.writeBytes(int2Bytes(transactionDto.getTransactionHeader().getVersion()));
            }
            if (null != transactionDto.getTransactionHeader().getType()) {
                buf.writeBytes(int2Bytes(transactionDto.getTransactionHeader().getType()));
            }
            if (null != transactionDto.getTransactionHeader().getSubType()) {
                buf.writeBytes(int2Bytes(transactionDto.getTransactionHeader().getSubType()));
            }
            if (null != transactionDto.getTransactionHeader().getTimestamp()) {
                buf.writeBytes(long2Bytes(transactionDto.getTransactionHeader().getTimestamp()));
            }
        }

        if (null != transactionDto.getData()) {
            buf.writeBytes(int2Bytes(transactionDto.getData().length));
            buf.writeBytes(transactionDto.getData());
        } else {
            buf.writeInt(0);
        }

        if (null != transactionDto.getExtra()) {
            buf.writeBytes(int2Bytes(transactionDto.getExtra().length));
            buf.writeBytes(transactionDto.getExtra());
        } else {
            buf.writeInt(0);
        }

        if (null != transactionDto.getPubKey()) {
            buf.writeBytes(int2Bytes(transactionDto.getPubKey().length));
            buf.writeBytes(transactionDto.getPubKey());
        } else {
            buf.writeInt(0);
        }
        buf.writeInt(0);

        byte[] bytesReturn = new byte[buf.writerIndex()];

        byte[] array = buf.array();
        for (int i = 0; i < bytesReturn.length; i++) {
            bytesReturn[i] = array[i];
        }
        return bytesReturn;
    }


    /**
     * 给TransactionDto对象赋值
     *
     * @param transactionDto
     */
    public static void setValueForTransactionDto(TransactionDto transactionDto, Map<String, byte[]> keyPairAndSign) {
        // 序列化transactionDto
        byte[] transactionDtoBytes = serialTransactionDto(transactionDto);

        // sm3加密
        byte[] hashVal = sm3Hash(transactionDtoBytes);

        byte[] priKeyBytes = keyPairAndSign.get("priKey");

        byte[] signBytes = new byte[0];
        try {
            signBytes = sm2Sign(priKeyBytes, transactionDtoBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        transactionDto.getTransactionHeader().setTransactionHash(hashVal);
        transactionDto.setSign(signBytes);
    }

    /**
     * 获取交易基本对象
     *
     * @param newTxQueryDto
     * @return
     */
    // public static BasicTxObj getBasicTxObj(NewTxQueryDto newTxQueryDto) {
    //     // 验证参数
    //     if (StringUtils.isBlank(newTxQueryDto.getAddr())) {
    //         throw new ServiceException(StatusCode.CLIENT_410001.value(), "ip地址不可以为空！");
    //     }
    //     if (null == newTxQueryDto.getRpcPort() || newTxQueryDto.getRpcPort() <= 0) {
    //         throw new ServiceException(StatusCode.CLIENT_4100301.value(), "端口不可以为空，并且要大于0");
    //     }
    //     if (StringUtils.isBlank(newTxQueryDto.getPubKeyPath())) {
    //         throw new ServiceException(StatusCode.CLIENT_410001.value(), "公钥地址不可以为空！");
    //     }
    //     if (StringUtils.isBlank(newTxQueryDto.getPriKeyPath())) {
    //         throw new ServiceException(StatusCode.CLIENT_410001.value(), "私钥地址不可以为空！");
    //     }
    //
    //
    //     // 读取PubKey
    //     String pubkeyStr = readFile(newTxQueryDto.getPubKeyPath());
    //     if (StringUtils.isBlank(pubkeyStr)) {
    //         throw new ServiceException("获取公钥失败或者公钥文件内容为空！");
    //     }
    //     // 将16进制的pubKey转换成ByteString
    //     ByteString pubKey = convertPubKeyToByteString(pubkeyStr);
    //
    //     // 获取grpc连接stub对象
    //     PeerGrpc.PeerBlockingStub stub = getStubByIpAndPort(newTxQueryDto.getAddr(), newTxQueryDto.getRpcPort());
    //
    //     BasicTxObj obj = new BasicTxObj();
    //     obj.setPubKey(pubKey);
    //     obj.setStub(stub);
    //     obj.setCurrentTime(System.currentTimeMillis() / 1000);
    //
    //     return obj;
    // }

    /**
     * 验证参数
     *
     * @param addr
     * @param rpcPort
     * @param pubKeyPath
     */
    public static void checkParam(String addr, Integer rpcPort, String pubKeyPath) {

        if (StringUtils.isBlank(addr)) {
            throw new ServiceException(StatusCode.CLIENT_410001.value(), "ip地址不可以为空！");
        }
        if (null == rpcPort || rpcPort <= 0) {
            throw new ServiceException(StatusCode.CLIENT_4100301.value(), "端口不可以为空，并且要大于0");
        }
        if (StringUtils.isBlank(pubKeyPath)) {
            throw new ServiceException(StatusCode.CLIENT_410001.value(), "公钥地址不可以为空！");
        }
    }


}
