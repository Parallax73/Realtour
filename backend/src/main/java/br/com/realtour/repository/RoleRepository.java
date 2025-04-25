package br.com.realtour.repository;


import br.com.realtour.entity.Role;
import br.com.realtour.util.UserRoles;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByRole(UserRoles Role);
}
