<h1 align="center"> E101 자율 프로젝트 </h1>

## 📝 목차

[프로젝트 개요](#item-one)

[역할 분담](#item-two)

[기술 스택](#item-three)

[서버 아키텍처](#item-four)

[서비스 구현 화면](#item-five)

[느낀 점](#item-end)

## 프로젝트 개요

<a id="item-one"></a>

- <strong>진행 기간</strong>: 2023.10.09 ~ 2023.11.17

- <strong>목표</strong>
  
  - 문서 정리 + 웹 겁색 + GPT 서비스를 한 페이지에서 한번에 사용할 수 있도록 편의성 제공
  
  - 트리 구조로 문서를 한 눈에 확인
  
  - 그래프 구조를 통해 문서 간 유사도를 연결 + 공유 받은 문서와 내 문서를 연결
  
  - 내가 정리한 문서를 AI가 퀴즈로 만들어줘 복습을 간편하게 할 수 있도록 편의성 제공

## 역할 분담

<a id="item-two"></a> 

#### <strong>권인식</strong> - 팀장, BE : MSA 설계 ,Auth Server, Gpt Server

#### <strong>심규렬</strong> - BE : Editor Server, Search Server

#### <strong>최상익</strong> - BE : Diagram Server, Quiz Server

#### <strong>윤자현</strong> - FE : Auth, Quiz, WebSocket, UX/UI

#### <strong>이가영</strong> - FE : Interactive, Diagram, GPT, Search, UX/UI

#### <strong>이세울</strong> - FE : Editor, Tip Tab, WebSocket, UX/UI

## 기술 스택

<a id="item-three"></a>

## **⚙** Management Tool

- 이슈관리 : JIRA

- 형상관리 : Gitlab

- 코드리뷰 : Gerrit

- 커뮤니케이션 : Notion, Mattermost

- 디자인 : Figma

- UCC : 모바비

## 💻 IDE

- VS Code : 1.18.1

- IntelliJ : 11.0.19

## 📱 Frontend

- Next : 13.5.6

- axios : ^1.6.0

- Node.js : 18.16.1

- stompjs:7.0.0

- react-query: ^3.39.3

- jotai:^2.5.1

## 💾 Backend

- Springboot : 3.1.5

- Lombok

- Spring Data JPA

- Spring Data Redis(lecttuce)

- Spring Web

- SERVER : AWS EC2 Ubuntu 20.04.6 LTS

- DB : MySQL 8.0.33, Redis - sentinal, Mongo DB

- websocket

- openfeign

- S3

## 🔃 DevOPS

- Nginx

- Docker

- Jenkins

## Architecture

<a id="item-four"></a>

![image.png](README_assets/73d0eb2c4e0f672b49e231b2767145e6c75e0960.png)

## 프로젝트 구조도

<a id="item-five"></a>

<details>
<summary>FrontEnd</summary>

```
📦src
 ┣ 📂api
 ┃ ┣ 📂instances
 ┃ ┃ ┗ 📜api.ts
 ┃ ┗ 📂service
 ┃ ┃ ┣ 📜diagram.ts
 ┃ ┃ ┣ 📜editor.ts
 ┃ ┃ ┣ 📜quiz.ts
 ┃ ┃ ┗ 📜user.ts
 ┣ 📂app
 ┃ ┣ 📂editor
 ┃ ┃ ┣ 📂[id]
 ┃ ┃ ┃ ┗ 📜page.tsx
 ┃ ┃ ┗ 📜layout.tsx
 ┃ ┣ 📂main
 ┃ ┃ ┗ 📜page.tsx
 ┃ ┣ 📂quiz
 ┃ ┃ ┣ 📂maker
 ┃ ┃ ┃ ┗ 📜page.tsx
 ┃ ┃ ┣ 📂room
 ┃ ┃ ┃ ┣ 📂[id]
 ┃ ┃ ┃ ┃ ┗ 📜page.tsx
 ┃ ┃ ┃ ┗ 📜page.tsx
 ┃ ┃ ┣ 📜layout.tsx
 ┃ ┃ ┗ 📜page.tsx
 ┃ ┣ 📂search
 ┃ ┃ ┣ 📜page.tsx
 ┃ ┃ ┗ 📜search.css
 ┃ ┣ 📂signin
 ┃ ┃ ┗ 📜page.tsx
 ┃ ┣ 📂signup
 ┃ ┃ ┗ 📜page.tsx
 ┃ ┣ 📜favicon.ico
 ┃ ┣ 📜globals.css
 ┃ ┣ 📜layout.tsx
 ┃ ┣ 📜loading.tsx
 ┃ ┣ 📜not-found.tsx
 ┃ ┣ 📜page.tsx
 ┃ ┗ 📜providers.tsx
 ┣ 📂assets
 ┃ ┗ 📜alone.gif
 ┣ 📂components
 ┃ ┣ 📂brain
 ┃ ┃ ┣ 📜backup.js
 ┃ ┃ ┣ 📜back_brain.js
 ┃ ┃ ┣ 📜back_outlines.js
 ┃ ┃ ┣ 📜Brain.js
 ┃ ┃ ┣ 📜Outlines.js
 ┃ ┃ ┣ 📜SelectShare.tsx
 ┃ ┃ ┗ 📜test.js
 ┃ ┣ 📂category
 ┃ ┃ ┗ 📜Category.tsx
 ┃ ┣ 📂darkmode
 ┃ ┃ ┗ 📜DarkmodeBtn.tsx
 ┃ ┣ 📂editor
 ┃ ┃ ┣ 📜DeleteBtn.tsx
 ┃ ┃ ┣ 📜Editor.module.css
 ┃ ┃ ┣ 📜GPT.css
 ┃ ┃ ┣ 📜GPT.tsx
 ┃ ┃ ┣ 📜Search.tsx
 ┃ ┃ ┣ 📜Search_iFrame.tsx
 ┃ ┃ ┣ 📜Search_noGPTver.tsx
 ┃ ┃ ┣ 📜SharedBtn.tsx
 ┃ ┃ ┣ 📜store.tsx
 ┃ ┃ ┣ 📜TestEditor.tsx
 ┃ ┃ ┗ 📜ToShareBtn.tsx
 ┃ ┣ 📂intro
 ┃ ┃ ┣ 📜arrow.png
 ┃ ┃ ┣ 📜backup.js
 ┃ ┃ ┣ 📜bite.png
 ┃ ┃ ┣ 📜bun_bottom.png
 ┃ ┃ ┣ 📜bun_top.png
 ┃ ┃ ┣ 📜cheese.png
 ┃ ┃ ┣ 📜css backup.css
 ┃ ┃ ┣ 📜download.svg
 ┃ ┃ ┣ 📜first_note.png
 ┃ ┃ ┣ 📜glowparticle.js
 ┃ ┃ ┣ 📜Intro.css
 ┃ ┃ ┣ 📜Intro.js
 ┃ ┃ ┣ 📜intro_logo.png
 ┃ ┃ ┣ 📜krlogo.png
 ┃ ┃ ┣ 📜left.png
 ┃ ┃ ┣ 📜left_hover.png
 ┃ ┃ ┣ 📜lettuce.png
 ┃ ┃ ┣ 📜line.png
 ┃ ┃ ┣ 📜line2.png
 ┃ ┃ ┣ 📜line3.png
 ┃ ┃ ┣ 📜line_shadow.png
 ┃ ┃ ┣ 📜logo.png
 ┃ ┃ ┣ 📜logo_blue.png
 ┃ ┃ ┣ 📜patty.png
 ┃ ┃ ┣ 📜plate.png
 ┃ ┃ ┣ 📜signin.png
 ┃ ┃ ┣ 📜signin_hover.png
 ┃ ┃ ┣ 📜signup.png
 ┃ ┃ ┗ 📜signup_hover.png
 ┃ ┣ 📂quiz
 ┃ ┃ ┣ 📜ChatRoom.tsx
 ┃ ┃ ┣ 📜QuizList.tsx
 ┃ ┃ ┣ 📜QuizMain.tsx
 ┃ ┃ ┣ 📜QuizMaker.tsx
 ┃ ┃ ┣ 📜QuizResult.tsx
 ┃ ┃ ┣ 📜QuizRoom.tsx
 ┃ ┃ ┣ 📜QuizStart.tsx
 ┃ ┃ ┗ 📜Tree.tsx
 ┃ ┣ 📂ui
 ┃ ┃ ┣ 📜Button.tsx
 ┃ ┃ ┣ 📜Card.tsx
 ┃ ┃ ┣ 📜Card2.tsx
 ┃ ┃ ┣ 📜Card3.tsx
 ┃ ┃ ┣ 📜chat.tsx
 ┃ ┃ ┣ 📜Input.tsx
 ┃ ┃ ┣ 📜Label.tsx
 ┃ ┃ ┣ 📜logout.tsx
 ┃ ┃ ┣ 📜Modal.tsx
 ┃ ┃ ┣ 📜Quiz.tsx
 ┃ ┃ ┣ 📜Rank.tsx
 ┃ ┃ ┗ 📜Timer.tsx
 ┃ ┣ 📜Loading.tsx
 ┃ ┣ 📜MySearch.tsx
 ┃ ┣ 📜MySearch_back.tsx
 ┃ ┣ 📜Navbar.tsx
 ┃ ┣ 📜Signin.tsx
 ┃ ┣ 📜Signup.tsx
 ┃ ┗ 📜ThreeScene.tsx
 ┣ 📂context
 ┃ ┣ 📜SocketEditorProvider.tsx
 ┃ ┣ 📜SocketProvider.tsx
 ┃ ┗ 📜SubscribeProvider.tsx
 ┣ 📂hooks
 ┃ ┣ 📜useAllDiagram.ts
 ┃ ┣ 📜useConnectSocket.ts
 ┃ ┣ 📜useCreateChildNote.ts
 ┃ ┣ 📜useCreateNote.ts
 ┃ ┣ 📜useCreateRoom.ts
 ┃ ┣ 📜useCreateSingleRoom.ts
 ┃ ┣ 📜useDeleteNote.ts
 ┃ ┣ 📜useGetNote.ts
 ┃ ┣ 📜useGetQuizHistory.ts
 ┃ ┣ 📜useGetSearchMyNote.ts
 ┃ ┣ 📜useGetSearchResult.ts
 ┃ ┣ 📜useGetSharedMember.ts
 ┃ ┣ 📜useGetShareUserList.ts
 ┃ ┣ 📜useGetUserInfo.ts
 ┃ ┣ 📜useGetUserInfoByNickName.ts
 ┃ ┣ 📜useGetUserNoteList.ts
 ┃ ┣ 📜useGPT.ts
 ┃ ┣ 📜useImageUpload.ts
 ┃ ┣ 📜useLinkNote.ts
 ┃ ┣ 📜useLoading.ts
 ┃ ┣ 📜useNoteList.ts
 ┃ ┣ 📜useReissue.ts
 ┃ ┣ 📜useSendQuizAnswer.ts
 ┃ ┣ 📜useShareDiagram.ts
 ┃ ┣ 📜useSharedNote.ts
 ┃ ┣ 📜useSiginin.ts
 ┃ ┣ 📜useSignup.ts
 ┃ ┣ 📜useUpdateNote.ts
 ┃ ┗ 📜useUsersFindByPkList.ts
 ┣ 📂store
 ┃ ┣ 📜documentsAtom.ts
 ┃ ┣ 📜isSolo.ts
 ┃ ┣ 📜mynoteResults.ts
 ┃ ┣ 📜searchOpen.ts
 ┃ ┗ 📜theme.ts
 ┗ 📂types
 ┃ ┣ 📜diagram.ts
 ┃ ┣ 📜ediotr.ts
 ┃ ┣ 📜quiz.ts
 ┃ ┗ 📜user.ts
```

</details>

<details>
<summary>back-end</summary>

```
📂config
📂gateway
📂discovery
 📂auth
  📂diagram
  📂editor
  📂gpt
  📂quiz
  📂search
```

</details>

## 서비스 구현 화면

<a id="item-six"></a>

#### 1. 랜딩 페이지 로그인

1.1 랜딩 페이지

![랜딩.gif](README_assets/606c926c99e82a793949199ec966036e2312bde1.gif)

1.2 로그인

![로그인.gif](README_assets/97bcc24be31a7582257c927eaaf128ce099e6561.gif)

#### 2. 다이어그램

2.1 내 뇌 보기

![내 뇌.gif](README_assets/4ecfa2f085ed5f632b2c71fcfa3b10673c2c4f9b.gif)

2.2 공유 뇌 보기

![친구 뇌 받기.gif](README_assets/cfedeefc7d4f196f755d61b1319a8a64958f01e9.gif)

#### 3. Editor

3.1 작성

![글쓰기.gif](README_assets/5eec8b7c9e30327cbb808ad77f397dcf9a121521.gif)

3.2 , 수정, 삭제

3.2 공유

![문서공유.gif](README_assets/e18139ecd3d25aa22339331d639a4b61e8f31324.gif)

3.4 동시 작성

#### 4.GPT

![](README_assets/17f23ccef01b3500d763b7810499913e1ebb12f1.gif)

#### 5.Search

![서치.gif](README_assets/2560d984591cc983e927fd36fc2322e8de457280.gif)

#### 6. Quiz

6.1 혼자풀기

6.1.1 방만들기

![혼자풀기1.gif](README_assets/a211a40a133c0a1d80d72587aa1d083eca24f1ab.gif)

6.1.2 퀴즈풀기 + 퀴즈 결과

![혼자풀기2.gif](README_assets/82b1ffdc0001c6a21ce028f51e02ec5cd75618ca.gif)

6.2 같이풀기

6.1.1 방만들기

6.1.2 대기실 : 레디 및 채팅

6.1.3  퀴즈 풀기

6.1.4 퀴즈 결과 : 랭킹 및 오답노트

## 느낀 점
