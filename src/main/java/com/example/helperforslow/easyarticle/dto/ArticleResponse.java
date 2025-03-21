package com.example.helperforslow.easyarticle.dto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleResponse {
    private String result;

    public ArticleResponse(String result) { this.result = result; }

    public String getResult() { return result; }

}
