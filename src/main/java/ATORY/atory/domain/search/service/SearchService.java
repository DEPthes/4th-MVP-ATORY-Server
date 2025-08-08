package ATORY.atory.domain.search.service;

import ATORY.atory.domain.search.dto.SearchResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    public SearchResponseDto search(String query, String type, int page, int size) {
        log.info("검색 요청 - query: {}, type: {}, page: {}, size: {}", query, type, page, size);
        
        // TODO: 실제 검색 로직 구현
        // 현재는 스텁 구현으로 빈 결과 반환
        
        List<SearchResponseDto.SearchItem> items = new ArrayList<>();
        
        return SearchResponseDto.builder()
                .query(query)
                .type(type != null ? type : "all")
                .total(0)
                .page(page)
                .size(size)
                .items(items)
                .build();
    }
} 