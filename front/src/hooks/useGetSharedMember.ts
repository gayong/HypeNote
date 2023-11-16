import { useMutation } from "react-query";
import { fetchSharedMember } from "@/api/service/editor";

const useGetSharedMember = () => {
  const SharedMember = useMutation((editorList: string[]) => fetchSharedMember(editorList));

  return SharedMember;
};

export default useGetSharedMember;
