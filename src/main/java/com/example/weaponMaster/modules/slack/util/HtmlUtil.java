package com.example.weaponMaster.modules.slack.util;

public class HtmlUtil {

    public static String getPlainText(String contents) {
        String plainText = contents
                .replaceAll("(?i)<br\\s*/?>", "\n")             // <br>, <br/> → 줄바꿈
                .replaceAll("(?i)</p>", "\n")                   // </p> → 줄바꿈
                .replaceAll("(?i)</h[1-6]>", "\n")              // 제목 태그 종료 → 줄바꿈
                .replaceAll("(?i)</(em|strong|span)>", " ")     // 강조, span 태그 종료 → 공백으로 대체
                .replaceAll("(?i)<[^>]*>", "")                  // 나머지 HTML 태그 제거
                .replaceAll("&nbsp;", " ")                      // &nbsp; → 일반 공백
                .replaceAll("[ \\t]+", " ")                     // 연속된 공백 정리
                .replaceAll("(?m)^\\s+", "")                    // 각 줄 시작의 공백 제거
                .replaceAll("(?m)\\s+\n", "\n")                 // 줄 끝의 불필요한 공백 제거
                .trim();

        return plainText;
    }
}
