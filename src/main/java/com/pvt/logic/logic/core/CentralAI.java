package com.pvt.logic.logic.core;

import com.pvt.dao.interfaces.user.UserService;
import com.pvt.web.websocket.chat.ChatAnnotation;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 13.05.2017.
 */

@Component(value = "CentralAI")
public class CentralAI {

    @Resource(name = "UserServiceImpl")
    private UserService userService;

    public static Map<String, DialogState> dialogStates = new HashMap<>();

   public  void doSomeWork()  {
      String[] messages = userService.getIntroMessage("").split("[\\#]");
       dialogStates.put("Fresher",new DialogState(true,false));

       try {
           ChatAnnotation connection = ChatAnnotation.getConnections().get("Fresher");
           if(connection==null){
               return;
           }

           for(int i =0; i<messages.length; i++){
                while(!dialogStates.get(connection.getNickname()).getCanContinue()){
                    Thread.sleep(100);
                }
                Thread.sleep(700);
                dialogStates.get(connection.getNickname()).setCanContinue(false);

                if(dialogStates.get(connection.getNickname()).getAfterResume()){
                       ChatAnnotation.broadcast("Вижу ты снова вернулся.");
                       Thread.sleep(1000);
                       ChatAnnotation.broadcast("Хорошо, тогда продолжим.");
                       Thread.sleep(1000);
                       ChatAnnotation.broadcast("напомню, на чем мы остановились:");
                       Thread.sleep(1000);
                       int lastAnchorIndex = dialogStates.get(connection.getNickname()).getLastAnchorBeforeResume();
                       i=lastAnchorIndex+1;
                       dialogStates.get(connection.getNickname()).setAfterResume(false);
                   }
                if(i>=messages.length){
                    break;
                }
                if(messages[i].equals("@@")){
                       addLastAnchor(connection.getNickname(),i);
                       messages[i]="";
                   }
                if(messages[i].equals("")){
                       dialogStates.get(connection.getNickname()).setCanContinue(true);
                       continue;
                   }
                   System.out.println("sending..."+messages[i]);
                   ChatAnnotation.broadcast(messages[i]);
            }



       }catch (Exception e){

       }

    }

   private void addLastAnchor(String nickname, int index){
       dialogStates.get(nickname).setLastAnchorBeforeResume(index);

   }
}
