import { fetchLinkNote } from "@/api/service/editor";

const useLinkNote = () => {
  const LinkNote = async (userId: number, parentId: string, childId: string) => {
    try {
      const response = await fetchLinkNote(userId, parentId, childId);
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };
  return { LinkNote };
};

export default useLinkNote;
