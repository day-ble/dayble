
# 🖥️ IT-CAST README

## 프로젝트 소개

- **진행 기간**: `2024-12-02` ~ `2025-01-06`
- **매일 문자나 이메일로 최신 IT 컨텐츠를 발송하는 서비스**입니다.
- 기술 발전 속도가 빠르게 변화하는 IT 업계의 트렌드를 한눈에 파악하고,
사용자에게 필요한 정보를 신속하게 전달하고자 합니다.
- 구독 시 전달 방식(이메일, 문자)을 선택할 수 있으며, 구독 유형(IT블로그, IT뉴스) 선택, 관심분야(프론트, 백엔드)를 선택할 수 있습니다.
- 더 좋은 품질을 위하여 admin으로 발송할 IT컨텐츠들을 미리 검수합니다.
  <br>

---

## 팀원 구성

<div align="center">

|                                          **유선준**                                           | **김신희** | **김지혜** | **권준석** | **최서영** |
|:------------------------------------------------------------------------------------------:|  :------: | :------: | :------: | :------: |
| <img src="https://github.com/SeonJuuuun.png" height="150"> <br/> [@SeonJuuuun](https://github.com/SeonJuuuun) | <img src="https://github.com/shinheekim.png" height="150"> <br/> [@shinheekim](https://github.com/shinheekim) | <img src="https://github.com/Hamiwood.png" height="150"> <br/> [@Hamiwood](https://github.com/Hamiwood) | <img src="https://github.com/junseok708.png" height="150"> <br/> [@junseok708](https://github.com/junseok708) | <img src="https://github.com/seoyeong-4811.png" height="150"> <br/> [@seoyeong-4811](https://github.com/seoyeong-4811) |

</div>

<br>

---

## 채택한 개발 기술과 브랜치 전략

![Java17](https://img.shields.io/badge/Java17-007396?style=for-the-badge&logo=java&logoColor=white)
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-6DB33F?style=for-the-badge&logo=QueryDSL&logoColor=white)
![WebClient](https://img.shields.io/badge/WebClient-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![AWSSES](https://img.shields.io/badge/AWSSES-FF9900?style=for-the-badge&logo=amazonSES&logoColor=white)
![CoolSMSSDK](https://img.shields.io/badge/CoolSMSSDK-FF9900?style=for-the-badge&logo=sms&logoColor=white)
![OpenCSV](https://img.shields.io/badge/OpenCSV-FF9900?style=for-the-badge&logo=csv&logoColor=white)
![Jsoup](https://img.shields.io/badge/Jsoup-FF9900?style=for-the-badge&logo=jsoup&logoColor=white)
![Kakao](https://img.shields.io/badge/Kakao-FFCD00?style=for-the-badge&logo=kakao&logoColor=white)
![OpenAIAPI](https://img.shields.io/badge/OpenAIAPI-412991?style=for-the-badge&logo=openai&logoColor=white)
![GitHubActions](https://img.shields.io/badge/GitHubActions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![EC2](https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![RDS](https://img.shields.io/badge/RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)
![ElastiCache](https://img.shields.io/badge/ElastiCache-FF4F8B?style=for-the-badge&logo=amazonelasticache&logoColor=white)
![S3](https://img.shields.io/badge/S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![CodeDeploy](https://img.shields.io/badge/CodeDeploy-6DB33F?style=for-the-badge&logo=awscodedeploy&logoColor=white)
![DockerCompose](https://img.shields.io/badge/DockerCompose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white)
![Loki](https://img.shields.io/badge/Loki-4A4A55?style=for-the-badge&logo=loki&logoColor=white)
![Promtail](https://img.shields.io/badge/Promtail-4A4A55?style=for-the-badge&logo=promtail&logoColor=white)
![Logback](https://img.shields.io/badge/Logback-6DB33F?style=for-the-badge&logo=logback&logoColor=white)


### 개발 환경

- **협업 툴**: Slack, Notion
- **Backend**: Java, Spring Boot, JPA
- **Database**: MySQL
- **버전 및 이슈관리**: Git, GitHub, GitHub Issues

### 브랜치 전략

- Git-flow 전략을 활용하여 **main**, **develop**, **issue** 브랜치를 관리했습니다.
    - **main**: 배포 버전을 관리하는 브랜치
    - **develop**: 개발 단계에서 통합되는 브랜치
    - **issue**: 기능별로 작업 후 develop 브랜치에 병합

<br>

---

## 프로젝트 설계

### 와이어프레임

#### 메인 페이지
![메인 페이지 (1)](https://github.com/user-attachments/assets/415a9bdb-f3cf-4044-8801-9e848837d121)

#### 구독 페이지
![구독 페이지 (1)](https://github.com/user-attachments/assets/0d14906a-34bb-4ee9-8f07-9350f70c3279)

### ERD (Entity Relationship Diagram)

서비스의 데이터베이스 구조는 아래 ERD에 기반합니다. 각 엔터티와 관계를 통해 효율적인 데이터 관리를 고려했습니다.

![img_1.png](img_1.png)
<br>

---

## 프로젝트 구조

```
├─common   // common 담당 모듈
│  ├─itcast
│  │   ├─domain
│  │   │   ├─user
│  │   │   │   └─User.java
│  │   │   ├─blog
│  │   │   │   └─Blog.java
│  │   │   └─BaseEntity.java // Timestamped.java
│  │   ├─config
│  │   ├─exception
│  │   ├─jwt
│  │   └─logging
│  └─ResponseTemplate.java
├─b2c   
│   ├─auth
│   │  ├─client
│   │  ├─controller
│   |  | └─ controller.java
│   |  ├─dto
│   |  |  ├─request
│   |  |  └─response
│   |  └─ application
│   |      └─ service.java
│   ├─user
│   │  ├─repository
│   │  ├─controller
│   |  | └─ controller.java
│   |  ├─dto
│   |  |  ├─request
│   |  |  └─response
│   └─ └─ application
│         └─ service.java
├─schedule   // 스케쥴링 담당 모듈
│   ├─repository
│   ├─dto
│   |  ├─request
│   |  └─response
│   ├─application
│   |   └─ service.java
│   └─ └─ application
│          └─ service.java
└─admin         // 어드민 담당 모듈
    ├─repository
		...
```
<br>

---

## 모듈별 기능

### B2C 모듈
* 소셜 로그인
* 회원 정보 작성, 수정, 회원 탈퇴
* 휴대폰 번호 인증, 이메일 인증
* 로깅 모니터링

### Schedule 모듈
* 블로그/뉴스 크롤링 스케쥴
* 블로그/뉴스 발송 날짜 선택 스케쥴
* 블로그/뉴스 발송 스케쥴 (이메일, 문자)
* 로깅 모니터링 및 AI 요약 기능

### Admin 모듈
*  관리자 블로그, 뉴스 검수 기능(CRUD)
*  관리자 히스토리 관리 기능(조회, CSV 다운로드 및 메일 발송)
*  로깅 모니터링


---


## 프로젝트 실행 방법

#### 1. 프로젝트를 clone 받기
```shell
$git clone https://github.com/crawling-project-crowrong/it-cast.git
```
#### 2. Docker Compose로 개발 환경 구성
```shell
$docker-compose up -d
```
#### 3. 애플리케이션 실행
