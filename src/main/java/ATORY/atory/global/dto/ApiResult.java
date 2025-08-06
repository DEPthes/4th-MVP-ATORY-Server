package ATORY.atory.global.dto;

import ATORY.atory.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResult<T> {

    // API 상태 코드
    private int code;

    // API 상태
    private HttpStatus status;

    // API 응답 메시지
    private String message;

    // API 응답 데이터
    private T data;

    private ApiResult(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> of(HttpStatus status, String message, T data) {
        return new ApiResult<>(status, message, data);
    }

    public static <T> ApiResult<T> of(HttpStatus status, T data) {
        return of(status, status.name(), data);
    }

    public static <T> ApiResult<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }

    public static <T> ApiResult<T> ok(String message) {
        return of(HttpStatus.OK, message, null);
    }

    public static <T> ApiResult<T> ok(String message, T data) {
        return of(HttpStatus.OK, message, data);
    }

    public static <T> ApiResult<T> success(T data) {
        return of(HttpStatus.OK, data);
    }

    public static <T> ApiResult<T> withError(ErrorCode errorCode, T data) {
        return new ApiResult<>(errorCode.getHttpStatus(), errorCode.getMessage(), data);
    }

    public static <T> ApiResult<T> withError(ErrorCode errorCode) {
        return withError(errorCode, null);
    }

    public static <T> ApiResult<T> error(int code, String message) {
        return new ApiResult<>(HttpStatus.valueOf(code), message, null);
    }
}
