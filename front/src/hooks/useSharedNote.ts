import { shareNote } from "@/api/service/user";


export const useSharedNote = () => {
  const shareDocument = async (userId: number, userList: number[], document: string) => {

    try {
      const response = await shareNote(userId, userList, document);
    } catch (error) {
      console.log(error);
    }
  };
  return {
    shareDocument,
  };
};
export default useSharedNote;
