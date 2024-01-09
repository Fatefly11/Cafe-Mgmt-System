package Controller.AuthControllers;

import Entity.Account;

public class adminLogin {
    public String adminLogin(String enteredUsername, String enteredPassword, int enteredPId){
        Account accountEntity = new Account();
        String result = accountEntity.adminLogin(enteredUsername, enteredPassword, enteredPId);
        return result;
    }
}
