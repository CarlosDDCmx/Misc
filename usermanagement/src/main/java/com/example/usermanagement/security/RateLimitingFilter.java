package com.example.usermanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    @Value("${app.rate-limit.requests-per-minute}")
    private int requestsPerMinute;

    private final ConcurrentMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Long> timestamps = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = getClientIp(request);
        
        if (isRateLimitExceeded(clientIp)) {
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                "Rate limit exceeded. Try again later");
            return;
        }
        
        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    private boolean isRateLimitExceeded(String clientIp) {
        long currentTime = System.currentTimeMillis();
        timestamps.putIfAbsent(clientIp, currentTime);
        requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));

        long timestamp = timestamps.get(clientIp);
        if (currentTime - timestamp > 60_000) { // Reset counter every minute
            timestamps.put(clientIp, currentTime);
            requestCounts.get(clientIp).set(0);
        }

        return requestCounts.get(clientIp).incrementAndGet() > requestsPerMinute;
    }
}