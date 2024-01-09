package Controller.AuthControllers;

import Entity.Account;

public class managerLogin {
    public String managerLogin(String enteredUsername, String enteredPassword, int enteredPId){
        Account accountEntity = new Account();
        String result = accountEntity.managerLogin(enteredUsername, enteredPassword, enteredPId);
        return result;
    }
}
