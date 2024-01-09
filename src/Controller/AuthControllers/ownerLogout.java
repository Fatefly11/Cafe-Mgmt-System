package Controller.AuthControllers;

import Entity.Account;

public class ownerLogout {
    public String ownerLogout(){
        Account accountEntity = new Account();
        String result = accountEntity.ownerLogout();
        return result;
    }
}
