package example.hello_security.filter;

import example.hello_security.ip.FailedAttempt;
import example.hello_security.ip.IPservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class IpBlockFilter extends OncePerRequestFilter {

    @Autowired
    private IPservice iPservice;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var ipAddress = request.getRemoteAddr();

        if ("/login".equals(request.getServletPath())) {
            boolean blocked = iPservice.isBlocked(ipAddress);
            if (blocked) {
                iPservice.setCaptchaRequired(ipAddress,true);
                response.sendRedirect("/blocked");
                return;
            } else if (iPservice.isTimeToUnblock(ipAddress)) {
                iPservice.resetFailedAttempt(ipAddress);
            }
        }
        filterChain.doFilter(request, response);
    }
}



