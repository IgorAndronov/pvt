/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.pvt.web.websocket.chat;

import com.pvt.logic.logic.core.CentralAI;
import com.pvt.logic.logic.core.DialogState;
import com.pvt.web.utils.ContextUtil;
import com.pvt.web.utils.HTMLFilter;
import org.apache.log4j.Logger;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


@ServerEndpoint(value = "/websocket/chat")
public class ChatAnnotation {

    private  String nickname;
    private Session session;

    private static final Logger logger = Logger.getLogger(ChatAnnotation.class);

    private static final Map<String, ChatAnnotation> connections = new ConcurrentHashMap<>();


    public ChatAnnotation() {
        logger.debug("!!! new instance " + this + " created.");

    }

    @OnOpen
    public void start(Session session) {
        logger.debug("OnOpen method of " + this + " is called. Session is " + session);
        this.session = session;

    }

    @OnClose
    public void end() {
        logger.debug("OnClose method of "+this+" is called");
        connections.remove(this.nickname);

    }

    @OnMessage
    public void incoming(String message) {
        System.out.println("Thread="+Thread.currentThread());
        System.out.println("message=" + message);

        ApplicationContext appContext= ContextUtil.getApplicationContext();
        CentralAI centralAI = (CentralAI) appContext.getBean("CentralAI");

        try {

            if (message.contains("\n")) {
                String messageToSend = String.format("%s: %s", nickname, HTMLFilter.filter(message.toString()));
                //  broadcast(messageToSend);
                return;
            }
            if (message.startsWith("##")) {
                this.nickname = message.substring(2);
                connections.put(this.nickname, this);
                return;
            }
            if (message.equals("#continue")) {
                if(centralAI.dialogStates.get(nickname)==null){
                    centralAI.dialogStates.put(nickname, new DialogState(true, true)) ;
                }else{
                    centralAI.dialogStates.get(nickname).setCanContinue(true);
                    System.out.println("continue");
                }
                return;
            }
            if (message.equals("#ready")) {
                Thread.sleep(2000);
                boolean newThread = true;
                for(Thread thread:Thread.getAllStackTraces().keySet()){
                   if(thread.getName().startsWith(nickname)){
                       centralAI.dialogStates.get(nickname).setCanContinue(true);
                       centralAI.dialogStates.get(nickname).setAfterResume(true);
                       newThread = false;
                   }
                }
                if(newThread){
                    new Thread(nickname+System.currentTimeMillis()) {
                        public void run() {
                            centralAI.doSomeWork();
                        }
                    }.start();
                }

            }

        }catch (Exception e){
            System.out.println("!!! "+e);

        }

    }


    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.error("!!! Chat Error: " + t.toString(), t);
    }


    public static void broadcast(String msg) {
        for (String client : connections.keySet()) {
            try {
                if(client.equals("Fresher")) {
                    synchronized (client) {
                        connections.get(client).session.getBasicRemote().sendText(msg);
                    }
                }
            } catch (IOException e) {
                logger.debug("Chat Error: Failed to send message to client", e);
                connections.remove(client);
                try {
                    connections.get(client).session.close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("* %s %s",
                        client, "has been disconnected.");
                broadcast(message);
            }
        }
    }


    public static Map<String,ChatAnnotation> getConnections() {
        return connections;
    }

    public String getNickname() {
        return nickname;
    }
}
