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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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
    private List<Unit> ownUnits;

    BCryptPasswordEncoder encoder;

    public Realtor(@NotNull RegisterRealtorDTO dto){
        this.username = dto.username();
        this.password = encoder.encode(dto.password());
        this.creci = dto.creci();
        this.role = UserRoles.REALTOR;
        this.ownUnits = new ArrayList<>();
    }

}
