// 로그인 관련
import axios from "axios";
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
export const signinUser = (email: string, password: string) =>
  // axios.post(`${window.location.origin}/api/auth/login`, { email, password });
  api.post(`auth/login`, { email, password });

// 유저 정보 조회
export const getUserInfo = async () => {
  const response = await api.get(`auth/user-info`);
  return response.data;
};

// 토큰 재발급
export const reissueToken = () =>
  axios.post(`${window.location.origin}/api/auth/reissue`, {}, { withCredentials: true });
// api.post(`auth/reissue`, {}, { withCredentials: true });

// 게시글 공유
export const shareNote = (userId: number, userList: number[], editorId: string) => {
  const data = { userId, userList, editorId };
  return api.post(`editor/share`, data);
};
// 인물 검색
export const getOtherUserPkByNickName = (nickName: string) => {
  return api.get(`auth/user-info/${nickName}`);
};

// userPk 로 user info 반환
export const getUsersInfo = async (userPkList: number[]) => {
  const response = await api.post(`auth/user-info/pk-list`, { userPkList: userPkList });
  return response.data;
};

// userPk 로 user root info 반환
export const getShareUserList = async (userPk: number) => {
  const responese = await api.get(`auth/root-user-info/${userPk}`);
  return responese.data;
};
