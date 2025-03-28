package br.com.realtour.entity;

import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.UserRoles;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Getter
@Setter
public class Client {

    @Id
    private String id;
    private String username;
    private String password;
    private UserRoles role;


    public Client(@NotNull RegisterClientDTO dto){
        this.username = dto.username();
        this.password = dto.password();
        this.role = UserRoles.CLIENT;
    }
}
