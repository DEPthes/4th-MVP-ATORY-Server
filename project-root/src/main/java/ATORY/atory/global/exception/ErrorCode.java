package ATORY.atory.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생하였습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Request Body를 통해 전달된 값이 유효하지 않습니다."),
    SER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 업로드 실패"),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 삭제 실패"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not found"),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "Tag not found");


    private final HttpStatus httpStatus;
    private final String message;
}
