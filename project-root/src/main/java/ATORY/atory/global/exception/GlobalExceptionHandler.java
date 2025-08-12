package ATORY.atory.global.exception;

import ATORY.atory.global.dto.ApiResult;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getDefaultMessage())
                .orElse("잘못된 요청입니다.");
        return ApiResult.of(HttpStatus.BAD_REQUEST, msg, null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Void> handleConstraintViolation(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getMessage())
                .orElse("잘못된 요청입니다.");
        return ApiResult.of(HttpStatus.BAD_REQUEST, msg, null);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<String> handleIllegalState(IllegalStateException ex) {
        return ApiResult.of(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<Void> handleUserNotFound(UserNotFoundException ex) {
        return ApiResult.of(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(InvalidRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Void> handleInvalidRole(InvalidRoleException ex) {
        return ApiResult.of(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<Void> handleUnexpected(Exception ex) {
        return ApiResult.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "회원가입에 실패했습니다. 잠시 후 다시 시도해주세요.",
                null
        );
    }
}
