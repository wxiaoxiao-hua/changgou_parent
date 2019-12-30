package com.changgou.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true) // 激活方法上面的preauthorize注解
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

    // 公钥
    private static final String PUBLIC_KEY = "public.key";

    @Bean  // 存放jwt token的对象
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean  // 解析jwt令牌第二部分,自己存入的信息
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(getPubkey());
        return converter;
    }

    private String getPubkey(){
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try{
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            // lines()得到的是一个数组,然后用joining()把数据拼接成一整个字符串
            return br.lines().collect(Collectors.joining("\n"));
        }catch(Exception e){
            return null;
        }
    }

    // 安全配置,对每个达到系统的http请求连接进行校验

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 所有的请求必须认证通过
        http.authorizeRequests()
                // 下边的路径放行
                .antMatchers("/user/add","/user/load/**").permitAll()  // 将配置的地址进行放行
                .anyRequest().authenticated(); // 其它的地址全部要进行认证授权
    }
}
