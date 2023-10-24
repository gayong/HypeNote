//에디터 관련

// 게시글 목록 조회
async function getEditorData() {
  const res = await fetch("");
  // The return value is *not* serialized
  // You can return Date, Map, Set, etc.

  if (!res.ok) {
    // This will activate the closest `error.js` Error Boundary
    throw new Error("Failed to fetch data");
  }

  return res.json();
}

export default async function Page() {
  const data = await getEditorData();

  return data;
}
