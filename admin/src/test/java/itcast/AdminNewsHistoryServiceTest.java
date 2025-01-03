package itcast;

import itcast.application.AdminNewsHistoryService;
import itcast.domain.user.User;
import itcast.dto.response.AdminNewsHistoryResponse;
import itcast.jwt.repository.UserRepository;
import itcast.repository.AdminRepository;
import itcast.repository.NewsHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminNewsHistoryServiceTest {

    @Mock
    NewsHistoryRepository newsHistoryRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    AdminRepository adminRepository;
    @InjectMocks
    AdminNewsHistoryService adminNewsHistoryService;

    @Test
    @DisplayName("뉴스 히스토리 조회 성공")
    public void successNewsHistoryRetrieve() {
        //given
        Long userId = 1L;
        LocalDate testDate = LocalDate.of(2020, 1, 1);
        LocalDateTime testAt01 = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime testAt02 = LocalDateTime.of(2025, 1, 1, 23, 59, 59);
        int page = 0;
        int size = 20;

        User user = User.builder()
                .id(1L)
                .kakaoEmail("kakao@kakao.com")
                .build();

        List<AdminNewsHistoryResponse> responses = List.of(
                new AdminNewsHistoryResponse(
                        1L,
                        1L,
                        1L,
                        testAt01,
                        testAt01
                ),
                new AdminNewsHistoryResponse(
                        2L,
                        2L,
                        1L,
                        testAt02,
                        testAt02
                )
        );

        Pageable pageable = PageRequest.of(page, size);
        Page<AdminNewsHistoryResponse> newsHistoryPage = new PageImpl<>(responses, pageable, responses.size());

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(newsHistoryRepository.findNewsHistoryListByCondition(null, 1L, testDate, pageable)).willReturn(newsHistoryPage);

        //when
        Page<AdminNewsHistoryResponse> responsePage = adminNewsHistoryService.retrieveNewsHistory(userId, null, 1L, testDate, page, size);

        //then
        assertEquals(2, responsePage.getContent().size());
        assertEquals(1L, responsePage.getContent().get(0).userId());
        assertEquals(2L, responsePage.getContent().get(1).userId());
        assertEquals(page, responsePage.getNumber());
        assertEquals(size, responsePage.getSize());
        verify(newsHistoryRepository).findNewsHistoryListByCondition(null, 1L, testDate, pageable);
    }
}
