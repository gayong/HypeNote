// 퀴즈관련
import api from "../instances/api";

// 퀴즈 방 생성 -
//group / single 동일 single ture,false만 관리
export const createQuizRoom = (
  roomName: string,
  pages: Array<string>,
  sharePages: Array<string>,
  quizCnt: number,
  single: boolean,
  content: string
) =>
  api.post(`quiz/quizroom`, {
    roomName,
    pages,
    sharePages,
    quizCnt,
    single,
    content,
  });

// 유저 초대 (여러명)
export const inviteUser = (
  roomName: string,
  pages: Array<string>,
  sharePages: Array<string>,
  quizCnt: number,
  single: boolean,
  users: Array<object>,
  content: string
) =>
  api.post(`quiz/quizroom/invite`, {
    roomName,
    pages,
    sharePages,
    quizCnt,
    single,
    users,
    content,
  });

// 퀴즈 싱글 방

export const createSingleQuizRoom = (
  roomName: string,
  pages: Array<string>,
  sharePages: Array<string>,
  quizCnt: number,
  single: boolean,
  content: string
) =>
  api.post(`quiz/quizroom/invite`, {
    roomName,
    pages,
    sharePages,
    quizCnt,
    single,
    content,
  });

// 퀴즈 정답 전송
export const sendQuiz = (roomId: number, userId: number, answers: object) =>
  api.post(`quiz/${roomId}/${userId}`, {
    answers,
  });

// 내 퀴즈 히스토리
export const quizHistory = (userId: number) => api.get(`quiz/${userId}`);
