import { shareDiagram } from "@/api/service/diagram";
import { useQuery, useInfiniteQuery, useMutation } from "react-query";
import { useState } from "react";
import { userAtom } from "@/store/authAtom";
import { useAtom } from "jotai";
// export const useShareDiagram = (members: Array<number>) => {
//   return useMutation(["shareDiagrams"], () => shareDiagram(members));
// };

export const useShareDiagram = () => {
  const [user] = useAtom(userAtom);
  const [shareInfo, setShareInfo] = useState([]);

  const shareDiagmram = useMutation(
    async ({ members }: { members: Array<number> }) => {
      const response = await shareDiagram(user.userPk, members);
      console.log(response, "1234");
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
