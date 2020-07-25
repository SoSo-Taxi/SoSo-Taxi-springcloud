package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.utils.JwtTokenUtils;
import com.apicaller.sosotaxi.webSocket.message.AuthRequest;
import com.apicaller.sosotaxi.webSocket.message.AuthResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
@Component
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
        /**
         * 登陆模块没加
         */
        String userRole = JwtTokenUtils.getUserRolesByToken(token);
        String userName = JwtTokenUtils.getUsernameByToken(token);
        if(userName == null || userName.isEmpty()) {
            LOGGER.error("用户名为空");
        }
        LOGGER.info("[session({}) 接入成功,用户名是({})]\"",session, userName);
        LOGGER.info("[session({}) 用户身份是({})]\"", session,userRole);

        String driverRole = "driver";
        if (driverRole.equals(userRole))
        {
            //考虑到司机断线重连的情况，这里先在已有map中找一下
            LoginDriver loginDriver = WebSocketUtil.findLoginDriver(userName);
            if(loginDriver == null) {
                loginDriver = new LoginDriver();
                loginDriver.setStartListening(false);
                loginDriver.setDispatched(false);
                loginDriver.setUserName(userName);
            }
            else {
            }
            loginDriver.setToken(token);
            WebSocketUtil.addLoginDriver(session,loginDriver);
        }
        else {
            WebSocketUtil.addSession(session, message.getAccessToken());
            WebSocketUtil.send(session, AuthResponse.TYPE, new AuthResponse().setCode(200));
        }

    }

    @Override
    public String getMessageType() {
        return AuthRequest.TYPE;
    }
}
