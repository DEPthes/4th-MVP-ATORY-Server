package ATORY.atory.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDto {
    private String query;
    private String type;
    private int total;
    private int page;
    private int size;
    private List<SearchItem> items;
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchItem {
        private String id;
        private String type;
        private String title;
        private String content;
        private String thumbnailUrl;
        private String authorName;
        private String authorJob;
    }
} 