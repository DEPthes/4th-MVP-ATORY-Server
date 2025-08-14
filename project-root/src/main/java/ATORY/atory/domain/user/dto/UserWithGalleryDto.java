package ATORY.atory.domain.user.dto;

import ATORY.atory.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Schema(description = "갤리러 기본 정보 DTO")
public class UserWithGalleryDto extends UserDto {
    @Schema(description = "갤러리 위치", example = "서울특별시 서대문구 거북골로 34")
    private String location;

}
