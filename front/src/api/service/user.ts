// 로그인 관련
import api from "../instances/api";

// 회원가입
export const createUser = (email: string, password: string, nickName: string, profileImage: string) =>
  api.post(`auth/signup`, {
    email,
    password,
    nickName,
    profileImage,
  });
// 로그인
export const signinUser = (email: string, password: string) => api.post(`auth/login`, { email, password });

// 게시글 공유
// export const shareNote = (userId: number, document:number) =>
// api.put(`auth/share`, {userId, document})
