package ATORY.atory.global.dto;

import ATORY.atory.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResult<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    private ApiResult(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /** -------------------- 성공 응답 -------------------- */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(HttpStatus.OK, "success", data);
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(HttpStatus.OK, message, data);
    }

    public static <T> ApiResult<T> successMessage(String message) {
        return new ApiResult<>(HttpStatus.OK, message, null);
    }

    /** -------------------- 에러 응답 -------------------- */
    public static <T> ApiResult<T> error(ErrorCode errorCode, T data) {
        return new ApiResult<>(errorCode.getHttpStatus(), errorCode.getMessage(), data);
    }

    public static <T> ApiResult<T> error(ErrorCode errorCode) {
        return error(errorCode, null);
    }

    /** -------------------- 기존 코드 호환용 -------------------- */
    public static <T> ApiResult<T> withError(ErrorCode errorCode, T data) {
        return error(errorCode, data);
    }

    public static <T> ApiResult<T> withError(ErrorCode errorCode) {
        return error(errorCode, null);
    }
}