package example.hello_security.provider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Slf4j
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private static final String PASSWORD_NOT_FOUND = "$2a$12$kgjXtfuBFl.iPWdLQ1HkU.4LMmnTyONn.JwpknHUU0bCp76sYJMRW"; // passwordNotFound
    private static final long RESPONSE_DELAY = 500;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        long startTime = System.currentTimeMillis();

        try{
            boolean matches = passwordEncoder.matches(password, passwordEncoder.encode("password"));
            if(!matches || !username.equals("user")){
                throw new BadCredentialsException("Invalid username or password");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.createAuthorityList("ROLE_USER"));
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);
            return authenticationToken;
        }catch (AuthenticationException e){
            log.debug("{}: Authentication failed","authenticate");
            simulatePasswordCheck();
            throw new BadCredentialsException("Invalid username or password");
        }finally {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime < RESPONSE_DELAY) {
                try {
                    Thread.sleep(RESPONSE_DELAY - elapsedTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }


    private void simulatePasswordCheck() {
        log.debug("{}: Simulating password check","simulatePasswordCheck");
        passwordEncoder.matches("passwordNotFound", PASSWORD_NOT_FOUND);
    }

}
