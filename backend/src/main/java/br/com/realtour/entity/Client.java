package br.com.realtour.entity;

import br.com.realtour.util.RegisterClientDTO;
import br.com.realtour.util.Sender;
import br.com.realtour.util.UserRoles;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Getter
@Setter
public class Client implements Sender {

    @Id
    private String id;
    private String email;
    private String username;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private List<Unit> savedUnits;


    public Client(@NotNull RegisterClientDTO dto){
        this.username = dto.username();
        this.email = dto.email();
        this.savedUnits = new ArrayList<>();
        roles.add(new Role(UserRoles.ROLE_USER));
    }

    public void addUnits(Unit unit){
        this.savedUnits.add(unit);
    }
}
