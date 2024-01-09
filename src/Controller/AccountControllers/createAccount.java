package Controller.AccountControllers;

import Entity.Account;

public class createAccount {
    public String createNewAccount(String username, String full_name, String password, int p_id, int max_slot){
        Account accountEntity = new Account();
        String result = accountEntity.createNewAccount(username, full_name, password, p_id, max_slot);
        System.out.println(result);
        return result;
    }
}
