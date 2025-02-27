package com.dayble.blog.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.dayble.blog.admin.application.AdminBlogHistoryService;
import com.dayble.blog.admin.controller.dto.response.AdminBlogHistoryResponse;
import com.dayble.blog.admin.domain.AdminRepository;
import com.dayble.blog.blogHistory.domain.BlogHistoryRepository;
import com.dayble.blog.user.domain.User;
import com.dayble.blog.user.domain.UserRepository;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class AdminBlogHistoryServiceTest {

    @Mock
    BlogHistoryRepository blogHistoryRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    AdminRepository adminRepository;
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    AdminBlogHistoryService adminBlogHistoryService;

    @Test
    @DisplayName("블로그 히스토리 조회 성공")
    public void SuccessBlogHistoryRetrieve() {
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

        List<AdminBlogHistoryResponse> responses = List.of(
                new AdminBlogHistoryResponse(
                        1L,
                        1L,
                        1L,
                        testAt01,
                        testAt01
                ),
                new AdminBlogHistoryResponse(
                        2L,
                        2L,
                        1L,
                        testAt02,
                        testAt02
                )
        );

        Pageable pageable = PageRequest.of(page, size);
        Page<AdminBlogHistoryResponse> blogHistoryPage = new PageImpl<>(responses, pageable, responses.size());

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(blogHistoryRepository.findBlogHistoryListByCondition(null, 1L, testDate, pageable)).willReturn(blogHistoryPage);

        //when
        Page<AdminBlogHistoryResponse> responsePage = adminBlogHistoryService.retrieveBlogHistory(userId, null, 1L, testDate, page, size);

        //then
        assertEquals(2, responsePage.getContent().size());
        assertEquals(1L, responsePage.getContent().get(0).userId());
        assertEquals(2L, responsePage.getContent().get(1).userId());
        assertEquals(page, responsePage.getNumber());
        assertEquals(size, responsePage.getSize());
        verify(blogHistoryRepository).findBlogHistoryListByCondition(null, 1L, testDate, pageable);
    }

    @Test
    @DisplayName("블로그 히스토리 CSV 파일 다운로드 성공")
    public void successBlogHistoryDownloadCSV() {
        // given
        Long adminId = 1L;
        Long userId = null;
        Long blogId = null;
        LocalDate startAt = LocalDate.of(2025, 1, 1);
        LocalDate endAt = LocalDate.of(2025, 1, 1);

        User user = User.builder()
                .id(adminId)
                .kakaoEmail("admin@email.com")
                .build();
        List<AdminBlogHistoryResponse> blogHistoryResponse = List.of(
                new AdminBlogHistoryResponse(1L, 1L, 1L,
                        LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2025, 1, 1, 0, 0, 0)),
                new AdminBlogHistoryResponse(2L, 2L, 1L,
                        LocalDateTime.of(2025, 1, 1, 23, 59, 59),
                        LocalDateTime.of(2025, 1, 1, 23, 59, 59))
        );

        given(userRepository.findById(adminId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(blogHistoryRepository.downloadBlogHistoryListByCondition(userId, blogId, startAt, endAt)).willReturn(blogHistoryResponse);

        // when
        String csv = adminBlogHistoryService.createCsvFile(adminId, userId, blogId, startAt, endAt);

        // then
        Assertions.assertNotNull(csv);
        Assertions.assertTrue(csv.contains("\"id\",\"userId\",\"blogId\",\"createdAt\",\"modifiedAt\""));
        Assertions.assertTrue(csv.contains("\"1\",\"1\",\"1\",\"2025-01-01T00:00\",\"2025-01-01T00:00\""));
        Assertions.assertTrue(csv.contains("\"2\",\"2\",\"1\",\"2025-01-01T23:59:59\",\"2025-01-01T23:59:59\""));
    }

    @Test
    @DisplayName("블로그 히스토리 CSV 파일 메일 전송 성공")
    public void successBlogHistorySendMailCSV() throws MessagingException, IOException {
        //given
        byte[] csvFile = "id,userId,blogsId,createdAt,modifiedAt\n1,1,1,2025-01-01T00:00,2025-01-01T23:59:59".getBytes();
        String title = "[관리자 전용 발신] 블로그 히스토리 CSV 파일";
        String to = "hamiwood@naver.com";
        String fileName = "BlogHistory_File(" + LocalDate.now() + ").csv";

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // when
        adminBlogHistoryService.sendEmail(csvFile);

        // then
        assertThat(mimeMessage.getSubject()).isEqualTo(title);
        assertThat(mimeMessage.getAllRecipients()[0].toString()).isEqualTo(to);

        Multipart multipart = (Multipart) mimeMessage.getContent();
        BodyPart attachmentPart = multipart.getBodyPart(1);
        assertThat(attachmentPart.getFileName()).isEqualTo(fileName);
    }
}
