import { fetchCreateNote } from "../api/service/editor";

export default async function UpdateNote(editorId: string, title: any, content: string) {
  if (Array.isArray(title) && title[0] && title[0].text) {
    let totalTitle = "";
    title.forEach((element) => {
      if (element.text) {
        totalTitle += element.text;
      }
    });
    const data = await fetchCreateNote(editorId, totalTitle, content);
  }
  return content;
}
