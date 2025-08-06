package ATORY.atory.global.exception;

import ATORY.atory.global.dto.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 사용자를 찾을 수 없는 경우
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResult<String>> handleUserNotFoundException(UserNotFoundException e) {
        log.error("User not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResult.error(ErrorCode.USER_NOT_FOUND.getCode(), e.getMessage()));
    }

    /**
     * 잘못된 역할이 지정된 경우
     */
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ApiResult<String>> handleInvalidRoleException(InvalidRoleException e) {
        log.error("Invalid role: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(ErrorCode.INVALID_ROLE.getCode(), e.getMessage()));
    }

    /**
     * 사업자 등록번호 검증 실패
     */
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiResult<String>> handleBusinessValidationException(BusinessValidationException e) {
        log.error("Business validation failed: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(ErrorCode.BUSINESS_VALIDATION_FAILED.getCode(), e.getMessage()));
    }

    /**
     * 작가를 찾을 수 없는 경우
     */
    @ExceptionHandler(ArtistNotFoundException.class)
    public ResponseEntity<ApiResult<String>> handleArtistNotFoundException(ArtistNotFoundException e) {
        log.error("Artist not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResult.error(ErrorCode.ARTIST_NOT_FOUND.getCode(), e.getMessage()));
    }

    /**
     * 매퍼 관련 예외
     */
    @ExceptionHandler(MapperException.class)
    public ResponseEntity<ApiResult<String>> handleMapperException(MapperException e) {
        log.error("Mapper error: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.error(ErrorCode.MAPPER_ERROR.getCode(), e.getMessage()));
    }

    /**
     * 일반적인 IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal argument: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(ErrorCode.INVALID_INPUT.getCode(), e.getMessage()));
    }

    /**
     * 예상치 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<String>> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }
}
