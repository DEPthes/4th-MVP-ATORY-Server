package ATORY.atory.domain.user.controller;

import ATORY.atory.domain.user.dto.AuthMeResponseDto;
import ATORY.atory.domain.user.service.UserService;
import ATORY.atory.global.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void getCurrentUser_ValidToken_ReturnsUserInfo() throws Exception {
        // Given
        String validToken = "valid.jwt.token";
        Long userId = 1L;
        
        AuthMeResponseDto userInfo = AuthMeResponseDto.builder()
                .userId(userId)
                .job("ARTIST")
                .nickname("테스트작가")
                .profileImageUrl("https://example.com/profile.jpg")
                .build();

        when(jwtProvider.validateToken(validToken)).thenReturn(true);
        when(jwtProvider.getUserIdFromToken(validToken)).thenReturn(String.valueOf(userId));
        when(userService.getCurrentUserInfo(userId)).thenReturn(userInfo);

        // When & Then
        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.job").value("ARTIST"))
                .andExpect(jsonPath("$.nickname").value("테스트작가"))
                .andExpect(jsonPath("$.profileImageUrl").value("https://example.com/profile.jpg"));
    }

    @Test
    void getCurrentUser_InvalidToken_Returns401() throws Exception {
        // Given
        String invalidToken = "invalid.jwt.token";
        when(jwtProvider.validateToken(invalidToken)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getCurrentUser_NoToken_Returns401() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }
} 