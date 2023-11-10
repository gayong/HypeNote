import { fetchNoteDetail } from "../api/service/editor";

// 내 단일 노트 가져오기
export default async function Note(editorId: string) {
  const data = await fetchNoteDetail(editorId);
  const content = data.data.data.content;
  console.log(content);
  return content;
}
