package hr.fer.progi.bugbusters.webgym.model.dto;

import hr.fer.progi.bugbusters.webgym.model.TransactionType;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionDto {
    private String senderUsername;
    private String receiverUsername;
    private Double amount;
    private Date dateWhen;
    private Long id;
    private TransactionType transactionType;

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDateWhen(Date dateWhen) {
        this.dateWhen = dateWhen;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
