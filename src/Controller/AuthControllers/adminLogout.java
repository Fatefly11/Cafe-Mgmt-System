package Controller.AuthControllers;

import Entity.Account;

public class adminLogout {
    public String adminLogout(){
        Account accountEntity = new Account();
        String result = accountEntity.adminLogout();
        return result;
    }
}
