import { fetchDiagramAll } from "@/api/service/diagram";
import { useQuery } from "react-query";
import useGetUserInfo from "./useGetUserInfo";

export const useAllDiagram = () => {
  const { data: user, isLoading, isError, error } = useGetUserInfo();

  return useQuery(["diagrams", user?.userPk], () => fetchDiagramAll(user?.userPk as number), {
    enabled: user?.userPk !== 0, // user.userPk가 0이 아닐 때만 요청을 보냅니다.
  });
};
