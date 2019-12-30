package com.changgou.oauth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

public class CreateJWTTest {

    // 创建令牌的测试类

    @Test
    public void createJWT(){
        // 证书文件的路径,就是密钥库
        String key_location = "changgou.jks";  // 里面有公钥,私钥,证书的信息
        // 密钥库密码
        String key_password = "changgou";
        // 密钥密码
        String keypassword = "changgou";
        // 密钥别名
        String alias = "changgou";

        // 访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        // 创建密钥工厂, 打开密钥库的时候,需要一个密钥库的密码, 因为库里可能存放的不只是一堆密钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,key_password.toCharArray());
        // 从密钥库里进行读取时, 需要密钥密码, 读取密钥对(公钥,私钥)
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keypassword.toCharArray());

        // 获取私钥
        RSAPrivateKey rsaPrivateKey =  (RSAPrivateKey) keyPair.getPrivate();

        // 定义一个 payload 就是JWT令牌的第二部分. 就是第二部分自己想要存放的哪些信息
        Map<String,Object> tokenMap = new HashMap<>();
        tokenMap.put("id","1");
        tokenMap.put("name","itheima");
        tokenMap.put("roles","ROLE_VIP,ROLE_USER");

        // 生成JWT令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivateKey));

        // 取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

    }
}
