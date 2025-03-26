package br.com.realtour.entity;

import br.com.realtour.util.RegisterRealtorDTO;
import br.com.realtour.util.UserRoles;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Getter
@Setter
public class Realtor {

    @Id
    private String id;
    private String username;
    private String password;
    private String creci;
    private UserRoles role;

    public Realtor(@NotNull RegisterRealtorDTO dto){
        this.username = dto.username();
        this.password = dto.password();
        this.creci = dto.creci();
        this.role = UserRoles.REALTOR;
    }

}
