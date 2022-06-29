package com.userFront.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userFront.dao.SavingsAccountDao;
import com.userFront.domain.SavingsAccount;
import com.userFront.domain.User;
import com.userFront.service.AccountService;
import com.userFront.service.UserService;

@Service
public class AccountServiceImpl implements AccountService {

	private static int nextAccountNumber = 11223147;

	@Autowired
	private SavingsAccountDao savingsAccountDao;

	@Autowired
	private UserService userService;
	

	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountBalance(new BigDecimal(0.0));
		savingsAccount.setAccountNumber(accountGen());

		savingsAccountDao.save(savingsAccount);

		return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
	}
	
	public void deposit(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

        }
    }
    
    public void withdraw(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

        }
    }

	private int accountGen() {
		return ++nextAccountNumber;
	}

}
