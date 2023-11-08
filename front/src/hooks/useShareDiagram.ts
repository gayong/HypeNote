import { shareDiagram } from "@/api/service/diagram";
import { useQuery, useInfiniteQuery, useMutation } from "react-query";
import { useState } from "react";

// export const useShareDiagram = (members: Array<number>) => {
//   return useMutation(["shareDiagrams"], () => shareDiagram(members));
// };

export const useShareDiagram = () => {
  const [shareInfo, setShareInfo] = useState([]);

  const shareDiagmram = useMutation(
    async ({ members }: { members: Array<number> }) => {
      const response = await shareDiagram(members);
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
