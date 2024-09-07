package org.master.task.scheduing.manager.service;

import jakarta.annotation.Resource;

import org.master.task.scheduing.manager.dao.impl.auth.InvalidateTokenRepository;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TokenCleanupService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TokenCleanupService.class);
    @Resource
    private InvalidateTokenRepository invalidateTokenRepository;




    // 每半小时执行一次
    @Scheduled(cron = "0 0/30 * * * ?")
    @Transactional
    public void cleanUpExpiredTokens() {
        Date now = new Date();
        int deletedCount = invalidateTokenRepository.deleteByInvalidateTimeBefore(now);
        log.info("Deleted {} expired tokens from the database.", deletedCount);
    }


}
