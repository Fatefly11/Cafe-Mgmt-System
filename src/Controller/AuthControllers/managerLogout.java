package Controller.AuthControllers;

import Entity.Account;

public class managerLogout {
    public String managerLogout(){
        Account accountEntity = new Account();
        String result = accountEntity.managerLogout();
        return result;
    }
}
