package com.example.weaponMaster.modules.common.exception;

import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.constant.AdminSlackChannelType;
import com.example.weaponMaster.modules.slack.service.SlackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // logger 로 스택 트레이스를 함께 로깅
    private static final Logger       logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final        SlackService slackService;

    // 클라이언트가 잘못된 요청을 보냈을 때 (400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("400 Bad Request error occurred: ", e);
        slackService.sendMessageAdmin(AdminSlackChannelType.BACK_END_ERROR_NOTICE, String.format(
                "`400 Bad Request Error`\n" +
                        "```\n" +
                        "%s\n" +
                          "%s" +
                        "```",
                e.getMessage(),
                getSimpleStackTrace(e.getStackTrace())
        ));

        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    // 예상하지 못한 오류 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(HttpServletRequest request, Exception e) {
        String requestURI = request.getRequestURI();

        // favicon.ico 요청은 무시 -> 아무것도 하지 않고 OK 반환
        if ("/favicon.ico".equals(requestURI)) {
            return ResponseEntity.ok().build();
        }

        logger.error("500 Unexpected error occurred: ", e);
        slackService.sendMessageAdmin(AdminSlackChannelType.BACK_END_ERROR_NOTICE, String.format(
                "`500 Internal Server Error`\n" +
                        "```\n" +
                        "%s\n" +
                          "%s" +
                        "```",
                e.getMessage(),
                getSimpleStackTrace(e.getStackTrace())
        ));

        return ResponseEntity.internalServerError().body(ApiResponse.error(e.getMessage()));
    }

    // 스택 트레이스 요약 필터링 (내가 작성한 코드 중에서는 어디서 발생했는지)
    private String getSimpleStackTrace(StackTraceElement[] e) {
        StringBuilder simpleStackTrace = new StringBuilder();
        for (StackTraceElement element : e) {
            if (element.getClassName().startsWith("com.example.weaponMaster")) {
                simpleStackTrace.append(element).append("\n");
            }
        }

        return simpleStackTrace.isEmpty() ? "" : ("\n" + simpleStackTrace.toString());
    }

}

