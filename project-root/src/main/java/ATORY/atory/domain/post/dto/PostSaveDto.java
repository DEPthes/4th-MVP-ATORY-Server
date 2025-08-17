package ATORY.atory.domain.post.dto;

import ATORY.atory.domain.post.entity.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostSaveDto {
    private String title;
    private List<MultipartFile> images;
    private String url;
    private String description;
    private List<Long> tagIDs;
    private PostType postType;
}
