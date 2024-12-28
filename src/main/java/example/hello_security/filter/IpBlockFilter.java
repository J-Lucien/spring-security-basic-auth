package example.hello_security.filter;

import example.hello_security.ip.IPservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class IpBlockFilter extends OncePerRequestFilter {

    private final IPservice iPservice;

    public IpBlockFilter(IPservice iPservice) {
        this.iPservice = iPservice;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var ipAddress = request.getRemoteAddr();

        if ("/login".equals(request.getServletPath())) {
            boolean blocked = iPservice.isBlocked(ipAddress);
            if (blocked) {
                response.sendRedirect("/blocked");
                return;
            } else if (iPservice.isTimeToUnblock(ipAddress)) {
                iPservice.resetFailedAttempt(ipAddress);
            }
        }
        filterChain.doFilter(request, response);
    }
}



