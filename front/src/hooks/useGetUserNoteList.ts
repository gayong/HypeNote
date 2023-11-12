import { fetchUserRootList } from "@/api/service/editor";

const useGetUserNoteList = () => {
  const userNoteList = async () => {
    try {
      const response = await fetchUserRootList();
      // data 저장하기
      const data = {
        documentsRoots: response.data.documentsRoots,
        sharedDocumentsRoots: response.data.sharedDocumentsRoots,
      };
      //   return "success";
      console.log(data);
      return data;
    } catch (error) {
      console.log(error);
    }
  };
  return { userNoteList };
};

export default useGetUserNoteList;
