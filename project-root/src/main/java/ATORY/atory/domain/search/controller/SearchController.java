package ATORY.atory.domain.search.controller;

import ATORY.atory.domain.search.dto.SearchRequestDto;
import ATORY.atory.domain.search.dto.SearchResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
@Tag(name = "Search", description = "검색 관련 API")
public class SearchController {

    @GetMapping
    @Operation(summary = "검색", description = "키워드 기반 검색을 수행합니다.")
    public ResponseEntity<SearchResponseDto> search(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "all") String type,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        
        // TODO: 실제 검색 로직 구현 (추후 구현 예정)
        SearchResponseDto response = SearchResponseDto.builder()
                .keyword(keyword)
                .type(type)
                .totalCount(0)
                .page(page)
                .size(size)
                .results(java.util.Collections.emptyList())
                .build();
        
        return ResponseEntity.ok(response);
    }
} 