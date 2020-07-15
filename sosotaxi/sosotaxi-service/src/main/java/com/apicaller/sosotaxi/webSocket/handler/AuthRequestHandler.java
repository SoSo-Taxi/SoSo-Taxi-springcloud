package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.utils.JwtTokenUtils;
import com.apicaller.sosotaxi.webSocket.message.AuthRequest;
import com.apicaller.sosotaxi.webSocket.message.AuthResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
public class AuthRequestHandler implements MessageHandler<AuthRequest> {


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRequestHandler.class);


    @Override
    public void execute(Session session, AuthRequest message) {


        String token = message.getAccessToken();
        if (StringUtils.isEmpty(token)) {
            WebSocketUtil.send(session, AuthResponse.TYPE,
                    new AuthResponse().setCode(1).setMessage("认证 accessToken 未传入,登陆失败"));
            LOGGER.info("[session({}) 接入失败,不存在token]\"", session);
            return;
        }
//
//        if (!token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
//            SecurityContextHolder.clearContext();
//            return;
//        }
        LOGGER.info("[session({}) 接入成功,用户名是({})]\"", session,JwtTokenUtils.getUsernameByToken(token));



        WebSocketUtil.addSession(session, message.getAccessToken());
        WebSocketUtil.send(session, AuthResponse.TYPE, new AuthResponse().setCode(200));


    }

    @Override
    public String getMessageType() {
        return null;
    }
}
