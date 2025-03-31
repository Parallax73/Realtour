package br.com.realtour.entity;

import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.UserRoles;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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
    private List<Unit> savedUnits;

    BCryptPasswordEncoder encoder;

    public Client(@NotNull RegisterClientDTO dto){
        this.username = dto.username();
        this.password = encoder.encode(dto.password());
        this.role = UserRoles.CLIENT;
        this.savedUnits = new ArrayList<>();
    }
}
