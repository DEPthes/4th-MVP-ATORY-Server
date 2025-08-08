package ATORY.atory.domain.artist.artistNote.controller;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteListResponseDto;
import ATORY.atory.domain.artist.artistNote.service.ArtistNoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistNoteController.class)
class ArtistNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistNoteService artistNoteService;

    @Test
    void getArtistNoteList_EmptyState_ReturnsEmptyList() throws Exception {
        // Given
        Page<ArtistNoteListResponseDto> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(artistNoteService.getArtistNoteList(any(), any())).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/api/v1/artist-notes")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void getArtistNoteList_WithData_ReturnsArtistNotes() throws Exception {
        // Given
        ArtistNoteListResponseDto note1 = ArtistNoteListResponseDto.builder()
                .noteId(1L)
                .thumbnailUrl("https://example.com/image1.jpg")
                .artist(ArtistNoteListResponseDto.ArtistInfo.builder()
                        .id(1L)
                        .nickname("작가1")
                        .englishName("Artist1")
                        .job("ARTIST")
                        .phone("010-****-1234")
                        .email("artist1@example.com")
                        .statusMessage("안녕하세요")
                        .build())
                .build();

        ArtistNoteListResponseDto note2 = ArtistNoteListResponseDto.builder()
                .noteId(2L)
                .thumbnailUrl("https://example.com/image2.jpg")
                .artist(ArtistNoteListResponseDto.ArtistInfo.builder()
                        .id(2L)
                        .nickname("작가2")
                        .englishName("Artist2")
                        .job("ARTIST")
                        .phone("010-****-5678")
                        .email("artist2@example.com")
                        .statusMessage("반갑습니다")
                        .build())
                .build();

        Page<ArtistNoteListResponseDto> page = new PageImpl<>(Arrays.asList(note1, note2), PageRequest.of(0, 10), 2);
        when(artistNoteService.getArtistNoteList(any(), any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/artist-notes")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].noteId").value(1))
                .andExpect(jsonPath("$.content[0].artist.nickname").value("작가1"))
                .andExpect(jsonPath("$.content[1].noteId").value(2))
                .andExpect(jsonPath("$.content[1].artist.nickname").value("작가2"));
    }
} 