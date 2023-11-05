import { quizHistory } from "@/api/service/quiz";
import { useQuery, useInfiniteQuery } from "react-query";

export const useGetQuizHistory = (userId: number) => {
  return useQuery(["quizHistory", userId], () => quizHistory(userId));
};
