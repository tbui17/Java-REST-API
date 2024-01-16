package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Optional;

import ErrorHandler.ErrorHandler;
import Model.Account;
import Util.ConnectionUtil;


public class AccountDAO {
    Connection connection;
    ErrorHandler errorHandler;

    public AccountDAO(){
        this.connection = ConnectionUtil.getConnection();
        this.errorHandler = ErrorHandler.getInstance();
    }

    public Optional<Account> addAccount(Account account){
        
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            
            

            pstmt.executeUpdate();
            ResultSet result = pstmt.getGeneratedKeys();
            if(result.next()){
                account.account_id = result.getInt(1);
                return Optional.of(account);
            }
            return Optional.empty();
        } catch (Exception e) {
            errorHandler.handle(e);
            return Optional.empty();
        }
        
            
            
        
    }

    public Optional<Account> getAccount(Account account){
        try {
            String sql = "SELECT * FROM account WHERE username = ? LIMIT 1";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, account.getUsername());
            ResultSet result = pstmt.executeQuery();
            if(!result.next()){
                return Optional.empty();
            }       
            Account dbAccount =  new Account(
                result.getInt(1),
                result.getString(2),
                result.getString(3)
                );
            return Optional.of(dbAccount);
        } catch (Exception e) {
            errorHandler.handle(e);
            return Optional.empty();
        }
    }

    public Optional<Account> getAccount(Integer accountId){
        try {
            String sql = "SELECT * FROM account WHERE account_id = ? LIMIT 1";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            ResultSet result = pstmt.executeQuery();
            if(!result.next()){
                return Optional.empty();
            }       
            Account dbAccount =  new Account(
                result.getInt(1),
                result.getString(2),
                result.getString(3)
                );
            return Optional.of(dbAccount);
        } catch (Exception e) {
            errorHandler.handle(e);
            return Optional.empty();
        }
    }

    
    
    


    
}
