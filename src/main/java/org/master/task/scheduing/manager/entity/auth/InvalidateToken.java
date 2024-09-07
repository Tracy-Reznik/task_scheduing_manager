package org.master.task.scheduing.manager.entity.auth;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "token"))
public class InvalidateToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID tokenId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date invalidateTime;

    public InvalidateToken() {
    }

    public InvalidateToken(String token, Date invalidateTime) {
        this.token = token;
        this.invalidateTime = invalidateTime;
    }

    // Getters and Setters
    public UUID getTokenId() {
        return tokenId;
    }

    public void setTokenId(UUID tokenId) {
        this.tokenId = tokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getInvalidateTime() {
        return invalidateTime;
    }

    public void setInvalidateTime(Date invalidateTime) {
        this.invalidateTime = invalidateTime;
    }
}

