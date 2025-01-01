package example.hello_security.ip;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
@Setter
public class IPservice {
    private final long BLOCK_DURATION = 30000; //30 seconds
    private final int MAX_ATTEMPT = 3;
    private ConcurrentHashMap<String,FailedAttempt> attemptConcurrentHashMap = new ConcurrentHashMap<>();

    public void addFailedAttempt(String ipAddress){
        if(attemptConcurrentHashMap.get(ipAddress) == null){
            attemptConcurrentHashMap.put(ipAddress, new FailedAttempt(System.currentTimeMillis(), 1,false));
        }else{
            FailedAttempt failedAttempt = attemptConcurrentHashMap.get(ipAddress);
            failedAttempt.setCount(failedAttempt.getCount() + 1);
            failedAttempt.setLastAccessTime(System.currentTimeMillis());
            attemptConcurrentHashMap.put(ipAddress, failedAttempt);
        }
    }

    public FailedAttempt getFailedAttempt(String ipAddress){
        return attemptConcurrentHashMap.get(ipAddress);
    }

    public void resetFailedAttempt(String ipAddress){
        FailedAttempt failedAttempt = attemptConcurrentHashMap.get(ipAddress);
        if(Objects.nonNull(failedAttempt)){
            failedAttempt.setCount(0);
            attemptConcurrentHashMap.put(ipAddress, failedAttempt);
        }

    }

    public boolean isBlocked(String ipAddress){
        FailedAttempt failedAttempt = getFailedAttempt(ipAddress);
        if(failedAttempt != null){
            return failedAttempt.getCount() > MAX_ATTEMPT && (System.currentTimeMillis() - failedAttempt.getLastAccessTime()) < BLOCK_DURATION;
        }
        return false;
    }

    public boolean isTimeToUnblock(String ipAddress){
        FailedAttempt failedAttempt = getFailedAttempt(ipAddress);
        if(failedAttempt != null){
            return failedAttempt.getCount() > MAX_ATTEMPT && (System.currentTimeMillis() - failedAttempt.getLastAccessTime()) > BLOCK_DURATION;
        }
        return false;
    }

    public int getFailedAttemptCount(String ipAddress){
        FailedAttempt failedAttempt = getFailedAttempt(ipAddress);
        if(failedAttempt != null){
            return failedAttempt.getCount();
        }
        return 0;
    }

    public void setCaptchaRequired(String ipAddress,boolean captchaRequired){
        FailedAttempt failedAttempt = getFailedAttempt(ipAddress);
        if(failedAttempt != null){
            failedAttempt.setCaptchaRequired(captchaRequired);
            attemptConcurrentHashMap.put(ipAddress, failedAttempt);
        }
    }

    public boolean isReCaptchaRequired(String ipAddress){
        FailedAttempt failedAttempt = getFailedAttempt(ipAddress);
        if(failedAttempt != null){
            return failedAttempt.isCaptchaRequired();
        }
        return false;
    }

}
