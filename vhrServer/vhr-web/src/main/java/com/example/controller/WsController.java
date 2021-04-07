package com.example.controller;

import com.example.entity.Chat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Date;

@Controller
public class WsController {
    SimpMessagingTemplate template;

    public WsController(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * org.springframework.web.socket.server.support.AbstractHandshaSessionConnectedEventkeHandler#doHandshake(ServerHttpRequest, ServerHttpResponse, WebSocketHandler, Map)
     *  determineUser(request, wsHandler, attributes);
     *
     * @param principal
     * @param chat
     */
    @MessageMapping("/ws/chat")
    public void handleMsg(Principal principal, Chat chat) {
        chat.setFrom(principal.getName());
        chat.setDate(new Date());
        //private String destinationPrefix = "/user/";
        //ToUser,security框架会将每个session id对应的已登录认证的用户存入请求的Principal中
        //org.springframework.web.socket.messaging.DefaultSimpUserRegistry.onApplicationEvent
        //org.springframework.security.authentication.AbstractAuthenticationToken.getName
        template.convertAndSendToUser(chat.getTo(),"/queue/chat",chat);
    }
}
