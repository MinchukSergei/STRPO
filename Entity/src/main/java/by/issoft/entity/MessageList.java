package by.issoft.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "message_list", indexes = {
        @Index(name = "fk_message_receiver_user_data_idx", columnList = "fk_message_receiver"),
        @Index(name = "fk_message_sender_user_data_idx", columnList = "fk_message_sender"),
        @Index(name = "fk_message_user_message_idx", columnList = "fk_message")
})
public class MessageList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_message_sender", nullable = false)
    private UserData messageSender;

    @ManyToOne
    @JoinColumn(name = "fk_message_receiver", nullable = false)
    private UserData messageReceiver;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_message", nullable = false)
    private UserMessage message;

    @Column(name = "message_timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar messageTime;

    //region Getters And Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(UserData messageSender) {
        this.messageSender = messageSender;
    }

    public UserData getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(UserData messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public UserMessage getMessage() {
        return message;
    }

    public void setMessage(UserMessage message) {
        this.message = message;
    }

    public Calendar getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Calendar messageTime) {
        this.messageTime = messageTime;
    }

    //endregion
}
