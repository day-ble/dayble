package itcast.application;

import com.opencsv.CSVWriter;
import itcast.domain.user.User;
import itcast.dto.response.AdminBlogHistoryResponse;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.jwt.repository.UserRepository;
import itcast.repository.AdminRepository;
import itcast.repository.BlogHistoryRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBlogHistoryService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final BlogHistoryRepository blogHistoryRepository;
    private final JavaMailSender mailSender;

    public Page<AdminBlogHistoryResponse> retrieveBlogHistory(Long adminId, Long userId, Long blogId, LocalDate createdAt,
                                                              int page, int size
    ) {
        isAdmin(adminId);
        Pageable pageable = PageRequest.of(page, size);
        return blogHistoryRepository.findBlogHistoryListByCondition(userId, blogId, createdAt, pageable);
    }

    public String createCsvFile(Long adminId, Long userId, Long blogId, LocalDate startAt, LocalDate endAt) {
        isAdmin(adminId);
        List<AdminBlogHistoryResponse> blogHistoryList = blogHistoryRepository.downloadBlogHistoryListByCondition(userId, blogId, startAt, endAt);
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        String[] header = new String[]{"id", "userId", "blogId", "createdAt", "modifiedAt"};
        csvWriter.writeNext(header);

        for (AdminBlogHistoryResponse response : blogHistoryList) {
            String[] data = {
                    String.valueOf(response.id()),
                    String.valueOf(response.userId()),
                    String.valueOf(response.blogId()),
                    String.valueOf(response.createdAt()),
                    String.valueOf(response.modifiedAt())
            };
            csvWriter.writeNext(data);
        }

        try {
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringWriter.toString();
    }

    public void sendEmail(byte[] csvFile) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String fileName = "BlogHistory_File(" + LocalDate.now() + ").csv";
        String title = "[관리자 전용 발신] 블로그 히스토리 CSV 파일";
        String content = "첨부된 CSV 파일을 확인해주십시오.";
        String to = "hamiwood@naver.com";

        helper.setFrom("hamiwood@naver.com");
        helper.setTo(to);
        helper.setSubject(title);
        helper.setText(content, false);

        helper.addAttachment(fileName, new ByteArrayResource(csvFile));
        mailSender.send(message);
    }

    private void isAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ItCastApplicationException(ErrorCodes.USER_NOT_FOUND));
        String email = user.getKakaoEmail();
        if (!adminRepository.existsByEmail(email)) {
            throw new ItCastApplicationException(ErrorCodes.INVALID_USER);
        }
    }
}
