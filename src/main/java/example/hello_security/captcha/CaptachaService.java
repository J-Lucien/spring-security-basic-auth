package example.hello_security.captcha;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Getter
public class CaptachaService {

    private final RecaptchaProperties recaptchaProperties;
    private final RestTemplate restTemplate;


    public boolean verifyCaptch(String response){
        StringBuilder captchaUrl = new StringBuilder(recaptchaProperties.getUrl());
        captchaUrl.append("?secret=").append(recaptchaProperties.getSecret()).append("&response=").append(response);
        Map<String, Object> result = restTemplate.getForObject(captchaUrl.toString(), Map.class);
        return result != null && Boolean.TRUE.equals(result.get("success"));
    }

}

