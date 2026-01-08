<div align="center">
    <div>
        <img width="100%" src="https://github.com/user-attachments/assets/92ab6374-7ee1-44eb-801d-89fe72440edd">
    </div>
    <div>
        <h1 style="margin: 20px 0;">TODO의 자율 주행, $\huge{\color{#29FF74}Memento}$</h1>
        <p><strong>Memento</strong>는 사용자가 할 일을 입력하면 AI가 이를 자동으로 정렬하고 최적의 타임라인을 생성하는 <strong>자율주행 개념의 생산성 서비스</strong>입니다.</p>
    </div>
</div>

## 🖤 ScreenShot 🖤
                
| <img width="250" src="https://github.com/user-attachments/assets/6b5c84a0-39d5-4d2a-90ad-265455a0cf9c"/> | <img width="250" src="https://github.com/user-attachments/assets/a2bc93a0-12b4-455c-b882-2db300ce6c9b"/> | <img width="250" src="https://github.com/user-attachments/assets/a8416913-599f-4eac-b27e-f5ca1e2565e6"/> |
|:----------------------:|:----------------------:|:----------------------:|
|         **온보딩**         |         **투데이뷰**         |         **투두뷰**         |

| <img width="250" src="https://github.com/user-attachments/assets/8f78fdeb-1bbb-409b-94e0-a2d6815e3137"/> | <img width="250" src="https://github.com/user-attachments/assets/a01f7387-2276-4ec3-9a77-79f5f296d3ac"/> | <img width="250" src="https://github.com/user-attachments/assets/5b924378-7250-4db6-beaf-a153e4ea4094"/> |
|:----------------------:|:----------------------:|:----------------------:|
|         **투두추가**         |         **일정추가**         |         **브레인덤뷰**         |

| <img width="250" src="https://github.com/user-attachments/assets/e8a7df8a-5fab-4ce8-a648-5a5ebf382240"/> | <img width="250" src="https://github.com/user-attachments/assets/0d1fbcb5-5c1c-441b-9ca0-f841afbd062d"/> |
|:----------------------:|:----------------------:|
|     **투두수정/삭제**     |     **일정수정/삭제**     |


<br>

# <img width="25" height="25" src="https://github.com/user-attachments/assets/6b10d8a9-7905-4400-94ee-7c609a65271f"> Conventions
[💻 Code Convention](https://testmanzi.notion.site/Coding-Convention-16667bb5c6cf80ba8a36c5caf1575f2e?pvs=4)<br>
[📝 Git Convention](https://testmanzi.notion.site/Git-Convention-16667bb5c6cf80cea652f08ca14961db?pvs=4)<br>
[👀 Branch Strategy](https://testmanzi.notion.site/Branch-Strategy-16667bb5c6cf80bfa675c1b726cbb086?pvs=4)

<br>

# <img width="25" height="25" src="https://github.com/user-attachments/assets/077607bd-5ee2-4411-adf3-efb53e892b5b"> API 명세서
[🔥 계속해서 개발 중입니다!](https://testmanzi.notion.site/API-16667bb5c6cf8036a7b9e192aa0e176c?pvs=4)

<br>

# 🔗 설계 (25.01.17 기준)

<details>
<summary>인프라 구조 설계</summary>
<div markdown="1">

![인프라 구조 설계](https://github.com/user-attachments/assets/c96739f0-d2a0-4bd2-a640-2e09a0d8bb1b)

</div>
</details>

<details>
<summary>ERD 설계</summary>
<div markdown="1">

![ERD 설계](https://github.com/user-attachments/assets/462b9551-bd9c-45fd-a0ab-e01605a238f9)

</div>
</details>

<br>

# 📂 Foldering (25.03.13 기준)
```
📁 src
└── 📁 main
    ├── 📁 java
    │   └── 📁 com
    │       └── 📁 official
    │           └── 📁 memento
    │               ├── 📁 auth
    │               ├── 📁 global
    │               ├── 📁 member
    │               ├── 📁 orderinfo
    │               └── 📁 schedule
    │                   ├── 📁 controller
    │                   │   ├── 📁 dto
    │                   │   │   ├── 📁 request
    │                   │   │   └── 📁 response
    │                   │   ├── 📄 ScheduleApiController
    │                   │   └── 📄 ScheduleApiDocs
    │                   ├── 📁 domain
    │                   │   ├── 📁 entity
    │                   │   ├── 📁 enums
    │                   │   └── 📄 ScheduleRepository
    │                   ├── 📁 infrastructure
    │                   │   └── 📁 persistence
    │                   │       ├── 📁 projection
    │                   │       │   ├── 📄 ScheduleEntity
    │                   │       │   └── 📄 ScheduleEntityJpaRepository
    │                   │       └── 📄 ScheduleRepositoryAdapter
    │                   ├── 📁 service
    │                   │   ├── 📁 command
    │                   │   ├── 📁 usecase
    │                   │   └── 📄 ScheduleService
    │                   ├── 📁 tag
    │                   ├── 📁 todo
    │                   └── 📄 HealthCheckApi
    ├── 📁 kotlin
    └── 📁 resources

```

<br>

# <img width="25" height="25" src="https://github.com/user-attachments/assets/f3d15d7c-b256-489d-b850-cfc364d48f84"> 고민 사항
<details>
<summary>우리 서비스가 TPS 1000 이 된다 가정하고, 톰캣, 스프링 설정 값 고민해보기 & 성능에 대한 고민</summary>
<div markdown="1">

### 우리 서비스가 TPS 1000 이 된다 가정하고, 톰캣, 스프링 설정 값 고민해보기

- 현재 API당 짧게 걸리는 APi는 50ms 부터 800ms 까지 다양합니다.
- 시간이 오래걸리는 애플 켈린더 동기화의 경우 비동기 처리를 하여 워커 쓰레드를 오랫동안 잡고 있는 상황을 방지하고자 합니다.
- 자주 발생하는 api의 경우 50ms부터 200ms 까지 걸리는데 이걸 고려할 경우 평균 0.125ms 정도 걸린다고 하면 125개정도의 워커 쓰레드가 필요합니다. 여유를 두기 위해 조금 더 여유롭게 설정한다면 워커 쓰레드의 갯수는 150~160개 정도로 설정하는게 좋을 거라 생각합니다.
- 디비 커넥션 역시 워커쓰레드와 비슷한 숫자로 설정을 하지만 가장 많이 발생하는 API인 개인 일정 조회를 캐시를 사용하여 디비와 통신하는 수를 줄이고자 합니다.
- 추가적으로
    - 요청대기열은 TPS의 50~100퍼센트로 설정한다.

### 성능에 대한 고민

- 우리 서비스는 개인 캘린더 서비스이기 때문에 동시성을 크게 고려할 필요 없다. 따라서 스케일 아웃보다는 트래픽 증가시 스케일 업을 통해 서버 성능을 개선 시킬 예정입니다.
- AI서버와, 일절 시간을 계산하는 배치 서버, 푸쉬 알람 서버를 분리하여 서버의 책임을 분리하여 한개의 서버가 갖는 일을 최대한 분리할 예정입니다.
- 그리고 캘린더 서비스 이기 때문에 쓰기보다는 읽기 작업이 훨씬 많이 일어나기 때문에 개인 일정 같은경우 변경사항을 바로 적용하는 것이 아니라 캐쉬를 통해 저장후 일정 시간에 저장 사항을 반영하는 방식으로 데이터베이스와의 연결을 줄이고자 합니다.

</div>
</details>

<br>

# <img width="25" height="25" src="https://github.com/user-attachments/assets/01d71526-e294-435f-aed8-b9a0eb74d8e9"> Memento 상세 설명
[🔥 자세한 기능을 보고싶다면?](https://chipped-alto-898.notion.site/Memento-1b56728a830e801eaab0c0e2c6b78f45?pvs=4)
