package Controller.AuthControllers;

import Entity.Account;

public class staffLogin {
    public String staffLogin(String enteredUsername, String enteredPassword){
        Account accountEntity = new Account();
        String result = accountEntity.staffLogin(enteredUsername, enteredPassword);
        return result;
    }
}
