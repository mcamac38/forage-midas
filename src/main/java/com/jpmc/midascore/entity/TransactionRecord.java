package com.jpmc.midascore.entity;


import jakarta.persistence.*;

/**
 *
 * @author Camacho
 */

@Entity 
public class TransactionRecord {
    
    @Id
    @GeneratedValue()
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserRecord sender;
    
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;
    
    @Column(nullable = false)
    private float amount;
    
    @Column(nullable = false)
    private float incentive;
    
    protected TransactionRecord() {
    }
    
    public TransactionRecord (UserRecord sender, UserRecord recipient, float amount, float incentive){
            this.sender = sender;
            this.recipient = recipient;
            this.amount = amount;
            this.incentive = incentive;
    }
    
    @Override
    public String toString(){
        return String.format("Transaction [id=%d, sender=%s, recipient=%s, amount= %f, incentive=%f ]", id, sender, recipient, amount, incentive);
    }
    
    public long getId(){
        return id;
    }
    
    public UserRecord getSender(){
        return sender;
    }
    
    public UserRecord getRecipient(){
        return recipient;
    }
    
    public float getAmount(){
        return amount;
    }
    
    public float getIncentive() {
        return incentive;
    }
    
    public void setAmount(float amount){
        this.amount = amount;
    }
    
    public void setIncentive(float incentive) {
        this.incentive = incentive;
    }
}
