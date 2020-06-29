package njnu.opengms.container.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

/**
 * @ClassName JwtUtils
 * @Description 用于生成jwToken
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public class JwtUtils {
    private static final long EXPIRATION_TIME = 24 * 3600 * 1000;
    private static final String SECRET = "hello";
    private static final String TOKEN_PREFIX = "Bearer";

    public static String generateToken(String uid, String username, String password) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        HashMap<String, Object> map = new HashMap<>(30);
        map.put("uid", uid);
        map.put("username", username);
        map.put("password", password);
        map.put("type", "JWT");


        return Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }


    /**
     * @param token
     *
     * @return Claims 可以使用get 获取Claims.get("username") Claims.get("password") Claims.get("uid")
     */
    public static Claims parseJWT(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @param token
     * @param key   定义返回的key值，可以是"username","password","uid","type"
     *
     * @return
     */
    public static Object parseJWT(String token, String key) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            return claims.get(key);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization") != null ? request.getHeader("Authorization") : (request.getParameter("token") != null ? request.getParameter("token") : null);
    }
}
