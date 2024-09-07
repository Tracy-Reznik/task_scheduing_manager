package org.master.task.scheduing.manager.dao.impl.auth;

import org.master.task.scheduing.manager.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUserName(String username);

}
