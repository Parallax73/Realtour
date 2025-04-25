package br.com.realtour.entity;


import br.com.realtour.util.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private String id;


    private UserRoles role = UserRoles.ROLE_USER;

    public Role(UserRoles erole) {
        this.role = erole;
    }
}
