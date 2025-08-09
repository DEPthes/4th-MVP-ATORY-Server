package ATORY.atory.domain.artist.artistNote.mapper;

import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteDetailResponseDto;
import ATORY.atory.domain.artist.artistNote.dto.ArtistNoteListItemDto;
import ATORY.atory.domain.artist.artistNote.dto.ArtistSummaryDto;
import ATORY.atory.domain.artist.artistNote.entity.ArtistNote;
import ATORY.atory.domain.artist.entity.Artist;
import ATORY.atory.domain.user.entity.User;
import ATORY.atory.global.util.PhoneMasker;

public final class ArtistNoteMapper {

    private static final String DEFAULT_PROFILE = "https://static.atory.app/assets/default-profile.png"; //이미지

    private ArtistNoteMapper() {}

    public static ArtistNoteListItemDto toListItem(ArtistNote note) {
        Artist artist = note.getArtist();
        User user = (artist != null) ? artist.getUser() : null;

        ArtistSummaryDto artistSummary = toArtistSummary(user);
        String thumb = (note.getCoverImageUrl() != null && !note.getCoverImageUrl().isBlank())
                ? note.getCoverImageUrl()
                : (artistSummary.getProfileImageUrl() != null ? artistSummary.getProfileImageUrl() : DEFAULT_PROFILE);

        return ArtistNoteListItemDto.builder()
                .noteId(note.getId())
                .thumbnailUrl(thumb)
                .artist(artistSummary)
                .build();
    }

    public static ArtistNoteDetailResponseDto toDetail(ArtistNote note) {
        Artist artist = note.getArtist();
        User user = (artist != null) ? artist.getUser() : null;

        ArtistSummaryDto artistSummary = toArtistSummary(user);

        return ArtistNoteDetailResponseDto.builder()
                .noteId(note.getId())
                .year(note.getYear())
                .type(note.getArtistNoteType())
                .description(note.getDescription())
                .artist(artistSummary)
                .build();
    }

    private static ArtistSummaryDto toArtistSummary(User user) {
        if (user == null) {
            return ArtistSummaryDto.builder()
                    .id(null)
                    .nickname("Unknown")
                    .englishName(null)
                    .job("ARTIST")
                    .phone(null)
                    .email(null)
                    .statusMessage(null)
                    .profileImageUrl(DEFAULT_PROFILE)
                    .build();
        }

        return ArtistSummaryDto.builder()
                .id(user.getId())
                .nickname(user.getUsername())
                .englishName(user.getEnglishName())
                .job("ARTIST")
                .phone(PhoneMasker.mask(user.getContact()))
                .email(user.getEmail())
                .statusMessage(user.getIntroduction())
                .profileImageUrl(user.getProfileImgUrl() != null ? user.getProfileImgUrl() : DEFAULT_PROFILE)
                .build();
    }
}