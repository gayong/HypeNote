import { fetchCreateNote } from "../api/service/editor";

// 내 단일 노트 가져오기
export default async function UpdateNote(editorId: string, title: string, content: string) {
  const data = await fetchCreateNote(editorId, title, content);
  return content;
}
