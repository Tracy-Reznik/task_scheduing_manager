package org.master.task.scheduing.manager.dao.impl.auth;

import org.master.task.scheduing.manager.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
