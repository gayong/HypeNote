import { fetchSearchMyNote } from "@/api/service/editor";
import { useQuery, useInfiniteQuery } from "react-query";
import useGetUserInfo from "./useGetUserInfo";

export const useGetSearchMyNote = (query: string, enabled = false) => {
  const { data: user } = useGetUserInfo();

  return useQuery(["fetchSearchMyNote", query], () => fetchSearchMyNote(user?.userPk as number, query), { enabled });
};
