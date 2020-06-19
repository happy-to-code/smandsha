package chain.tj.model.dto;

import lombok.Data;

@Data
public class TransactionHeaderDto {
    private Integer version;
    private Integer type;
    private Integer subType;
    private Long timestamp;
    private byte[] transactionHash;
}