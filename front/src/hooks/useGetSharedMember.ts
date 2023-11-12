import { fetchSharedMember } from "@/api/service/editor";

const useGetSharedMember = () => {
  const SharedMember = async (editorList: string[]) => {
    try {
      const response = await fetchSharedMember(editorList);
      // data 저장하기
      //   return "success";
      console.log(response);
      return response;
    } catch (error) {
      console.log(error);
      return;
    }
  };
  return { SharedMember };
};

export default useGetSharedMember;
