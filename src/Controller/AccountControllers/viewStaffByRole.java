package Controller.AccountControllers;

import Entity.Account;

import java.util.List;

public class viewStaffByRole {
    public List<Object> viewStaffByRole(int p_id){
        List<Object> accounts;
        Account accountEntity = new Account();
        accounts = accountEntity.viewStaffByRole(p_id);
        return accounts;
    }
}
