package ATORY.atory.domain.search.controller;

import ATORY.atory.domain.search.dto.SearchResponseDto;
import ATORY.atory.domain.search.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Test
    void search_WithQuery_ReturnsResults() throws Exception {
        // Given
        String query = "작가";
        SearchResponseDto.SearchItem item1 = SearchResponseDto.SearchItem.builder()
                .id("1")
                .type("artistNote")
                .title("작가노트 제목")
                .content("작가노트 내용")
                .thumbnailUrl("https://example.com/image1.jpg")
                .authorName("작가1")
                .authorJob("ARTIST")
                .build();

        SearchResponseDto.SearchItem item2 = SearchResponseDto.SearchItem.builder()
                .id("2")
                .type("artistNote")
                .title("다른 작가노트")
                .content("다른 내용")
                .thumbnailUrl("https://example.com/image2.jpg")
                .authorName("작가2")
                .authorJob("ARTIST")
                .build();

        SearchResponseDto response = SearchResponseDto.builder()
                .query(query)
                .type("all")
                .total(2)
                .page(0)
                .size(10)
                .items(Arrays.asList(item1, item2))
                .build();

        when(searchService.search(query, null, 0, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/search")
                        .param("q", query)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.query").value(query))
                .andExpect(jsonPath("$.total").value(2))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].title").value("작가노트 제목"))
                .andExpect(jsonPath("$.items[1].title").value("다른 작가노트"));
    }

    @Test
    void search_EmptyQuery_Returns400() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/search")
                        .param("q", "")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void search_WithType_ReturnsFilteredResults() throws Exception {
        // Given
        String query = "작가";
        String type = "artistNote";
        
        SearchResponseDto response = SearchResponseDto.builder()
                .query(query)
                .type(type)
                .total(1)
                .page(0)
                .size(10)
                .items(Arrays.asList(
                    SearchResponseDto.SearchItem.builder()
                        .id("1")
                        .type("artistNote")
                        .title("작가노트")
                        .content("내용")
                        .thumbnailUrl("https://example.com/image.jpg")
                        .authorName("작가")
                        .authorJob("ARTIST")
                        .build()
                ))
                .build();

        when(searchService.search(query, type, 0, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/search")
                        .param("q", query)
                        .param("type", type)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(type))
                .andExpect(jsonPath("$.total").value(1));
    }
} 