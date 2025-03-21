package com.example.helperforslow.easyarticle.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HighlightedWordResponse {
    private String word;
    private int sentenceId;
    private int start;
    private int end;
    private int combination;
}
