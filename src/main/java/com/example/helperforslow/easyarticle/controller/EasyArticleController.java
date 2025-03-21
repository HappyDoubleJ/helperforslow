package com.example.helperforslow.easyarticle.controller;

import com.example.helperforslow.easyarticle.dto.*;
import com.example.helperforslow.easyarticle.service.GptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/easy-article")
public class EasyArticleController {
    private final GptService gptService;

    public EasyArticleController(GptService gptService) {
        this.gptService = gptService;
    }

    // ✅ 기사 분석 API (POST 요청)
    @PostMapping("/analyze")
    public ResponseEntity<ArticleResponse> analyzeArticle(@RequestBody ArticleRequest request) {
        ArticleResponse response = gptService.analyzeText(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/simplyfy")
    public ResponseEntity<SimplifiedTextResponse> simplifyText(@RequestBody SimplifyTextRequest request) {
        SimplifiedTextResponse response = gptService.simplifyText(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/highlight")
    public ResponseEntity<List<HighlightedWordResponse>> groupWords(@RequestBody HighlightRequest request) {
        List<HighlightedWordResponse> result = gptService.findSemanticGroups(request);
        return ResponseEntity.ok(result);
    }






}
