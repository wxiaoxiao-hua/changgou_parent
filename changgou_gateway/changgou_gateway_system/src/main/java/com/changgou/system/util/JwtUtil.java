package com.changgou.system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// token的校验工具类
public class JwtUtil {
    // 设置这个的有限期为
    public static final Long JWT_TTL = 3600000L; //以毫秒为单位, 计算出来一个小时
    // 设置密钥明文
    public static final String JWT_KEY = "itcast";
    // 附加信息
    public static final String JWT_CLAIMS = "roles";

    // 生成加密之后的密钥
    public static SecretKey generalKey(){
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
        return key;
    }

    // 解析这个密码
    public static Claims parseJWT(String jwt)throws Exception{
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
