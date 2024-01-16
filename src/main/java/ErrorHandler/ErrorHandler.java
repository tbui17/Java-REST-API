package ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ErrorHandler {
    public Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
    private static ErrorHandler instance;

    private ErrorHandler(){

    }

    public static ErrorHandler getInstance(){
        if(instance == null){
            instance = new ErrorHandler();
            return instance;
        }
        return instance;
    }
    
    public void handle(Exception e){
        logger.error(e.toString());
    }
}
