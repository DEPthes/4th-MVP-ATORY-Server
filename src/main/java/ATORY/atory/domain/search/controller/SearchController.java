package ATORY.atory.domain.search.controller;

import ATORY.atory.domain.search.dto.SearchResponseDto;
import ATORY.atory.domain.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@Tag(name = "Search", description = "검색 관련 API")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "통합 검색", description = "키워드로 작가노트, 컬렉션, 전시, 공모전, 사용자를 검색합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검색 성공"),
        @ApiResponse(responseCode = "400", description = "검색어가 비어있음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<SearchResponseDto> search(
            @Parameter(description = "검색어", example = "작가")
            @RequestParam @NotBlank(message = "검색어는 필수입니다.") String q,
            
            @Parameter(description = "검색 타입", example = "artistNote")
            @RequestParam(required = false) String type,
            
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "페이지당 항목 수", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        SearchResponseDto response = searchService.search(q, type, page, size);
        return ResponseEntity.ok(response);
    }
} 