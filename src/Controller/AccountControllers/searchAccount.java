package Controller.AccountControllers;

import Entity.Account;

import java.util.List;

public class searchAccount {
    public List<Account> searchAccountBySubString(String substring){
        List<Account> accounts;
        Account accountEntity = new Account();
        accounts = accountEntity.searchAccountBySubString(substring);
        return accounts;
    }
}
