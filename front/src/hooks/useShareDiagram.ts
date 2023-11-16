import { shareDiagram } from "@/api/service/diagram";
import { useQuery, useInfiniteQuery, useMutation } from "react-query";
import { useState } from "react";
// import { userAtom } from "@/store/authAtom";
import { useAtom } from "jotai";
import useGetUserInfo from "./useGetUserInfo";
// export const useShareDiagram = (members: Array<number>) => {
//   return useMutation(["shareDiagrams"], () => shareDiagram(members));
// };

export const useShareDiagram = () => {
  const { data: user, isLoading, isError, error } = useGetUserInfo();

  // const [user] = useAtom(userAtom);
  const [shareInfo, setShareInfo] = useState([]);

  const shareDiagmram = useMutation(
    async ({ members }: { members: Array<number> }) => {
      if (!user) {
        return;
      }
      const response = await shareDiagram(user.userPk, members);
      return response.result;
    },
    {
      onError: (error) => {
        console.log(error);
      },
      onSuccess: (data) => {
        setShareInfo(data);
      },
    }
  );

  return {
    shareDiagmram: shareDiagmram,
    shareInfo: shareInfo,
  };
};
