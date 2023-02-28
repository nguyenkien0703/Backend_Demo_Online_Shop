package com.app.my_app.config.jwt;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * This class will extend Spring's AuthenticationEntryPoint class and override its method commence.
 * It rejects every unauthenticated request and send error code 401
 * chuyên dùng để xửi lí các yêu cầu xác thực
 * Trong Spring Security, AuthenticationEntryPoint là một interface được sử dụng để xử lý lỗi xác thực.
 *  Nó cung cấp một phương thức có tên commence để bắt đầu quá trình xác thực khi xảy ra lỗi xác thực.
 * Phương thức commence được gọi khi một yêu cầu cần xác thực được gửi đến ứng dụng,
 *  nhưng người dùng chưa được xác thực hoặc token không hợp lệ. Nó có hai tham số đầu vào:
 *  HttpServletRequest và HttpServletResponse. HttpServletRequest là yêu cầu gửi đến ứng dụng, 
 * HttpServletResponse là phản hồi sẽ được gửi trả lại cho người dùng.
 *
 * Phương thức commence thường được triển khai để gửi trả lại mã lỗi và thông báo cho người dùng, hoặc chuyển hướng người dùng đến trang đăng nhập. Các thao tác được thực hiện trong phương thức commence phụ thuộc vào cách ứng dụng của bạn xử lý các yêu cầu xác thực
 * phương thức commence của AuthenticationEntryPoint có hai tham số đầu vào là HttpServletRequest và HttpServletResponse.
 *
 * Tuy nhiên, trong trường hợp xảy ra lỗi xác thực, AuthenticationEntryPoint sẽ ném ra một AuthenticationException. Thông thường, AuthenticationException sẽ chứa thông tin chi tiết về lỗi xác thực xảy ra.
 *
 * Do đó, trong phương thức commence, ta sẽ thêm một tham số kiểu AuthenticationException để xử lý các thông tin chi tiết về lỗi xác thực. Khi xảy ra lỗi xác thực, phương thức commence sẽ được gọi và sẽ nhận được đối tượng AuthenticationException, từ đó ta có thể lấy thông tin chi tiết về lỗi xác thực và xử lý phản hồi trả về cho người dùng phù hợp.
 *
 * Ví dụ, trong phương thức commence, ta có thể dùng đối tượng AuthenticationException để lấy thông tin chi tiết về lỗi và tạo một thông báo phản hồi trả về cho người dùng, ví dụ như mã lỗi và thông báo lỗi.
 *
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;// trc và sau khi Serializable là 1

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}