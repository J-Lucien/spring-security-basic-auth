package example.hello_security.filter;

import example.hello_security.captcha.CaptachaService;
import example.hello_security.ip.IPservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Setter
public class CaptchaAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private CaptachaService captachaService;
    @Autowired
    private IPservice iPservice;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean reCaptchaRequired = iPservice.isReCaptchaRequired(request.getRemoteAddr());
        boolean matches = new AntPathRequestMatcher("/login", "POST").matches(request);
        log.debug("matches: {}", matches);
        if (matches) {
            String requestParameter = request.getParameter("g-recaptcha-response");
            log.debug("requestParameter: {}", requestParameter);
            if (reCaptchaRequired) { // check if captcha is required
                log.debug("Captcha is required for IP: {}", request.getRemoteAddr());
                if (Objects.isNull(requestParameter) || requestParameter.isEmpty()) {
                    log.debug("Captcha is required");
                    response.sendRedirect("/login?captchaRequired");
                    return;
                }
                if (!captachaService.verifyCaptch(requestParameter)) {
                    log.debug("Captcha is not valid");
                    response.sendRedirect("/login?captchaError");
                    return;
                }
            }
        } else {
            boolean get = new AntPathRequestMatcher("/login", "GET").matches(request);
            log.debug("GET: {}", get);
            if (get) {
                if (reCaptchaRequired) {
                    request.setAttribute("recaptchaSiteKey", captachaService.getRecaptchaProperties().getSiteKey());
                    request.setAttribute("recaptchaRequired", true);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
