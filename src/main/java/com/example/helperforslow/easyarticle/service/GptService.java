package com.example.helperforslow.easyarticle.service;

import com.example.helperforslow.easyarticle.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GptService {
    private
    final GptApiClient gptApiClient;

    public GptService(GptApiClient gptApiClient) {
        this.gptApiClient = gptApiClient;
    }

    public ArticleResponse analyzeText(ArticleRequest request) {
        String gptResponse = gptApiClient.sendArticleRequest(request.getText());
        return new ArticleResponse(gptResponse);
    }

    public SimplifiedTextResponse simplifyText(SimplifyTextRequest request) {


        String prompt = String.format(
                "기사 원문: %s\n\n" +
                        "문맥을 고려하여 아래 문장을 의미를 유지한 채 더 쉬운 표현으로 바꿔주세요:\n" +
                        "선택된 문장: '%s'\n" +
                        "쉬운 표현:",
                request.getArticle(), request.getSelectedText()
        );

        // GPT 호출
        String simplifiedText = gptApiClient.sendArticleRequest(prompt);

        // 응답 생성
        return new SimplifiedTextResponse(
                request.getSelectedText(),
                simplifiedText.trim(),
                extractContext(request.getArticle(), request.getSelectedText())
        );

    }
    private String extractContext(String article, String selectedText) {
        if (article == null || selectedText == null) {
            return "Error: Context not available";
        }

        // 그냥 전체 기사 반환
        return article;
    }

    public List<HighlightedWordResponse> findSemanticGroups(HighlightRequest request){
        String prompt = buildPrompt(request.getFullText(), request.getWord());

        // GPT API 호출
        String gptResponseJson = gptApiClient.sendArticleRequest(prompt);

        // 🔴 디버깅 로그 추가
        System.out.println("📌 GPT 응답 원본 타입: " + (gptResponseJson == null ? "null" : gptResponseJson.getClass().getSimpleName()));
        System.out.println("📌 GPT 응답 원본: " + gptResponseJson);

        // JSON이 이스케이프된 형태라면, 이스케이프 제거
        if (gptResponseJson.startsWith("\"") && gptResponseJson.endsWith("\"")) {
            gptResponseJson = gptResponseJson.substring(1, gptResponseJson.length() - 1); // 따옴표 제거
            gptResponseJson = gptResponseJson.replace("\\\"", "\""); // 이스케이프 제거
        }

        // Jackson ObjectMapper 사용하여 JSON을 Java 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // ✅ JSON 이스케이프 문자 처리
            if (gptResponseJson.startsWith("\"") && gptResponseJson.endsWith("\"")) {
                gptResponseJson = gptResponseJson.substring(1,gptResponseJson.length() - 1);

                gptResponseJson = gptResponseJson.replace("\\\"", "\"");
                gptResponseJson = gptResponseJson.replace("\\n", ""); // 개행 제거
            }

            return objectMapper.readValue(
                    gptResponseJson.trim(),
                    new TypeReference<List<HighlightedWordResponse>>() {}
            );
        } catch (Exception e) {
            System.err.println("❌ JSON 변환 실패! 응답 원본: " + gptResponseJson);
            throw new RuntimeException("GPT 응답 파싱 실패: " + gptResponseJson, e);
        }
    }

    private String buildPrompt(String text, String targetWord) {
        return String.format("""
        전체 문장:
        %s

        🔍 작업 설명:
        아래 문장에서 '%s'와 **의미상 유사하거나 같은 의미로 사용된 모든 단어들**을 찾아주세요.
        단어가 형태는 달라도 문맥상 '%s'와 같은 의미로 쓰였다면 같은 그룹(combination) 번호를 부여해야 합니다.

        📌 응답 형식 (설명 없이 JSON 배열만 반환하세요):
        [
          {"word": "은행", "sentenceId": 0, "start": 10, "end": 12, "combination": 1},
          {"word": "금융기관", "sentenceId": 1, "start": 34, "end": 38, "combination": 1}
        ]

        ⚠️ 유의사항:
        - 같은 의미로 쓰인 단어는 모두 동일한 'combination' 값을 가져야 합니다.
        - 문장이 다르더라도 의미가 같으면 같은 번호를 써야 합니다.
        - sentenceId는 문장의 순서 (0부터 시작).
        - start와 end는 문장에서 해당 단어가 시작하고 끝나는 인덱스입니다.
        - 반드시 유효한 JSON 배열만 응답하세요 (설명 ❌).
    """, text, targetWord, targetWord);
    }
}
