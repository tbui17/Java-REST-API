package Service;


import java.util.Optional;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {
    private AccountDAO dao;
    
    public AccountService(){
        this.dao = new AccountDAO();
    }

    public Optional<Account> getAccount(Account account){
        return dao.getAccount(account);
    }
    public Optional<Account> getAccount(Integer accountId){
        return dao.getAccount(accountId);
    }

    public Optional<Account> register(Account account){
        
        if (!isValidUsername(account)){
            return Optional.empty();
        }
        if (!isValidPassword(account)){
            return Optional.empty();
        }
        return dao.addAccount(account);
        
        
        
    }

    public Optional<Account> login(Account account) {
        
        Optional<Account> res = dao.getAccount(account);
        
        if (res.isEmpty() || !isValidCredentials(account, res.get())){
            return Optional.empty();
        }
        return res;
    }

    public boolean checkAccountExists(Integer accountId){
        if(dao.getAccount(accountId).isEmpty()){
            return false;
        }
        return true;
    }

    protected boolean isValidUsername(Account account) {
        
        if (account.getUsername() == ""){
            return false;
        }
        if (dao.getAccount(account).isPresent()){
            return false;
        }
        return true;
    }


    protected boolean isValidPassword(Account account){
        if (account.getPassword().length() <4){
            return false;
        }
        return true;
    }

    protected boolean isValidCredentials(Account inboundAccount, Account databaseAccount){
        if (!inboundAccount.username.equals(databaseAccount.username) || !inboundAccount.password.equals(databaseAccount.password)){
            return false;
        }
        return true;
    }

    }



