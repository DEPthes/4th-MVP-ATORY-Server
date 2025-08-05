package ATORY.atory.domain.user.service;

import ATORY.atory.domain.user.dto.PublicDataBusinessRequestDto;
import ATORY.atory.domain.user.dto.PublicDataBusinessResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessValidationService {

    private final RestTemplate restTemplate;

    @Value("${public.data.api.key}")
    private String apiKey;

    @Value("${public.data.api.url}")
    private String apiUrl;

    public boolean validateBusinessRegistrationNumber(String registrationNumber) {
        try {
            // 입력값 검증
            if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
                log.warn("사업자등록번호가 입력되지 않았습니다.");
                return false;
            }

            // 숫자만 추출 (하이픈 제거)
            String cleanNumber = registrationNumber.replaceAll("[^0-9]", "");
            
            if (cleanNumber.length() != 10) {
                log.warn("사업자등록번호 형식이 올바르지 않습니다: {}", registrationNumber);
                return false;
            }

            // 공공데이터포털 API 호출
            PublicDataBusinessResponseDto response = callPublicDataApi(cleanNumber);
            
            // API 응답 검증
            if (response == null) {
                log.warn("API 응답이 null입니다: {}", registrationNumber);
                return false;
            }
            
            if (!response.isSuccess()) {
                log.warn("API 호출이 실패했습니다. status: {}, status_code: {}", 
                    response.getStatus(), response.getStatus_code());
                return false;
            }
            
            if (!response.hasValidData()) {
                log.warn("유효한 사업자 데이터가 없습니다. valid_cnt: {}, data size: {}", 
                    response.getValid_cnt(), response.getData() != null ? response.getData().size() : 0);
                return false;
            }

            PublicDataBusinessResponseDto.BusinessDataDto businessData = response.getData().get(0);
            
            // 납세자상태 확인 (01: 계속사업자, 02: 휴업자, 03: 폐업자)
            String businessStatus = businessData.getB_stt_cd();
            String businessStatusText = businessData.getB_stt();
            
            log.info("사업자 상태 확인 - 번호: {}, 상태코드: {}, 상태: {}", 
                cleanNumber, businessStatus, businessStatusText);
            
            // 계속사업자(01) 또는 휴업자(02)인 경우만 유효
            boolean isValid = "01".equals(businessStatus) || "02".equals(businessStatus);
            
            log.info("사업자 등록번호 검증 결과 - 번호: {}, 유효성: {}", cleanNumber, isValid);
            
            return isValid;

        } catch (Exception e) {
            log.error("사업자등록번호 검증 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }

    public String getBusinessName(String registrationNumber) {
        try {
            // 공공데이터포털 API에서는 사업자명을 직접 제공하지 않으므로
            // 별도의 API 호출이 필요하거나, 다른 방법을 사용해야 함
            // 현재는 검증 성공 시 기본값 반환
            if (validateBusinessRegistrationNumber(registrationNumber)) {
                return "유효한 사업자"; // 실제로는 별도 API 호출 필요
            }
            return null;
        } catch (Exception e) {
            log.error("사업자명 조회 중 오류 발생: {}", e.getMessage(), e);
            return null;
        }
    }

    private PublicDataBusinessResponseDto callPublicDataApi(String registrationNumber) {
        try {
            // URL에 serviceKey를 쿼리 파라미터로 포함
            String url = String.format("%s?serviceKey=%s", apiUrl, apiKey);
            
            // 공공데이터포털 API는 POST 요청 시 JSON 형태로 데이터 전송
            PublicDataBusinessRequestDto requestDto = new PublicDataBusinessRequestDto(registrationNumber);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<PublicDataBusinessRequestDto> request = new HttpEntity<>(requestDto, headers);
            
            log.info("공공데이터포털 API 호출 URL: {}", url);
            log.info("공공데이터포털 API 요청 데이터: {}", registrationNumber);
            
            PublicDataBusinessResponseDto response = restTemplate.postForObject(
                url, 
                request, 
                PublicDataBusinessResponseDto.class
            );
            
            log.info("공공데이터포털 API 응답: {}", response);
            
            return response;
            
        } catch (Exception e) {
            log.error("공공데이터포털 API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("사업자등록번호 검증 API 호출에 실패했습니다.", e);
        }
    }
} 