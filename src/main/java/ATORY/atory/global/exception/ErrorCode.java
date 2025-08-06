package ATORY.atory.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생하였습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Request Body를 통해 전달된 값이 유효하지 않습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "잘못된 역할입니다."),
    BUSINESS_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "사업자 등록번호 검증에 실패했습니다."),
    ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND, "작가를 찾을 수 없습니다."),
    MAPPER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 매핑 중 오류가 발생했습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 업로드 실패"),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 삭제 실패");

    private final HttpStatus httpStatus;
    private final String message;

    public int getCode() {
        return httpStatus.value();
    }
}
