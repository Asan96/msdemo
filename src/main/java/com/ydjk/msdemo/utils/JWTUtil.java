package com.ydjk.msdemo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ydjk.msdemo.entity.User;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JWTUtil {
    //token有效时长
    private static final long EXPIRE=24*60*60*1000L;
    //token的密钥
    private static final String SECRET="jwt+shiro";


    public static String createToken(User user) throws UnsupportedEncodingException {
        //token过期时间
        Date date=new Date(System.currentTimeMillis()+EXPIRE);

        //jwt的header部分
        Map<String ,Object>map=new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");

        //使用jwt的api生成token
        String token= JWT.create()
                .withHeader(map)
                .withClaim("username", user.getUsername())//私有声明
                .withClaim("userId", user.getId())
                .withExpiresAt(date)//过期时间
                .withIssuedAt(new Date())//签发时间
                .sign(Algorithm.HMAC256(SECRET));//签名
        return token;
    }

    //校验token的有效性，1、token的header和payload是否没改过；2、没有过期
    public static boolean verify(String token){
        try {
            //解密
            JWTVerifier verifier=JWT.require(Algorithm.HMAC256(SECRET)).build();
            verifier.verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    //无需解密也可以获取token的信息
    public static String getUsername(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Long getUserId(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getRealName(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("realName").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Boolean isCurrentUser(String token, User user){
        return Objects.equals(getUserId(token), user.getId());
    }

    public static Boolean isCurrentUser(String token, Long userId){
        return Objects.equals(getUserId(token), userId);
    }
}
