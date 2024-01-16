package Controller;


import java.util.ArrayList;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ErrorHandler.ErrorHandler;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    
    private ObjectMapper mapper;
    private AccountService accountService;
    private MessageService messageService;
    private ErrorHandler errorHandler;
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        this.mapper = new ObjectMapper();
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.errorHandler = ErrorHandler.getInstance();

        // example
        app.get("example-endpoint", this::exampleHandler);
        
        // account
        app.post("register", this::register);
        app.post("login", this::login);

        // messages
        app.get("messages",this::getAllMessages);
        app.post("messages",this::createMessage);

        app.get("messages/{message_id}",this::getMessage);
        app.patch("messages/{message_id}", this::updateMessage);
        app.delete("messages/{message_id}", this::deleteMessage);
        
        app.get("accounts/{account_id}/messages", this::getUserMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


    private void register(Context context) {
        try{
            Account unverifiedAccount = mapper.readValue(context.body(), Account.class);
            Optional<Account> result = accountService.register(unverifiedAccount);
            if (result.isEmpty()){
                context.status(400);
                context.json("");
                return;
            }
            
            String responseBody = mapper.writeValueAsString(result.get());
            context.status(200);
            context.json(responseBody);

        }  catch (Exception e) {
            errorHandler.handle(e);
            context.status(400);
            context.json("");
            
        }

    }

    private void login(Context context){
        try{
            Account accountInput = mapper.readValue(context.body(), Account.class);
            Optional<Account> result = accountService.login(accountInput);
            if (result.isEmpty()){
                context.status(401);
                context.json("");
                return;
            }
            String responseBody = mapper.writeValueAsString(result.get());
            context.status(200);
            context.json(responseBody);
        } catch (Exception e) {
            errorHandler.handle(e);
            
        } 
        
    }

    private void getAllMessages(Context context){
        ArrayList<Message> result = messageService.getAllMessages();
        context.status(200);

        try{


            String responseBody = mapper.writeValueAsString(result);
            
            context.status(200);
            context.json(responseBody);
        } catch (JsonProcessingException e) {
            errorHandler.handle(e);
        } 
        
    }

    private void getUserMessages(Context context) {
        Integer account_id = Integer.parseInt( context.pathParam("account_id"));

        
            
        ArrayList<Message> result = messageService.getUserMessages(account_id);
        
        

        
        context.status(200);

        try{


            String responseBody = mapper.writeValueAsString(result);
            
            context.status(200);
            context.json(responseBody);
        } catch (JsonProcessingException e) {
            errorHandler.handle(e);
        } 
        
    }

    

    private void getMessage(Context context) {
        Integer message_id = Integer.parseInt( context.pathParam("message_id"));

        
            
        Optional<Message> result = messageService.getMessage(message_id);
        

        
        if(result.isEmpty()){
            context.status(200);
            context.json("");
            return;
        } 

        try {
            String responseBody = mapper.writeValueAsString(result.get());
            context.status(200);
            context.json(responseBody);
            
        } catch (Exception e) {
            context.status(200);
            context.json("");
            
        }        
        
    }

    private void createMessage(Context context) {
        try{
            Message unverifiedMessage = mapper.readValue(context.body(), Message.class);
            Optional<Message> maybeMessage = messageService.createMessage(unverifiedMessage);
            if(maybeMessage.isEmpty()){
                context.status(400);
                context.json("");
                return;
            }
            String responseBody = mapper.writeValueAsString(maybeMessage.get());
            context.status(200);
            context.json(responseBody);

        }  catch (JsonProcessingException e) {
            errorHandler.handle(e);
        }

    }

    
    private void updateMessage(Context context) {
        Integer message_id = Integer.parseInt( context.pathParam("message_id"));

        try{
            JsonNode body = mapper.readTree(context.body());
            String messageText = body.get("message_text").asText();
            Optional<Message> result = messageService.updateMessage(message_id, messageText);


            String responseBody;
            Integer statusCode;

            if(result.isEmpty()){
                
                statusCode = 400;
                responseBody = "";
            } else {
                statusCode = 200;
                responseBody = mapper.writeValueAsString(result.get());
            }
            
            
            context.status(statusCode);
            context.json(responseBody);


        } catch (JsonProcessingException e) {
            errorHandler.handle(e);
        }  
        
        

    }

    private void deleteMessage(Context context) {
        Integer message_id = Integer.parseInt( context.pathParam("message_id"));


        Optional<Message> result = messageService.deleteMessage(message_id);

        
        
        if(result.isEmpty()){
            
            context.status(200);
            context.json("");
            return;
        }



        try {
            context.status(200);
            context.json(
                mapper.writeValueAsString(result.get())
            );
            
        } catch (JsonProcessingException e) {

            context.status(400);
            context.json("");
            
        }
            
       
    }

    
}