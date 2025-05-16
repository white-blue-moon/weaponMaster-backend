package com.example.weaponMaster.config.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

// RestClient -> 필요 시 인터셉터로 통신 로그 확인할 때 사용
@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, org.springframework.http.client.ClientHttpRequestExecution execution) throws java.io.IOException {
        log.info("[Request] {} {}", request.getMethod(), request.getURI());

        try {
            ClientHttpResponse response = execution.execute(request, body);
            log.info("[Response] Status: {}", response.getStatusCode());
            return response;
        } catch (IOException e) {
            log.error("[IOException] during request: {}", e.getMessage(), e);
            throw  e;
        } catch (Exception e) {
            log.error("[Exception] during request: {}", e.getMessage(), e);
            throw e;
        }
    }
}
