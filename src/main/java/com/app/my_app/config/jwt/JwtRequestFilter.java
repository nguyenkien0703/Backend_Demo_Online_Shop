package com.app.my_app.config.jwt;

import com.app.my_app.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
The JwtRequestFilter extends the Spring Web Filter OncePerRequestFilter class. For any incoming request this Filter
class gets executed. It checks if the request has a valid JWT token. If it has a valid JWT Token then it sets the
 Authentication in the context, to specify that the current user is authenticated.
 lớp này có nhiệm vụ check xem filter đã check trên requesrt này chưa, moiõ filter chỉ được check 1 lần,
 Lớp OncePerRequestFilter cung cấp phương thức doFilterInternal để định nghĩa logic xử lý filter trên mỗi request.
  Phương thức này được gọi bởi Spring Framework khi có request đến ứng dụng.

 */

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * If the request has a valid JWT token, then set the authentication in the security context
     *
     * @param request  The request object.
     * @param response The response object.
     * @param chain    The FilterChain object represents the chain of filters that the request will pass through.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);// cat tu ki tu 7 den het

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        /*
         * Đối tượng UsernamePasswordAuthenticationToken có thể có từ 2 đến 3 tham số
         * đầu vào, tùy thuộc vào cách sử dụng và cấu hình của ứng dụng. Trong đó:
         * 
         * Tham số đầu tiên là principal: Biểu diễn thông tin chính của người dùng đăng
         * nhập. Trong ví dụ của bạn, đối tượng userDetails được truyền vào tham số này.
         * Đối tượng này chứa thông tin về người dùng bao gồm tên đăng nhập, mật khẩu,
         * quyền truy cập và các thông tin khác.
         * 
         * Tham số thứ hai là credentials: Biểu diễn thông tin xác thực, thường là mật
         * khẩu. Trong trường hợp này, giá trị của tham số là null, vì thông tin xác
         * thực đã được xác minh trước đó bởi UserDetailsService và không cần nhập lại
         * mật khẩu.
         * 
         * Tham số thứ ba là authorities: Biểu diễn danh sách các quyền được cấp cho
         * người dùng đăng nhập. Trong ví dụ của bạn, đối tượng
         * userDetails.getAuthorities() được truyền vào tham số này, cho phép người dùng
         * truy cập các tài nguyên và chức năng được giới hạn bởi các quyền tương ứng.
         * 
         * Với các ứng dụng Spring Security, thông tin xác thực và quyền truy cập của
         * người dùng được lưu trữ trong đối tượng Authentication. Khi người dùng đăng
         * nhập thành công, đối tượng UsernamePasswordAuthenticationToken được tạo ra và
         * chứa các thông tin này, sau đó được đưa vào đối tượng SecurityContextHolder.
         * 
         * 
         */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // nếu trong token ta có thể lấy tên cảu ng dùng và hiện tại thì ng dùng chưa dc xác thực 
            // notice: authentication là đói tượng  thuộc security, chứa thông tin cảu ng dùng sau khi đăng nhập success như thông tin xac thuhực, các quyền 
            // userDetails chứa thông tin chi tiết: username, password, các quyền truy cập ,thông tin khác 
            UserDetails userDetails = this.userService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}
/*
 * JWT (JSON Web Token) là một phương tiện để truyền thông tin giữa các bên một cách an toàn,
 * đóng gói các thông tin về người dùng vào một chuỗi ký tự có định dạng JSON. JWT được sử dụng rộng rãi trong các ứng dụng web và di động, đặc biệt là khi có nhiều hệ thống liên kết với nhau.

Spring Security có thể được kết hợp với JWT để xác thực và ủy quyền người dùng. Các bước để xác thực và ủy quyền bằng JWT bao gồm:

Người dùng đăng nhập và gửi yêu cầu đến server.

Server kiểm tra thông tin đăng nhập của người dùng và tạo ra một JWT token, bao gồm các thông tin về người dùng và thời gian hết hạn của token.

JWT token được trả về cho người dùng.

Các yêu cầu tiếp theo từ người dùng đến server đều phải được gửi với JWT token.

Server sẽ xác thực JWT token và lấy các thông tin về người dùng và cấp quyền truy cập cho người dùng tương ứng.

Trong Spring Security, JWT có thể được sử dụng với JwtAuthenticationFilter để xác thực người dùng và tạo JWT token. JwtAuthenticationFilter sẽ kiểm tra token được gửi đến trong header của yêu cầu và sử dụng JWT secret key để xác thực token. Sau đó, nếu token hợp lệ, JwtAuthenticationFilter sẽ tạo ra Authentication object và lưu trữ thông tin xác thực của người dùng.

Sau khi xác thực thành công, các AuthorizationFilter sẽ được sử dụng để kiểm tra quyền truy cập của người dùng. Nếu người dùng có quyền truy cập yêu cầu, yêu cầu sẽ được xử lý bởi Controller và trả về kết quả cho người dùng.

Vì JWT được truyền trực tiếp giữa client và server, nó giúp giảm thiểu số lượng lưu lượng mạng truyền tải giữa các bên và giúp tăng tính bảo mật trong quá trình truyền thông tin.

 * 
 */
