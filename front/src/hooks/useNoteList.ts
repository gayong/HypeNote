import { fetchNoteList } from "@/api/service/editor";

const useNoteList = () => {
  const noteList = async (rootList: string[]) => {
    try {
      const response = await fetchNoteList(rootList);
      return response.data.data;
    } catch (err) {
      console.log(err);
    }
    return "";
  };
  return { noteList };
};
export default useNoteList;
