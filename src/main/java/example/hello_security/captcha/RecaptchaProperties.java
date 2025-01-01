package example.hello_security.captcha;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "captcha")
@Data
public class RecaptchaProperties {
    private String url;
    private String secret;
    private String siteKey;
}
