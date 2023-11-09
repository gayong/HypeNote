import { sendQuiz } from "@/api/service/quiz";
import { useMutation } from "react-query";

export const useSendQuizAnswer = () => {
  const sendQuizAnswer = useMutation(
    async ({ roomId, userId, answers }: { roomId: number; userId: number; answers: object }) => {
      const response = await sendQuiz(roomId, userId, answers);
      console.log(response);
    },
    {
      onError: (error) => {
        console.log(error);
      },
    }
  );

  return {
    sendQuizAnswer,
  };
};
