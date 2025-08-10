package ATORY.atory.domain.collector.dto;

import ATORY.atory.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class CollectorDto {
    private Long id;
    private UserDto user;
    private String birth;
    private String educationBackground;
    private Boolean disclosureStatus;
    private List<Object> notes; // 현재 컬렉터 노트가 없다면 빈 리스트

    @Builder
    public CollectorDto(Long id, UserDto user, String birth,
                                String educationBackground, Boolean disclosureStatus,
                                List<Object> notes) {
        this.id = id;
        this.user = user;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
        this.notes = notes;
    }
}
