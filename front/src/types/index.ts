// 데이터 타입을 쓰시오.
export interface User {
  userId: number;
  email: string;
  documents: Array<any>;
}

export interface LoginUser {
  email: string;
  password: string;
}

export interface Note {
  title: string;
  content: string;
}

export interface Notes {
  notes: Array<Note>;
}
