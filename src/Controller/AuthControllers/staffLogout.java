package Controller.AuthControllers;

import Entity.Account;

public class staffLogout {
    public String staffLogout(){
        Account accountEntity = new Account();
        String result = accountEntity.staffLogout();
        return result;
    }
}
