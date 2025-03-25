package br.com.realtour.entity;

import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.UserRoles;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
