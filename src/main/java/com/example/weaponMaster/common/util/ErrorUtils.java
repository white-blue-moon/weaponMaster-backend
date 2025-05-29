package com.example.weaponMaster.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class ErrorUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static boolean isErrorCode(String errBody, String expectedCode) {
        JsonNode rootNode = objectMapper.readTree(errBody);
        String errorCode  = rootNode.path("error").path("code").asText();

        if (expectedCode.equals(errorCode)) {
            return true;
        }

        return false;
    }
}
