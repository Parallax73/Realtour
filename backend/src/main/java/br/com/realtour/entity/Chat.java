package br.com.realtour.entity;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "chats")
public class Chat {
    @Id
    private String id;
    @DBRef
    private Client client;
    @DBRef
    private Realtor realtor;
    @DBRef
    private Unit unit;
    private List<Message> messages;

}