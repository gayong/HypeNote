import { getNote } from "../service/editor";

// 내 단일 노트 가져오기
export default async function Note(editorId: string) {
  const data = await getNote(editorId);
  return data.data;
}
