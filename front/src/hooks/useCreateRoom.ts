import { createQuizRoom } from "@/api/service/quiz";

export const useCreateRoom = () => {
  const createRoom = async (
    roomName: string,
    pages: Array<number>,
    sharePages: Array<number>,
    quizCnt: number,
    single: boolean
  ) => {
    console.log("호출");
    try {
      const response = await createQuizRoom(roomName, pages, sharePages, quizCnt, single);
      console.log(response);
    } catch (error) {
      console.log("에러에러");
      console.log(error);
    }
  };
  return { createRoom };
};
