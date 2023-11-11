import { fetchCreateCihldDocument } from "@/api/service/editor";

const useCreateChildNote = () => {
  const createChildDocument = async (userId: number, editorId: string) => {
    console.log(userId);
    try {
      const response = await fetchCreateCihldDocument(userId, editorId);
      console.log(response);
      return response.data.data.id;
    } catch (err) {
      console.log(err);
    }
    return "";
  };
  return { createChildDocument };
};
export default useCreateChildNote;
