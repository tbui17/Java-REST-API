package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import ErrorHandler.ErrorHandler;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    Connection connection;
    ErrorHandler errorHandler;

    public MessageDAO(){
        this.connection = ConnectionUtil.getConnection();
        this.errorHandler = ErrorHandler.getInstance();
    }


    /**
     * @return
     */
    public Optional<Message> getMessage(Integer message_id){
        
        String sql = "SELECT * FROM message WHERE message_id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, message_id);
            ResultSet result = pstmt.executeQuery();
            if(!result.next()){
                return Optional.empty();
            }
            
            
            return Optional.of(constructMessageObject(result));
                
            
        } catch (SQLException e) {
            errorHandler.handle(e);
            return Optional.empty();
        }
        
    }

    

    public ArrayList<Message> getAllMessages(){

        String sql = "SELECT * FROM message";
        ArrayList<Message> messages = new ArrayList<>();
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            ResultSet result = pstmt.executeQuery();
            while(result.next()){
                Message message = constructMessageObject(result);
                messages.add(message);
            }
            return messages;
            
            
                
            
        } catch (SQLException e) {
            errorHandler.handle(e);
            return messages;
        }
    }

    public ArrayList<Message> getUserMessages(Integer account_id) {
        ArrayList<Message> messages = new ArrayList<>();

        String sql = "SELECT * FROM message WHERE POSTED_BY = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, account_id);
            ResultSet result = pstmt.executeQuery();

            while(result.next()){
                Message message = constructMessageObject(result);
                messages.add(message);
            }
            
        } catch (SQLException e) {
            errorHandler.handle(e);
        }

        return messages;
    }



    public Optional<Message> createMessage(Message message){
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
    
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, message.getPosted_by());
            pstmt.setString(2, message.getMessage_text());
            pstmt.setLong(3, message.getTime_posted_epoch());
    
            pstmt.executeUpdate();
            ResultSet result = pstmt.getGeneratedKeys();
            if(result.next()){
                
                Integer key = result.getInt(1);
                message.message_id = key;
                return Optional.of(message);
            }
            return Optional.empty();
    
        } catch (SQLException e) {
            errorHandler.handle(e);
            return Optional.empty();
        }
        
    }

    

    public boolean updateMessage(Integer message_id, String newMessageText){

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

        
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newMessageText);
            pstmt.setInt(2, message_id);
            Integer rows = pstmt.executeUpdate();
            if (rows == 0){
                return false;
            }
            return true;
        } catch (Exception e) {
            errorHandler.handle(e);
            return false;
        }
            
        
    }

    public boolean deleteMessage(Integer message_id){

        String sql = "DELETE FROM message WHERE message_id = ?";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, message_id);
            Integer rows = pstmt.executeUpdate();
            if (rows == 0){
                return false;
            }
            return true;
            
        } catch (SQLException e) {
            errorHandler.handle(e);
        }
        return false;
        
    }

    
    protected static Message constructMessageObject(ResultSet result) throws SQLException {
        
        
        return new Message(
                result.getInt(1),
                result.getInt(2),
                result.getString(3),
                result.getLong(4)
            );
    }

    
}
