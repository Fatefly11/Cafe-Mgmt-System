package Controller.AccountControllers;

import Entity.Account;

public class ownerViewAccount {
    public Account viewOneAccount(int user_id){
        Account accountEntity = new Account();
        Account account;
        account = accountEntity.viewOneAccount(user_id);
        return account;
    }
}
