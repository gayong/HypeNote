import { getUsersInfo } from "@/api/service/user";
import { useMutation } from "react-query";

const useUsersFindByPkList = () => {
  const getUsersFindByPkList = useMutation((userList: number[]) => getUsersInfo(userList));
  return getUsersFindByPkList;
};
export default useUsersFindByPkList;
