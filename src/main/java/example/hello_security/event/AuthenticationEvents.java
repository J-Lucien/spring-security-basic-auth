package example.hello_security.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {

    @EventListener
    public void onSuccessAuthentication (AuthenticationSuccessEvent successEvent) {
        System.out.println("EVENT----------*******-----------Authentication success: " + successEvent.getAuthentication().getName());
    }

    @EventListener
    public void onFailureAuthentication (AbstractAuthenticationFailureEvent failureEvent){
        System.out.println("EVENT----------*******-----------Authentication failure: " + failureEvent.getAuthentication().getName());
    }
}
