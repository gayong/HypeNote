import { fetchDiagramAll } from "@/api/service/diagram";
import { useState } from "react";
import { useQuery } from "react-query";
import useGetUserInfo from "./useGetUserInfo";

export const useAllDiagram = () => {
  const { data: user, isLoading, isError, error } = useGetUserInfo();
  const [diagramReady, setDiagramReady] = useState(false);

  return useQuery(["diagrams", user?.userPk], () => fetchDiagramAll(user?.userPk as number), {
    enabled: !diagramReady && user?.userPk !== 0,
    onSuccess: () => {
      setDiagramReady(true);
    },
  });
};
