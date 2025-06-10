package br.com.realtour.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {

    @Id
    public String id;
    Realtor realtor;
    Client client;
    List<Message> messages = new ArrayList<>();

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
