package br.com.realtour.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "chats")
public class Chat {
    @Id
    private String id;
    @DBRef
    private Client client;
    @DBRef
    private Realtor realtor;
    @DBRef
    private Unit unit; // Added reference to the Unit
    private LocalDateTime timestamp;
    private List<Message> messages;
    private MessageType type;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}