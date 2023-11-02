// 다이아그램 관련
import api from "../instances/api";
// 내 뇌 전체 조회
export const fetchDiagramAll = async () => api.get(`api/diagram`);

// 친구 뇌 합치기
export const shareDiagram = async (members: Array<number>) => {
  api.post(`api/diagram/share`, { members });
};

// 제목 수정
export const renameTitle = async (title: string) => {
  api.put(`api/diagram/title`, { title });
};
