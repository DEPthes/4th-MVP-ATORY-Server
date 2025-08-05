package ATORY.atory.domain.artist.dto;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDto;
import ATORY.atory.domain.post.dto.PostDto;
import ATORY.atory.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArtistWithPostDto {

    private Long id;
    private UserDto user;
    private String birth;
    private String educationBackground;
    private Boolean disclosureStatus;
    private Page<PostDto> post;

    @Builder
    public ArtistWithPostDto(Long id, UserDto user, String birth, String educationBackground, Boolean disclosureStatus,Page<PostDto> post) {
        this.id = id;
        this.user = user;
        this.birth = birth;
        this.educationBackground = educationBackground;
        this.disclosureStatus = disclosureStatus;
        this.post = post;
    }
}
