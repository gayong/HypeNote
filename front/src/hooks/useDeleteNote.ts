import { fetchDeleteNote } from "@/api/service/editor";

const useDeleteNote = () => {
  const DeleteNote = async (editorId: string) => {
    try {
      const response = await fetchDeleteNote(editorId);
    } catch (error) {
      console.log(error);
    }
  };
  return { DeleteNote };
};

export default useDeleteNote;
