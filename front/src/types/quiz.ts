export interface QuizRoom {
  roomName: string;
  pages: Array<number>;
  sharePages: Array<number>;
  quizCnt: number;
  single: boolean;
  users?: Array<object>;
}

export interface QuizRoomDetail {
  result: QuizRoomInfo;
  type: string;
}

export interface QuizRoomInfo {
  id: number;
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
}

export interface QuizResultInfo {
  ranking: Array<number>;
  avg: number;
  result: Array<object>;
}

export interface Chat {
  chatTime: string;
  userPk: string;
  content: string;
}

export interface QuizUser {
  host?: boolean;
  ready?: boolean;
  userName: string;
  userPk: number;
}
