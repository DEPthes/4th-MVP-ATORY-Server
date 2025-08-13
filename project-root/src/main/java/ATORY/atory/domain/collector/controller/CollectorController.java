package ATORY.atory.domain.collector.controller;

import ATORY.atory.domain.collector.dto.CollectorDto;
import ATORY.atory.domain.collector.dto.CollectorWithPostDto;
import ATORY.atory.domain.collector.service.CollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collector")
public class CollectorController {

    private final CollectorService collectorService;

    @GetMapping("/{id}")
    public CollectorDto findCollectorById(@PathVariable Long id) {
        return collectorService.findCollectorById(id);
    }

    @GetMapping("/{id}/posts")
    public CollectorWithPostDto findPostsByCollector(@PathVariable Long id,
                                                     @RequestParam("type") String postType,
                                                     Pageable pageable) {
        return collectorService.findPostByCollector(id, postType, pageable);
    }

    @GetMapping("/{id}/posts/tags")
    public Object findPostTags(@PathVariable Long id,
                               @RequestParam("type") String postType) {
        return collectorService.findTagsByCollectorAndType(id, postType);
    }
}
