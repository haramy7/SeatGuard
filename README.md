# SeatGuard

학생/직원의 자리 상태를 실시간으로 모니터링하고, 장기 이석 시 관리자에게 알림을 보내는 출석 관리 시스템입니다.

---

## 주요 기능

### 자동 자리 감지
- 마우스, 키보드, 스크롤 등 활동이 3분간 없으면 자동으로 '자리 비움' 처리
- 10분 이상 지속되면 '장기 이석' 상태로 전환
- 활동 감지 시 즉시 '재석' 상태로 복귀

### 수동 상태 변경
- 화장실, 외출 등 자리를 비울 때 직접 버튼으로 상태 변경 가능

### 실시간 대시보드
- 관리자 화면에서 전체 인원의 상태를 실시간 확인
- 재석(초록), 자리비움(회색), 장기이석(빨강) 시각적 구분

### 푸시 알림 (FCM)
- 장기 이석 발생 시 관리자 스마트폰으로 푸시 알림 전송
- "OOO님이 10분째 자리를 비웠습니다"

### 통계 대시보드
- 오늘 자리비움 시간 TOP 5
- 평균 자리비움 시간
- 현재 출석률
- 시간대별 자리비움 히트맵

---

## 기술 스택

| 구분 | 기술 |
|------|------|
| Backend | Spring Boot 3.2, Java 17 |
| Frontend | Thymeleaf, Vanilla JS |
| Database | PostgreSQL 15 |
| Push | Firebase Cloud Messaging |
| Deploy | Docker, Railway |

---

## 프로젝트 구조
```
src/main/
├── java/com/seatguard/
│   ├── config/          # Firebase, WebMvc 설정
│   ├── controller/      # API, 페이지 컨트롤러
│   ├── dto/             # 데이터 전송 객체
│   ├── interceptor/     # 로그인 인터셉터
│   ├── model/           # JPA 엔티티
│   ├── repository/      # 데이터 접근 계층
│   └── service/         # 비즈니스 로직
└── resources/
    ├── static/          # CSS, JS, 이미지
    ├── templates/       # Thymeleaf 템플릿
    └── application.yml  # 설정 파일
```

---

## 로컬 실행

### 1. PostgreSQL 실행
```bash
docker-compose up -d
```

### 2. 애플리케이션 실행
```bash
mvn spring-boot:run
```

### 3. 접속
```
http://localhost:8090
```

---

### GitHub에 푸시
```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/haramy7/SeatGuard.git
git push -u origin main
```

## DB 스키마

### users
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | BIGINT | PK |
| login_id | VARCHAR | 로그인 아이디 |
| password | VARCHAR | 비밀번호 |
| name | VARCHAR | 이름 |
| role | ENUM | STUDENT, ADMIN |
| current_status | VARCHAR | IN, AWAY, DANGER |
| fcm_token | VARCHAR | 푸시 토큰 |

### attendance_logs
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | BIGINT | PK |
| user_id | BIGINT | FK → users |
| status | VARCHAR | 상태값 |
| recorded_at | TIMESTAMP | 기록 시간 |

---

## API 명세

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | /login | 로그인 |
| POST | /signup | 회원가입 |
| GET | /logout | 로그아웃 |
| POST | /api/status | 상태 업데이트 |
| GET | /api/admin/dashboard | 실시간 현황 |
| GET | /api/admin/stats | 통계 데이터 |
| POST | /api/fcm/token | FCM 토큰 등록 |

---

## 라이선스

이다겸 허태훈
