package br.com.realtour.entity;

import br.com.realtour.util.RegisterRealtorDTO;
import br.com.realtour.util.Sender;
import br.com.realtour.util.UserRoles;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Realtor implements Sender {

    @Id
    private String id;
    private String email;
    private String username;
    private String password;
    private String creci;
    private Set<Role> roles = new HashSet<>();
    private List<Unit> ownUnits;



    public Realtor(@NotNull RegisterRealtorDTO dto){
        this.email = dto.email();
        this.username = dto.username();
        this.creci = dto.creci();
        this.ownUnits = new ArrayList<>();
        roles.add(new Role(UserRoles.ROLE_USER));
    }

    public void addUnits(Unit unit){
        this.ownUnits.add(unit);
    }

}
