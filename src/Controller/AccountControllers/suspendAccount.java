package Controller.AccountControllers;

import Entity.Account;

public class suspendAccount {
    public String suspendOneAccount(int user_id){
        Account accountEntity = new Account();
        String result = accountEntity.suspendOneAccount(user_id);
        System.out.println(result);
        return result;
    }
}
