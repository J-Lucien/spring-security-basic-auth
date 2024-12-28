package example.hello_security.ip;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class FailedAttempt {
    private long lastAccessTime;
    private int count;
}
