package Service;

import java.util.ArrayList;
import java.util.Optional;


import DAO.MessageDAO;

import Model.Message;

public class MessageService {
    private MessageDAO dao;
    private AccountService accountService;
    
    
    public MessageService(){
        this.dao = new MessageDAO();
        this.accountService = new AccountService();
        
    }

    public ArrayList<Message> getAllMessages(){
        return dao.getAllMessages();
    }

    public ArrayList<Message> getUserMessages(Integer accountId){
        
        return dao.getUserMessages(accountId);
    }

    public Optional<Message> getMessage(Integer message_id){
        return dao.getMessage(message_id);
    }

    public Optional<Message> deleteMessage(Integer messageId){
        Optional<Message> maybeMessage = getMessage(messageId);
        if (maybeMessage.isEmpty()){
            
            return Optional.empty();
            
        }
        boolean isSuccess = dao.deleteMessage(messageId);
        if (!isSuccess){
            
            return Optional.empty();
        }
        return maybeMessage;
    }

    public Optional<Message> updateMessage(Integer messageId, String messageText) {
        if(!isValidMessageText(messageText)){
            return Optional.empty();
        }
        if(dao.getMessage(messageId).isEmpty()){
            return Optional.empty();
        }
        dao.updateMessage(messageId, messageText);
        
        return dao.getMessage(messageId);

    }


    public Optional<Message> createMessage(Message newMessage) {
        if(!isValidMessageText(newMessage.getMessage_text())){
            return Optional.empty();
            
        }
        if(!accountService.checkAccountExists(newMessage.getPosted_by())){
            return Optional.empty();
        }
        
        return dao.createMessage(newMessage);
        
        
        
        
        

    }

    protected static boolean isValidMessageText(String messageText){
        return !(messageText.length() >= 255 || messageText.isBlank());
    }


}
