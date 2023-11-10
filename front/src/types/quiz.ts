// 퀴즈 방 관련
export interface QuizRoom {
  roomName: string;
  pages: Array<number>;
  sharePages: Array<number>;
  quizCnt: number;
  single: boolean;
  content: string;
  users: Array<object>;
}
export interface QuizRoomGroup extends QuizRoom {
  inviteUsers: Array<QuizUser>;
}

export interface QuizRoomInfo {
  id: number;
  content: string;
  roomName: string;
  roomMax: number;
  roomCnt: number;
  readyCnt: number;
  quizCnt: number;
  roomStatus: boolean;
  createdDate: string;
  users: Array<QuizUser>;
  pages: Array<number>;
  sharePages: Array<number>;
  single: boolean;
  inviteUsers: Array<QuizUser>;
  host: number;
}

export interface QuizRanking {
  correct: number;
  ranking: number;
  total: number;
  userImg: string;
  userName: string;
  userPk: number;
}

export interface QuizRoomDetail {
  result: object;
  type: string;
  ranking?: Array<number>;
}

export interface QuizUser {
  host?: boolean;
  ready?: boolean;
  userName: string;
  userPk: number;
  userImg: string;
}

export interface chatUser {
  userPk: number;
  content: string;
  chatTime: string;
  userName: string;
  userImg: string;
}

export interface QuizInfo {
  answer: string;
  commentary: string;
  example: Array<QuizQuestion>;
  id: number;
  question: string;
}
export interface QuizInfoWithMyAnswer extends QuizInfo {
  myAnswer: number;
}

export interface QuizQuestion {
  content: string;
  ex: string;
}

export interface QuizResultInfo {
  correct: number;
  examDone: string;
  examStart: string;
  questionResult: Array<object>;
  quizId: number;
  roomId: number;
  totals: number;
  userPk: number;
}
