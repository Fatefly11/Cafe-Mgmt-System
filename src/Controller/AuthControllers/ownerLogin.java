package Controller.AuthControllers;

import Entity.Account;

public class ownerLogin {
    public String ownerLogin(String enteredUsername, String enteredPassword, int enteredPId){
        Account accountEntity = new Account();
        String result = accountEntity.ownerLogin(enteredUsername, enteredPassword, enteredPId);
        return result;
    }
}
