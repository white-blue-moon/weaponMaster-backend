package com.example.weaponMaster.modules.common.exception;

import com.example.weaponMaster.modules.common.dto.ApiResponse;
import com.example.weaponMaster.modules.slack.constant.AdminSlackChannelType;
import com.example.weaponMaster.modules.slack.constant.UserSlackNoticeType;
import com.example.weaponMaster.modules.slack.entity.AdminSlackNotice;
import com.example.weaponMaster.modules.slack.service.SlackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SlackService slackService;

    // 클라이언트가 잘못된 요청을 보냈을 때 (400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        System.err.println(e.getMessage());
        slackService.sendMessageAdmin(AdminSlackChannelType.BACK_END_ERROR_NOTICE, String.format(
                "`400 Bad Request Error`\n" +
                        "```\n" +
                        " %s\n" +
                        "```",
                e.getMessage()
        ));

        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    // 예상하지 못한 오류 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(HttpServletRequest request, Exception e) {
        String requestURI = request.getRequestURI();

        // favicon.ico 요청은 무시
        if ("/favicon.ico".equals(requestURI)) {
            return ResponseEntity.ok().build(); // 아무것도 하지 않고 OK 반환
        }

        System.err.println(e.getMessage());
        slackService.sendMessageAdmin(AdminSlackChannelType.BACK_END_ERROR_NOTICE, String.format(
                "`500 Internal Server Error`\n" +
                        "```\n" +
                        "%s\n" +
                        "```",
                e.getMessage()
        ));

        return ResponseEntity.internalServerError().body(ApiResponse.error(String.format("[UNEXPECTED ERROR] %s", e.getMessage())));
    }
}

