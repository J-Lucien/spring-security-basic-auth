package example.hello_security.filter;

import example.hello_security.captcha.CaptachaService;
import example.hello_security.ip.IPservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Setter
public class CaptchaAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private CaptachaService captachaService;
    @Autowired
    private IPservice iPservice;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(new AntPathRequestMatcher("/login","POST").matches(request)){
            String requestParameter = request.getParameter("g-recaptcha-response");
            System.out.println("requestParameter: "+requestParameter);
            if (requestParameter == null) {
                System.out.println("requestParameter is null");
            }else if(iPservice.isReCaptchaRequired(request.getRemoteAddr())){ // check if captcha is required
                if (!captachaService.verifyCaptch(requestParameter)) {
                    System.out.println("Captcha is not valid");
                    response.sendRedirect("/login?error=true");
                    return;
                }
            }
        }
        else if(new AntPathRequestMatcher("/login","GET").matches(request)){
            if(iPservice.isReCaptchaRequired(request.getRemoteAddr())){
                request.setAttribute("recaptchaSiteKey",captachaService.getRecaptchaProperties().getSiteKey());
                request.setAttribute("recaptchaRequired",true);
            }
        }
        filterChain.doFilter(request, response);
    }
}
