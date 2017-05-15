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

import com.pvt.domain.logic.core.CentralAI;
import com.pvt.domain.logic.core.DialogState;
import com.pvt.web.utils.HTMLFilter;
import org.apache.log4j.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Scope;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


@ServerEndpoint(value = "/websocket/chat")
@Component
public class ChatAnnotation implements ApplicationContextAware {

    private static ApplicationContext appContext;
    static CentralAI  centralAI;

    private static final Logger logger = Logger.getLogger(ChatAnnotation.class);

    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);

    public static Set<ChatAnnotation> getConnections() {
        return connections;
    }

    private static final Set<ChatAnnotation> connections =
            new CopyOnWriteArraySet<>();

    public String getNickname() {
        return nickname;
    }

    private  String nickname;
    private Session session;

    private StringBuilder filteredMessage = new StringBuilder(256);

    public ChatAnnotation() {
        logger.debug("new instance " + this + " created. connectionIds=" + connectionIds);
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }


    @OnOpen
    public void start(Session session) {
        logger.debug("OnOpen method of " + this + " is called. Session is " + session);
        this.session = session;
        connections.add(this);

       // broadcast(message);
    }


    @OnClose
    public void end() {
        logger.debug("OnClose method of "+this+" is called");
        connections.remove(this);

       // broadcast(message);
    }


    @OnMessage
    public void incoming(String message) {
        System.out.println("Thread="+Thread.currentThread());
        System.out.println("message=" + message);
        try {

            if (message.contains("\n")) {

                String messageToSend = String.format("%s: %s",
                        nickname, HTMLFilter.filter(message.toString()));
                //  broadcast(messageToSend);

            } else {
                if (message.startsWith("##")) {
                    nickname = message.substring(2);
                    //    broadcast(nickname + " has joined");
                } else if (message.equals("#continue")) {
                    if(centralAI.dialogStates.get(nickname)==null){
                        centralAI.dialogStates.put(nickname, new DialogState(true, true)) ;
                    }else{
                        centralAI.dialogStates.get(nickname).setCanContinue(true);
                        System.out.println("continue");
                    }

                } else if (message.equals("#ready")) {
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
            }
        }catch (Exception e){

        }

    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.error("Chat Error: " + t.toString(), t);
    }


    public static void broadcast(String msg) {
        for (ChatAnnotation client : connections) {
            try {
                if(client.nickname.equals("Fresher")) {
                    synchronized (client) {
                        client.session.getBasicRemote().sendText(msg);
                    }
                }
            } catch (IOException e) {
                logger.debug("Chat Error: Failed to send message to client", e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("* %s %s",
                        client.nickname, "has been disconnected.");
                broadcast(message);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("applicationContext ="+applicationContext);
        appContext=applicationContext;
        centralAI = (CentralAI) appContext.getBean("CentralAI");
    }
}
