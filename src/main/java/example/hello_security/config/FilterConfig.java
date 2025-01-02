package example.hello_security.config;

import example.hello_security.filter.CaptchaAuthenticationFilter;
import example.hello_security.filter.IpBlockFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public IpBlockFilter ipBlockFilter() {
        return new IpBlockFilter();
    }

    @Bean
    public CaptchaAuthenticationFilter captchaAuthenticationFilter() {
        return new CaptchaAuthenticationFilter();
    }

    @Bean
    public FilterRegistrationBean<IpBlockFilter> IpBlockRegistrationBean() {
        FilterRegistrationBean<IpBlockFilter> registrationBean = new FilterRegistrationBean<>(ipBlockFilter());
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CaptchaAuthenticationFilter> captchaAuthenticationFilterFilterRegistrationBean() {
        FilterRegistrationBean<CaptchaAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(captchaAuthenticationFilter());
        registrationBean.setEnabled(false);
        return registrationBean;
    }
}
