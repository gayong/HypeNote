import { createSingleQuizRoom } from "@/api/service/quiz";
import { QuizRoomGroup, QuizUser } from "@/types/quiz";
import { useState } from "react";
import { useMutation } from "react-query";

export const useCreateSingleRoom = () => {
  const [roomInfo, setRoomInfo] = useState<QuizRoomGroup>();
  const [roomId, setRoomId] = useState();
  const createSingleRoomMutation = useMutation(
    async ({
      roomName,
      pages,
      sharePages,
      quizCnt,
      single,
      content,
    }: {
      roomName: string;
      pages: Array<string>;
      sharePages: Array<string>;
      quizCnt: number;
      single: boolean;
      content: string;
    }) => {
      const response = await createSingleQuizRoom(roomName, pages, sharePages, quizCnt, single, content);
      console.log(response.data.result);
      //   setRoomInfo(response.data.result);
    },
    {
      onError: (error) => {
        console.log(error);
      },
    }
  );

  return {
    createSingleRoomMutation: createSingleRoomMutation,
    roomId: roomId,
    // roomInfo: roomInfo,
  };
};
