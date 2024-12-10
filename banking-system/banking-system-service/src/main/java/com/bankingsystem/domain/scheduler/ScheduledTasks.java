package com.bankingsystem.domain.scheduler;

import com.bankingsystem.domain.service.CheckingAccountService;
import com.bankingsystem.domain.service.JointCheckingAccountService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private final CheckingAccountService checkingAccountService;
    private final JointCheckingAccountService jointCheckingAccountService;

    public ScheduledTasks(CheckingAccountService checkingAccountService, JointCheckingAccountService jointCheckingAccountService) {
        this.checkingAccountService = checkingAccountService;
        this.jointCheckingAccountService = jointCheckingAccountService;
    }

    // Scheduled to run daily at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void applyInterestToOverdrawnAccountsTask() {
        checkingAccountService.applyInterestToOverdrawnAccounts();
        jointCheckingAccountService.applyInterestToOverdrawnAccounts();
    }
}
