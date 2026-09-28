package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.entity.TransactionRecord;
/**
 *
 * @author Camacho
 */
@Component
public class TransactionListener {
    private final DatabaseConduit databaseConduit;
    private final IncentiveQuerier incentiveQuerier;
    
    public TransactionListener(DatabaseConduit databaseConduit, IncentiveQuerier incentiveQuerier) {
        this.databaseConduit = databaseConduit;
        this.incentiveQuerier = incentiveQuerier;
    }
    
    @KafkaListener(groupId = "midas-core", topics = "${general.kafka-topic}")
    public void listen(Transaction data) {
        float amount = data.getAmount();
        long senderId = data.getSenderId();
        long recipientId = data.getRecipientId();
        
        UserRecord sender = databaseConduit.findUserById(senderId);
        UserRecord recipient = databaseConduit.findUserById(recipientId);
        
      
        if (sender == null || recipient == null) {
            return;
        }
        
        if (sender.getBalance() < amount) {
            return;
        }
        
        //This incentive is envoked AFTER validation, if validation fails incentive never needs to be called
        
        Incentive incentive = incentiveQuerier.query(data);
        float incentiveAmount = incentive.getAmount();
        
        float newSenderBalance = sender.getBalance() - amount;
        float newRecipientBalance = recipient.getBalance() + amount + incentiveAmount;
        
        sender.setBalance(newSenderBalance);
        recipient.setBalance(newRecipientBalance);
        
        databaseConduit.save(sender);
        databaseConduit.save(recipient);
    
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, amount, incentiveAmount);
        databaseConduit.save(transactionRecord);
        
    }
    

}
