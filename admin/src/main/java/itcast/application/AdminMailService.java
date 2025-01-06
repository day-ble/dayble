package itcast.application;

import itcast.domain.mailEvent.MailEvents;
import itcast.dto.response.MailResponse;
import itcast.mail.repository.MailEventsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMailService {

    private final MailEventsRepository mailEventsRepository;
    private final AdminCheckService adminCheckService;

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
}