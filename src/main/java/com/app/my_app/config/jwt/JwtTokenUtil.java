package com.app.my_app.config.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



@Component
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("Private KEY")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    /*Trong Java, Claims là một interface được định nghĩa trong thư viện javax.security
    . Nó đại diện cho các thông tin chứa trong JSON Web Token (JWT), bao gồm các thông tin về người dùng, quyền hạn,
     thời gian hết hạn, v.v.
     Mỗi Claims sẽ chứa một hoặc nhiều cặp key-value, trong đó key là một chuỗi xác định loại thông tin và value là giá trị tương ứng của thông tin đó. Ví dụ, Claims có thể chứa thông tin về tên người dùng, quyền hạn của người dùng, thời gian hết hạn của token, v.v.

Để truy xuất các thông tin trong Claims, ta có thể sử dụng các phương thức get() của interface này. Ví dụ:

    Claims claims = Jwts.parser()
                   .setSigningKey(secretKey)
                   .parseClaimsJws(token)
                   .getBody();
String username = claims.getSubject();
Date expirationDate = claims.getExpiration();
Trong đó, Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody() được sử dụng để parse thông tin trong JWT và trả về đối tượng Claims. Sau đó, ta có thể sử dụng các phương thức get() của Claims để lấy các thông tin cần thiết, ví dụ như tên người dùng (getSubject()) hoặc thời gian hết hạn (getExpiration()).


    * */
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    /*Function<Claims, T> claimsResolver
    Trong lập trình Java, Function là một interface được sử dụng để định nghĩa một hàm số,
     chấp nhận một đối tượng đầu vào và trả về một đối tượng đầu ra.
     Function<Claims, T> có nghĩa là nó đại diện cho một hàm số, chấp nhận một đối tượng Claims làm đầu vào và trả về một đối tượng có kiểu T.

    * */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
        /*
        * Câu lệnh claimsResolver.apply(claims) sử dụng đối tượng claimsResolver được khai báo trước đó để ánh xạ (map) đối tượng Claims thành một đối tượng khác kiểu T (kiểu đối tượng được xác định trước đó).
          phương thức apply() của đối tượng claimsResolver là một phương thức của interface Function, nhận đối tượng Claims làm tham số đầu vào và trả về một đối tượng kiểu T. Do đó, khi gọi phương thức claimsResolver.apply(claims),
*       nó sẽ sử dụng claimsResolver để thực hiện phép ánh xạ và trả về đối tượng T tương ứng.
        * */
    }

    /*
    Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody()
     được sử dụng để parse thông tin trong JWT và trả về đối tượng Claims

    * */
    // lay claims chứa thông tin trong token dc lưu trữ dưới dạng key-value ra từ chuỗi token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * The function takes a userDetails object and returns a JWT token
     *
     * @param userDetails This is the user object that contains the user's information.
     * @return A JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * It takes a map of claims, a subject, and a secret key, and returns a JWT token
     * @param claims A map of claims that will be added to the JWT.
     * @param subject The subject of the token.
     * @return A JWT token
     */
    
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    /**
     * If the username in the token matches the username in the userDetails object, and the token is not expired, then
     * return true
     *
     * @param token The JWT token to validate
     * @param userDetails The user details object that contains the username and password of the user.
     * @return A boolean value.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
    