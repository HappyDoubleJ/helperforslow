package com.example.helperforslow.easyarticle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedTextResponse {
    private String originalText;
    private String simplifiedText;
    private String context;
}
