//에디터 관련
import api from "../instances/api";

// 게시글 document 만들기
export const fetchCreateDocument = (userId: number) => {
  return api.post(`editor/${userId}`);
};

// 게시글 작성
export const fetchCreateNote = (editorId: string, title: string, content: string) => {
  api.post(`editor/write/${editorId}`, {
    title,
    content,
  });
};

// 게시글 삭제
export const fetchDeleteNote = (edittorId: string) => {
  api.delete(`editor/write/${edittorId}`);
};

// 에디터 게시글 상세 조회
export const fetchNoteDetail = (editorId: string) => {
  return api.get(`editor/${editorId}`);
};
