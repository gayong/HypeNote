export interface QuizRoom {
  roomName: string;
  pages: Array<number>;
  sharePages: Array<number>;
  quizCnt: number;
  single: boolean;
  users?: Array<object>;
}

export interface QuizRoomInfo {
  id: number;
  roomName: string;
  roomMax: number;
  roomCnt: number;
  ready: number;
  quizCnt: number;
  roomStatus: boolean;
  createdDate: string;
  users: Array<object>;
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
