import { getShareUserList } from "@/api/service/user";
import { useQuery, useInfiniteQuery } from "react-query";
import { userAtom } from "@/store/authAtom";
import { useAtom } from "jotai";

export const useShareMemberList = () => {
  const [user] = useAtom(userAtom);
  return useQuery(["memberList", user.userPk], () => getShareUserList(user.userPk));
};
