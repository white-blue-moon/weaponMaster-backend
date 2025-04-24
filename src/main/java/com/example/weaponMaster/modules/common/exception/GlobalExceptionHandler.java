package com.example.weaponMaster.modules.common.exception;

import com.example.weaponMaster.modules.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 클라이언트가 잘못된 요청을 보냈을 때 (400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        System.err.println(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    // 예상하지 못한 오류 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception e) {
        System.err.println(e.getMessage());
        return ResponseEntity.internalServerError().body(ApiResponse.error(String.format("[UNEXPECTED ERROR] %s", e.getMessage())));
    }
}

