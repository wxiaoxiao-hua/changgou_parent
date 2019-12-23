package com.changgou.system.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

// 这是一个生成鉴权也就是 token 的工具类
public class JwtUtil {

    // 设置这个的有限期为
    public static final Long JWT_TTL = 3600000L; //以毫秒为单位, 计算出来一个小时
    // 设置密钥明文
    public static final String JWT_KEY = "itcast";
    // 附加信息
    public static final String JWT_CLAIMS = "roles";

    // 创建token
    public static String createJWT(String id,String subject,Long ttlMillis,Object claims){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //获取当前时间的毫秒值
        long nowMillis = System.currentTimeMillis();
        // 获取当前的日期
        Date now = new Date(nowMillis);
        // 进行判断
        if(ttlMillis==null){
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        SecretKey secretKey = generalKey();

        JwtBuilder builder = Jwts.builder()
                .setId(id)  //唯一的id值
                .setSubject(subject) // 主题,可以是json数据
                .setIssuer("admin") // 签发者
                .setIssuedAt(now) // 签发的时间
                .signWith(signatureAlgorithm,secretKey)  // 使用HS256对称加密算法签名,第二个参数是密钥
                .claim(JWT_CLAIMS,claims) // 附加信息
                .setExpiration(expDate); // 设置过期时间

        return builder.compact();
    }

    // 生成加密之后的密钥
    public static SecretKey generalKey(){
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
        return key;
    }
}
