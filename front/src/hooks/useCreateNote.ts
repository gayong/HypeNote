import { fetchCreateDocument } from "@/api/service/editor";

const useCreateNote = () => {
  const createDocument = async (userId: number) => {
    console.log(userId);
    try {
      const response = await fetchCreateDocument(userId);
      console.log(response.data.data);
      return response.data.data.id;
    } catch (err) {
      console.log(err);
    }
    return "";
  };
  return { createDocument };
};
export default useCreateNote;
