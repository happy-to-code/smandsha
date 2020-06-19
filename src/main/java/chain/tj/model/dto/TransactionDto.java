package chain.tj.model.dto;

import lombok.Data;

@Data
public class TransactionDto {

    private TransactionHeaderDto transactionHeader;
    private byte[] data;
    private byte[] pubKey;
    private byte[] sign;
    private byte[] result;
    private byte[] extra;
}