import { getShareUserList } from "@/api/service/user";
import { useQuery, useInfiniteQuery } from "react-query";
// import { userAtom } from "@/store/authAtom";
import { useAtom } from "jotai";
import useGetUserInfo from "./useGetUserInfo";

export const useShareMemberList = () => {
  const { data: user, isLoading, isError, error } = useGetUserInfo();
  // const [user] = useAtom(userAtom);
  return useQuery(["memberList", user?.userPk], () => getShareUserList(user?.userPk as number));
};
