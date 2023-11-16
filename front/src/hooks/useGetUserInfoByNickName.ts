import { getOtherUserPkByNickName } from "@/api/service/user";
import { useQuery } from "react-query";

export const useGetUserInfoByNickName = (nickName: string) => {
  return useQuery(["otherUserInfo", nickName], () => getOtherUserPkByNickName(nickName), {});
};
