package example.hello_security.filter;

import example.hello_security.ip.FailedAttempt;
import example.hello_security.ip.IPservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class IpBlockFilter extends OncePerRequestFilter {

    @Autowired
    private IPservice iPservice;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("/login".equals(request.getServletPath())) {
            var ipAddress = request.getRemoteAddr();
            boolean blocked = iPservice.isBlocked(ipAddress);
            log.debug("Blocked: {}", blocked);
            if (blocked) {
                log.debug("Blocked IP: {}", ipAddress);
                iPservice.setCaptchaRequired(ipAddress, true);
                response.sendRedirect("/blocked");
                return;
            } else if (iPservice.isTimeToUnblock(ipAddress)) {
                iPservice.resetFailedAttempt(ipAddress);
            }
        }
        filterChain.doFilter(request, response);
    }
}



