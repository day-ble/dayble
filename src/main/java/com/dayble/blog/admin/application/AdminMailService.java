package com.dayble.blog.admin.application;

import com.dayble.blog.admin.controller.dto.request.AdminSendMailRequest;
import com.dayble.blog.admin.controller.dto.response.MailResponse;
import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.global.exception.ErrorCodes;
import com.dayble.blog.mailEvent.MailEvents;
import com.dayble.blog.mailEvent.MailEventsRepository;
import com.dayble.blog.user.domain.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminMailService {

    private final MailEventsRepository mailEventsRepository;
    private final AdminCheckService adminCheckService;
    private final AdminEmailSender adminEmailSender;
    private final UserRepository userRepository;

    public Page<MailResponse> retrieveMailEvent(Long adminId, int page, int size) {
        adminCheckService.isAdmin(adminId);
        PageRequest pageable = PageRequest.of(page, size);
        Page<MailEvents> mailEventsPage = mailEventsRepository.findAll(pageable);

        Map<Pair<Long, LocalDate>, List<MailResponse.MailContent>> groupedData = mailEventsPage.getContent().stream()
                .collect(Collectors.groupingBy(
                        event -> Pair.of(event.getUser().getId(), event.getCreatedAt().toLocalDate()),
                        Collectors.mapping(
                                event -> new MailResponse.MailContent(
                                        event.getId(),
                                        event.getTitle(),
                                        event.getSummary(),
                                        event.getOriginalLink(),
                                        event.getThumbnail()
                                ),
                                Collectors.toList()
                        )
                ));

        List<MailResponse> mailResponses = groupedData.entrySet().stream()
                .map(entry -> new MailResponse(
                        entry.getKey().getLeft(),
                        entry.getKey().getRight(),
                        entry.getValue()
                ))
                .sorted(Comparator
                        .comparing(MailResponse::createdAt, Comparator.reverseOrder())
                        .thenComparing(MailResponse::userId))
                .toList();

        return new PageImpl<>(mailResponses, pageable, mailEventsPage.getTotalElements());
    }

    public void sendMailEvent(Long adminId, Long userId, LocalDate createdAt) {
        adminCheckService.isAdmin(adminId);

        LocalDateTime startOfDay = createdAt.atStartOfDay();
        LocalDateTime endOfDay = createdAt.atTime(23, 59, 59);

        List<MailEvents> mailEvents = mailEventsRepository.findByUserIdAndCreatedAtBetween(userId, startOfDay,
                endOfDay);
        if (mailEvents.isEmpty()) {
            throw new DaybleApplicationException(ErrorCodes.EMAIL_EVENT_NOT_FOUND);
        }

        String userEmail = userRepository.findEmailById(userId)
                .orElseThrow(() -> new DaybleApplicationException(ErrorCodes.USER_EMAIL_NOT_FOUND));

        List<AdminSendMailRequest.MailContent> mailContents = mailEvents.stream()
                .map(event -> new AdminSendMailRequest.MailContent(
                        event.getId(),
                        event.getTitle(),
                        event.getSummary(),
                        event.getOriginalLink(),
                        event.getThumbnail()
                ))
                .collect(Collectors.toList());

        AdminSendMailRequest sendMailRequest = new AdminSendMailRequest(
                userEmail,
                mailContents
        );

        adminEmailSender.send(sendMailRequest);
    }
}
