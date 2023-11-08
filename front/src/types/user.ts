// 데이터 타입을 쓰시오.
export interface User {
  userPk: number;
  nickName: string;
  email: string;
  profileImage: string;
  documents: Array<any>;
}

export interface LoginUser {
  email: string;
  password: string;
}
