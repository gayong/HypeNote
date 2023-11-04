// 퀴즈관련
import api from "../instances/api";

// 퀴즈 방 생성 -
//group / single 동일 single ture,false만 관리
export const createQuizRoom = (
  roomName: string,
  pages: Array<number>,
  sharePages: Array<number>,
  quizCnt: number,
  single: boolean
) =>
  api.post(`quiz/quizroom`, {
    roomName,
    pages,
    sharePages,
    quizCnt,
    single,
  });
// export const createQuizRoom = () => api.get(`quiz/quizroom`);

// 유저 초대 (여러명)
export const inviteUser = (
  roomName: string,
  pages: Array<number>,
  sharePages: Array<number>,
  quizCnt: number,
  single: boolean,
  users: Array<object>
) =>
  api.post(`quizroom/invite`, {
    roomName,
    pages,
    sharePages,
    quizCnt,
    single,
    users,
  });

// 퀴즈 정답 전송
export const sendQuiz = (roomId: number, userId: number) => api.post(`quizroom/${roomId}/${userId}`);
