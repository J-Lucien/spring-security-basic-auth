package example.hello_security.filter.exception;

import example.hello_security.ip.FailedAttempt;
import example.hello_security.ip.IPservice;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private IPservice ipService;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String ipAddress = request.getRemoteAddr();
        System.out.println("Login failed from IP: "+ ipAddress);
        ipService.addFailedAttempt(ipAddress);
        System.out.println("Failed attempt count: "+ ipService.getFailedAttemptCount(ipAddress));
        response.sendRedirect("/login?error=true");
    }
}
