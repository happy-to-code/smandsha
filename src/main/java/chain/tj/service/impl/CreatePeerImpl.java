package chain.tj.service.impl;

import chain.tj.common.response.RestResponse;
import chain.tj.model.proto.PeerGrpc;
import chain.tj.service.CreatePeer;

import static chain.tj.util.PeerUtil.getStubByIpAndPort;

/**
 * @author ：zhangyifei
 * @date ：Created in 2020/6/17 19:35
 * @description：
 * @modified By：
 * @version:
 */
public class CreatePeerImpl implements CreatePeer {

    @Override
    public RestResponse createPeer() {

        PeerGrpc.PeerBlockingStub stub = getStubByIpAndPort("10.1.3.150", 9008);

        // MyTransaction.TransactionHeader transactionHeader = MyTransaction.TransactionHeader.newBuilder()
        //         .setVersion(0)
        //         .setType(0)
        //         .setSubType(0)
        //         .setTimestamp(System.currentTimeMillis() / 1000)
        //         .setTransactionHash(ByteString.copyFrom(transactionDto.getTransactionHeader().getTransactionHash()))
        //         .build();
        //
        // MyTransaction.Transaction transaction = MyTransaction.Transaction.newBuilder()
        //         .setHeader(transactionHeader)
        //         .setPubkey()
        //         .setData(ByteString.copyFrom())
        //         .setSign(ByteString.copyFrom())
        //         .build();
        //
        // return MyPeer.PeerRequest.newBuilder()
        //         .setPubkey()
        //         .setPayload(transaction.toByteString())
        //         .build();


        return null;
    }
}
