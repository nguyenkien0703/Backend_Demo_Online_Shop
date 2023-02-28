package com.app.my_app.config;

import com.app.my_app.config.jwt.JwtAuthenticationEntryPoint;
import com.app.my_app.config.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity// bat tinh nang bao mat cho ung dung web 
@EnableGlobalMethodSecurity(prePostEnabled = true) // cung la bat tinh nang bao mat cho toan cuc ung dung nhung ma o level cao hon, dc su dung 
// @PreAuthorize, @PostAuthorize, @Secured, @RolesAllowed và
// @EnableAspectJAutoProxy để kiểm soát quyền truy cập của người dùng đối với
// các phương thức trong ứng dụng của bạn.
/*
 * Khi sử dụng @EnableGlobalMethodSecurity, bạn có thể đặt giá trị
 * prePostEnabled = true để bật tính năng bảo mật phương thức nâng cao
 * như @PreAuthorize và @PostAuthorize. @PreAuthorize được sử dụng để kiểm tra
 * quyền truy cập trước khi phương thức được gọi, trong khi @PostAuthorize được
 * sử dụng để kiểm tra quyền truy cập sau khi phương thức đã trả về kết quả.
 * 
 */


public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    /*
     * được sử dụng để cấu hình việc xác thực người dùng. Phương thức
     * configureGlobal(AuthenticationManagerBuilder auth) được sử dụng để thiết lập
     * UserDetailsService, một interface trong Spring Security cung cấp thông tin
     * người dùng cho quá trình xác thực, và cách mã hóa mật khẩu của người dùng.
     * đối tượng AuthenticationManagerBuilder được truyền vào để cấu hình
     * UserDetailsService và mã hóa mật khẩu.
     * Trong trường hợp này, passwordEncoder() trả về một đối tượng PasswordEncoder,
     * được sử dụng để mã hóa mật khẩu của người dùng trước khi lưu trữ vào cơ sở dữ
     * liệu. Sau đó, phương thức passwordEncoder() được gọi trên đối tượng
     * AuthenticationManagerBuilder để thiết lập cách mã hóa mật khẩu trong quá
     * trình xác thực.
     * 
     */
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        AppConfig appConfig = new AppConfig();// tao ra doi tuong passwordEncoder

        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(appConfig.passwordEncoder());
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors();
        // We don't need CSRF for this example
        httpSecurity.csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests().antMatchers("/authenticate", "/register", "/swagger-ui/**", "/v3/**","/swagger-resources/**", "/", "/js/**", "/css/**", "/img/**", "/demo/**","/api/auth/**").permitAll().
                // all other requests need to be authenticated
                        anyRequest().authenticated().and().
                // make sure we use stateless session; session won't be used to
                // store user's state.
                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        /*
        * Dòng lệnh này được sử dụng trong cấu hình Spring Security để thêm một Filter vào chuỗi Filter của Spring Security 
        trước một Filter đã được cấu hình sẵn (UsernamePasswordAuthenticationFilter trong trường hợp này).
        
        JwtRequestFilter trong ví dụ này là một custom filter để xác thực và xử lý các yêu cầu liên quan đến JWT.
        * Bằng cách sử dụng phương thức addFilterBefore(), chúng ta đang chỉ định rằng filter này sẽ được thực hiện trước khi
        * filter UsernamePasswordAuthenticationFilter được gọi, đảm bảo rằng các yêu cầu của người dùng đã được xác thực trước khi được
        *  xử lý tiếp.
        *
        * */
    }


}