package com.imooc.mumushengxian.util;

import com.imooc.mumushengxian.model.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成 解析和验证JSON Web Token
 */
@Component
public class JwtTokenUtil {
//    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    //JWT密钥
    @Value("${jwt.secret}")
    private static String secret;
    //JWT过期时间(毫秒), 这里设置为24小时
    @Value("${jwt.expiration}")
    private static long expiration;

    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成JWT令牌
     * @param user
     * @return JWT令牌字符串
     */
    public static String generateToken(User user) {
        // 获取当前时间
        Date now = new Date();
        // 计算过期时间
        Date expiryDate = new Date(now.getTime() + expiration);
        // 创建JWT声明（payload）
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        // 构建JWT并返回字符串
        return Jwts.builder()
                .setClaims(claims) // 设置声明
                .setSubject(user.getUsername()) // 设置主题
                .setIssuedAt(now) // 设置签发时间
                .setExpiration(expiryDate) // 设置过期时间
                .signWith(getSecretKey(), SignatureAlgorithm.HS512) // 使用密钥签名
                .compact(); // 生成令牌字符串
    }

    /**
     * 解析JWT令牌
     * @param token
     * @return JWT中的所有声明信息
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey()) //设置签名密钥
                .build() //构建
                .parseClaimsJws(token) //解析令牌
                .getBody(); //获取声明体
    }

    /**
     * 验证JWT令牌的有效性
     * @param token
     * @return
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
