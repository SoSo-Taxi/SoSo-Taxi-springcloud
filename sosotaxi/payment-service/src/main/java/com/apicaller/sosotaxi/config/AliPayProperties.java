package com.apicaller.sosotaxi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("alipay")
public class AliPayProperties {
    
    private String appId;
    private String gatewayUrl;
    private String format;
    private String charset;
    private String signType;
    private String appPrivateKey;
    private String alipayPublicKey;

    private String returnUrl;
    private String notifyUrl;

}
