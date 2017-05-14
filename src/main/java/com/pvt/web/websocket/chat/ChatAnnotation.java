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
package com.mycompany.credit.web.websocket.chat;

import com.mycompany.credit.web.LoginController;
import com.mycompany.credit.web.utils.HTMLFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;



@ServerEndpoint(value = "/websocket/chat")
public class ChatAnnotation {

    private static final Logger logger = Logger.getLogger(ChatAnnotation.class);

    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<ChatAnnotation> connections =
            new CopyOnWriteArraySet<>();

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
        // Never trust the client

       if(message.contains("\n")) {

           String messageToSend = String.format("%s: %s",
                    nickname, HTMLFilter.filter(message.toString()));
            broadcast(messageToSend);

        }else {
           if(message.startsWith("##")){
               nickname=message.substring(2);
               broadcast(nickname + " has joined");
           }else{
              broadcast("typing");
           }
        }

    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.error("Chat Error: " + t.toString(), t);
    }


    private static void broadcast(String msg) {
        for (ChatAnnotation client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
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
}
