package itcast;

import itcast.application.AdminCheckService;
import itcast.application.AdminMailService;
import itcast.domain.mailEvent.MailEvents;
import itcast.domain.user.User;
import itcast.dto.response.MailResponse;
import itcast.mail.repository.MailEventsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AdminMailServiceTest {

    @Mock
    private MailEventsRepository mailEventsRepository;

    @Mock
    private AdminCheckService adminCheckService;

    @InjectMocks
    private AdminMailService adminMailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("메일 조회 성공 테스트")
    void SuccessRetrieveMailEvents() {
        // Given
        Long adminId = 1L;
        int page = 0;
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size);

        User user1 = User.builder()
                .id(1L)
                .kakaoEmail("kakao@kakao.com")
                .build();
        User user2 = User.builder()
                .id(2L)
                .kakaoEmail("kakao@kakao.com")
                .build();

        MailEvents event1 = MailEvents.of(user1, "제목1", "요약1", "링크1", "썸네일1");
        MailEvents event2 = MailEvents.of(user2, "제목2", "요약2", "링크2", "썸네일2");

        List<MailEvents> mailEventsList = Arrays.asList(event1, event2);
        Page<MailEvents> mailEventsPage = new PageImpl<>(mailEventsList, pageable, mailEventsList.size());

        when(mailEventsRepository.findAll(pageable)).thenReturn(mailEventsPage);
        doNothing().when(adminCheckService).isAdmin(adminId);

        // When
        Page<MailResponse> result = adminMailService.retrieveMailEvent(adminId, page, size);

        // Then
        verify(adminCheckService).isAdmin(adminId);
        verify(mailEventsRepository).findAll(pageable);

        // 검증 로직 수정
        assertEquals(2, result.getContent().size());

        MailResponse response1 = result.getContent().get(0);
        assertEquals(1L, response1.userId());
        assertEquals(1, response1.contents().size());

        MailResponse response2 = result.getContent().get(1);
        assertEquals(2L, response2.userId());
        assertEquals(1, response2.contents().size());
    }
}
