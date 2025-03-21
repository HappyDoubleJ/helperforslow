package com.example.helperforslow.easyarticle.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimplifyTextRequest {
    private String article;
    private String selectedText;
}
