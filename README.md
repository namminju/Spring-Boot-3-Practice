<h1 align="center">🌱 Spring Boot 3 Practice</h1>

<p align="center">
    Spring Boot 3 기반의 모듈형 백엔드 학습 프로젝트입니다.<br/>
    REST API부터 인증, 보안, 테스트, Redis, JWT까지 단계적으로 실습합니다.
</p>

<p align="center">
    <img src="https://img.shields.io/badge/SpringBoot-3.4.5-success?style=flat-square&logo=springboot"/>
    <img src="https://img.shields.io/badge/Java-17-blue?style=flat-square&logo=java"/>
    <img src="https://img.shields.io/badge/Gradle-green?style=flat-square&logo=gradle"/>
</p>

---

## 🧭 프로젝트 개요

> **Spring Boot 3**의 핵심 기능을 단계적으로 실습하고,  
> **실무에 가까운 구조와 코드 패턴**을 이해하는 데 목적이 있습니다.  
> 주요 학습 범위는 아래와 같습니다.

- Spring MVC 기반 REST API 설계
- 인증/인가 (JWT + Spring Security)
- Redis를 활용한 토큰 저장 및 블랙리스트 처리
- 예외 처리, 커스텀 Validator 구현

---

## 🗂️ 프로젝트 구조
    src/main/
    ├── java/
    │ └── com/example/demo/
    │ ├── config/ # 설정 클래스 (Spring 설정, Redis 등)
    │ ├── controller/ # REST API 컨트롤러
    │ ├── domain/ # Entity 및 엔티티 관련 클래스
    │ ├── dto/ # Request, Response DTO
    │ ├── exception/ # 커스텀 예외 및 예외 처리 핸들러
    │ ├── filter/ # JWT 필터 등 서블릿 필터
    │ ├── jwt/ # JWT 생성 및 검증 유틸리티
    │ ├── repository/ # JPA Repository 인터페이스
    │ ├── service/ # 비즈니스 로직 구현
    │ ├── util/ # 유틸성 클래스 (ex: 토큰 유틸)
    │ ├── validation/ # 커스텀 Validator 및 어노테이션
    │ └── DemoApplication.java # 메인 애플리케이션 클래스
    ├── resources/
    └── application.yml # Spring Boot 설정 파일

 
---


## ⚙️ 사용 기술 스택

| 분야 | 기술 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3.4.5 |
| 웹 | Spring MVC, Spring Web |
| 데이터베이스 | H2, MySQL |
| 인증/보안 | Spring Security, JWT |
| 캐시/세션 | Redis |
| 개발 도구 | Gradle, Lombok, Bean Validation |


---

## 📌 목표

> 이 프로젝트는 단순한 기능 구현이 아닌,  
> **Spring 생태계의 개념을 이해하고 실전에 활용 가능한 수준으로 끌어올리는 것**을 목표로 합니다.

---

## 🙋‍♂️ 작성자

- GitHub: [namminju](https://github.com/namminju)
- Notion: https://carnation-garage-549.notion.site/Spring-Boot-1c01140a882281d797b6f38fe373a737

