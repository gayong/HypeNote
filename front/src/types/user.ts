// 데이터 타입을 쓰시오.
export interface User {
  userPk: number;
  nickName: string;
  email: string;
  profileImage: string;
  documentsRoots: Array<string>;
  sharedDocumentsRoots: Array<string>;
}

export interface LoginUser {
  email: string;
  password: string;
}

export interface BasicUser {
  userPk: number;
  nickName: string;
}
