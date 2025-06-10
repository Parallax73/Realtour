package br.com.realtour.entity;


import br.com.realtour.util.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    public String message;
    public Sender sender;
    public Date sended;
}
