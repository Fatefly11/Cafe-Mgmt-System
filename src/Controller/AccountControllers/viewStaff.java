package Controller.AccountControllers;

import Entity.Account;

import java.util.List;

public class viewStaff {
    public List<Account> viewAllStaff(){
        List<Account> accounts;
        Account accountEntity = new Account();
        accounts = accountEntity.viewAllStaff();
        return accounts;
    }
}
