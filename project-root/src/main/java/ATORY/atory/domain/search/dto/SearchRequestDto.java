package ATORY.atory.domain.search.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchRequestDto {
    private String keyword;
    private String type; // "artist", "note", "gallery" 등
    private int page = 0;
    private int size = 10;
} 