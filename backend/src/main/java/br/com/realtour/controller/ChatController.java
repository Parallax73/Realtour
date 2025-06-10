package br.com.realtour.controller;

import br.com.realtour.service.ChatService;
import br.com.realtour.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatController {

    @Autowired
    ChatService chatService;




    @PostMapping("/create-chat")
    public ResponseEntity<?> createChat(@RequestHeader("Authorization") String token, @RequestHeader("username") String realtorName){
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.chatService.createChat(token,realtorName));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestHeader("Authorization") String token, @RequestHeader("message") String message, @RequestHeader("chatId") String chatId){
        log.debug("CALLEDDD");
        log.info(message + "ASDASDASD");
        try{
            this.chatService.sendMessage(token,chatId,message);
            return ResponseEntity.status(HttpStatus.OK).body("Message created successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
