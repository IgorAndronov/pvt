package com.pvt.logic.logic.core;

import com.pvt.dao.interfaces.user.UserService;
import com.pvt.web.websocket.chat.ChatAnnotation;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 13.05.2017.
 */

@Component(value = "CentralAI")
public class CentralAI {

    @Resource(name = "UserServiceImpl")
    private UserService userService;

    public static Map<String, DialogState> dialogStates = new ConcurrentHashMap<>();

   public  void doSomeWork()  {
      String[] messages = userService.getIntroMessage("").split("[\\#]");
       dialogStates.put("Fresher",new DialogState(true,false));

       try {
           ChatAnnotation connection = ChatAnnotation.getConnections().get("Fresher");
           if(connection==null){
               return;
           }

           for(int i =0; i<messages.length; i++){
                Thread.currentThread().sleep(700);
                System.out.println("!!! continue");

                if(dialogStates.get(connection.getNickname()).getAfterResume()){
                    sendAndWait("Вижу ты снова вернулся. ", connection);
                    sendAndWait("Хорошо, тогда продолжим. ",connection);
                    sendAndWait("Напомню, на чем мы остановились:",connection);

                    int lastAnchorIndex = dialogStates.get(connection.getNickname()).getLastAnchorBeforeResume();
                    i=lastAnchorIndex+1;
                    dialogStates.get(connection.getNickname()).setAfterResume(false);
                }

                System.out.println("!!! next message = " +messages[i]);
                if(messages[i].equals("@@")){
                       addLastAnchor(connection.getNickname(),i);
                       messages[i]="";
                }
                if(messages[i].equals("")){
                       dialogStates.get(connection.getNickname()).setCanContinue(true);
                       continue;
                }


               sendAndWait(messages[i],connection);

            }



       }catch (Exception e){

       }

    }

   private void addLastAnchor(String nickname, int index){
       dialogStates.get(nickname).setLastAnchorBeforeResume(index);

   }

   private void sendAndWait(String message, ChatAnnotation connection){
       System.out.println("sending..."+message);
       ChatAnnotation.broadcast(message);
       dialogStates.get(connection.getNickname()).setCanContinue(false);
       try {
           while(!dialogStates.get(connection.getNickname()).getCanContinue()){
               Thread.currentThread().sleep(200);
               System.out.println(connection.getNickname() + " "+ dialogStates.get(connection.getNickname()).getCanContinue());
           }
       }catch (Exception e){

       }

   }
}
