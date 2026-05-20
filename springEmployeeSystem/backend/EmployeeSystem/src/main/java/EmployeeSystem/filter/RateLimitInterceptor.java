package EmployeeSystem.filter;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int CAPACITY = 200;
    private static final Duration REFILL_PERIOD = Duration.ofMinutes(1);

    // Caffeine cache with 2-minute idle expiry prevents unbounded growth for inactive IPs
    private final com.github.benmanes.caffeine.cache.Cache<String, Bucket> buckets =
        Caffeine.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .maximumSize(100_000)
            .build();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ip = resolveClientIp(request);
        Bucket bucket = buckets.get(ip, k -> newBucket());

        if (bucket.tryConsume(1)) {
            return true;
        }

        log.warn("Rate limit exceeded for IP: {}", ip);
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Too many requests — please retry after a minute.\"}");
        return false;
    }

    private Bucket newBucket() {
        return Bucket.builder()
            .addLimit(Bandwidth.classic(CAPACITY, Refill.greedy(CAPACITY, REFILL_PERIOD)))
            .build();
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.trim().isEmpty()) {  // trim().isEmpty() is Java 8 compatible
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
