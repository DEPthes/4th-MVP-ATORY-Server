package ATORY.atory.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SearchResponseDto {
    private String keyword;
    private String type;
    private int totalCount;
    private int page;
    private int size;
    private List<SearchResultDto> results;
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class SearchResultDto {
        private String id;
        private String title;
        private String content;
        private String type; // "artist", "note", "gallery"
        private String imageUrl;
    }
} 