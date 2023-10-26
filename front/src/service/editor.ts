//에디터 관련
const api = "https://k9e101.p.ssafy.io/api";

// 에디터 게시글 상세 조회
export async function getNote(editorId: string) {
  const res = await fetch(`${api}/editor/${editorId}`);

  if (!res.ok) {
    throw new Error("Failed to fetch data");
  }

  return res.json();
}

// 에디터 게시글 검색
// 코드 틀릴 수 있음..
export async function getSearch(keyword: string) {
  const res = await fetch(`${api}/search?=${keyword}`);
  if (!res.ok) {
    throw new Error("Failed to fetch data");
  }

  return res.json();
}
