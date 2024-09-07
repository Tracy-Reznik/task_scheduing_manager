package org.master.task.scheduing.manager.dao.impl.auth;


import org.master.task.scheduing.manager.entity.auth.InvalidateToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvalidateTokenRepository extends JpaRepository<InvalidateToken, UUID> {
    Optional<InvalidateToken> findByToken(String token);
    int deleteByInvalidateTimeBefore(Date now);
}