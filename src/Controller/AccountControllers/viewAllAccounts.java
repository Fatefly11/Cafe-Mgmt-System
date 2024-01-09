package Controller.AccountControllers;

import Entity.Account;

import java.util.List;

public class viewAllAccounts {
    public List<Account> viewAllAccounts(){
        List<Account> accounts;
        Account accountEntity = new Account();
        accounts = accountEntity.viewAllAccounts();
        return accounts;
    }
}
