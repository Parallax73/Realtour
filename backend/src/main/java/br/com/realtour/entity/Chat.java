package br.com.realtour.entity;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "chats")
public class Chat {
    @Id
    private String id;
    private String clientEmail;
    private String clientName;
    private String realtorEmail;
    private String realtorName;
    private String unitId;
    private String unitAddress;
    private String unitNumber;
    private List<Message> messages;
}