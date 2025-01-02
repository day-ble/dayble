package itcast.mail.application;

import itcast.mail.dto.request.MailContent;
import itcast.mail.dto.request.SendMailRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailIntegrationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MailIntegrationServiceTest.class);

    @Autowired
    private MailService mailService;

    @Test
    void send_async_test() throws InterruptedException {
        log.info("Test 시작 스레드: {}", Thread.currentThread().getName());
        final SendMailRequest sendMailRequest = new SendMailRequest(
                List.of("seonjun0906@gmail.com", "sunjun0000@naver.com"),
                List.of(new MailContent("2025년 쿠버네티스 표준 아키텍처 | 요즘IT",
                                "2025년 쿠버네티스 표준 아키텍처는 성숙한 쿠버네티스 생태계를 기반으로 하여 다양한 도구 및 제품을 소개하고 있으며 이를 통해 쿠버네티스를 효과적으로 운영할 수 있는 방법을 제시합니다. 쿠버네티스와 관련된 여러 오픈 소스 및 관리형 제품을 이해하고 선택하는 것이 중요하다는 점을 강조하며 특정 도구들이 현재 시장에서의 트렌드와 비즈니스 요구를 반영하고 있음을 설명하고 있습니다.",
                                "https://yozm.wishket.com/magazine/detail/2900/",
                                "https://yozm.wishket.com/media/news/2869/image3.gif")
                        , new MailContent("2025년 쿠버네티스 표준 아키텍처 | 요즘IT",
                                "2025년 쿠버네티스 표준 아키텍처는 성숙한 쿠버네티스 생태계를 기반으로 하여 다양한 도구 및 제품을 소개하고 있으며 이를 통해 쿠버네티스를 효과적으로 운영할 수 있는 방법을 제시합니다. 쿠버네티스와 관련된 여러 오픈 소스 및 관리형 제품을 이해하고 선택하는 것이 중요하다는 점을 강조하며 특정 도구들이 현재 시장에서의 트렌드와 비즈니스 요구를 반영하고 있음을 설명하고 있습니다.",
                                "https://yozm.wishket.com/magazine/detail/2900/",
                                "https://yozm.wishket.com/media/news/2891/image4.png"),
                        new MailContent("2025년 쿠버네티스 표준 아키텍처 | 요즘IT",
                                "2025년 쿠버네티스 표준 아키텍처는 성숙한 쿠버네티스 생태계를 기반으로 하여 다양한 도구 및 제품을 소개하고 있으며 이를 통해 쿠버네티스를 효과적으로 운영할 수 있는 방법을 제시합니다. 쿠버네티스와 관련된 여러 오픈 소스 및 관리형 제품을 이해하고 선택하는 것이 중요하다는 점을 강조하며 특정 도구들이 현재 시장에서의 트렌드와 비즈니스 요구를 반영하고 있음을 설명하고 있습니다.",
                                "https://yozm.wishket.com/magazine/detail/2900/",
                                "https://yozm.wishket.com/media/news/2872/cover.webp")));

        mailService.send(sendMailRequest);

        Thread.sleep(1000);
        log.info("Test 끝 스레드: {}", Thread.currentThread().getName());
    }
}
