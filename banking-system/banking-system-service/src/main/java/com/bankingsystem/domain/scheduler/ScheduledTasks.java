package com.bankingsystem.domain.scheduler;

import com.bankingsystem.domain.service.CheckingAccountService;
import com.bankingsystem.domain.service.JointCheckingAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final CheckingAccountService checkingAccountService;
    private final JointCheckingAccountService jointCheckingAccountService;

    public ScheduledTasks(CheckingAccountService checkingAccountService, JointCheckingAccountService jointCheckingAccountService) {
        this.checkingAccountService = checkingAccountService;
        this.jointCheckingAccountService = jointCheckingAccountService;
    }

//    @Scheduled(cron = "0 0 2 * * ?") // Run daily at 2 AM
    @Scheduled(fixedRate = 120000, initialDelay = 60000) // Run every 2 minutes with initial delay of 1 minute
    @Transactional
    public void applyInterestToOverdrawnAccountsTask() {
        logger.info("Applying interest to overdrawn Checking Accounts");
        checkingAccountService.applyInterestToOverdrawnAccounts();
        logger.info("Applying interest to overdrawn Joint Checking Accounts");
        jointCheckingAccountService.applyInterestToOverdrawnAccounts();
    }
}
