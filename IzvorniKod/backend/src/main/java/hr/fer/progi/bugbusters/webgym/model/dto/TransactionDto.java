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
}
