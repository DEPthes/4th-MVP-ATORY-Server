package ATORY.atory.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PublicDataBusinessResponseDto {
    private String status_code;
    private String request_cnt;
    private String valid_cnt;
    private String status;
    private List<BusinessDataDto> data;
    
    @Getter
    @NoArgsConstructor
    public static class BusinessDataDto {
        private String b_no;           // 사업자등록번호
        private String b_stt;          // 납세자상태
        private String b_stt_cd;       // 납세자상태코드
        private String tax_type;       // 과세유형
        private String tax_type_cd;    // 과세유형코드
        private String end_dt;         // 폐업일
        private String utcc_yn;        // 단위과세여부
        private String tax_type_change_dt; // 과세유형전환일자
        private String invoice_apply_dt;    // 세금계산서적용일자
        private String rbf_tax_type;        // 전환과세유형
        private String rbf_tax_type_cd;     // 전환과세유형코드
    }
    
    // API 응답 성공 여부 확인
    public boolean isSuccess() {
        return "OK".equals(status) || "200".equals(status_code);
    }
    
    // 유효한 데이터가 있는지 확인
    public boolean hasValidData() {
        return data != null && !data.isEmpty() && "1".equals(valid_cnt);
    }
} 