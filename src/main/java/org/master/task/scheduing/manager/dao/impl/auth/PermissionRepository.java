package org.master.task.scheduing.manager.dao.impl.auth;



import org.master.task.scheduing.manager.entity.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
