package com.example.helperforslow.easyarticle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedTextResponse {
    private String originalText;
    private String simplifiedText;
    private String context;
}
