package Controller.AccountControllers;

import Entity.Account;

public class getUserIdByUsername {
    public int getUserIdByUsername(String username){
        Account accountEntity = new Account();
        return accountEntity.getUserIdByUsername(username);
    }
}
