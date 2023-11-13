import { fetchDiagramAll } from "@/api/service/diagram";
import { userAtom } from "@/store/authAtom";
import { useAtom } from "jotai";
import { useQuery, useInfiniteQuery } from "react-query";

// export const useAllDiagram = () => {
//   const [user] = useAtom(userAtom);
//   return useQuery(["diagrams", user.userPk], () => fetchDiagramAll(user.userPk));
// };
export const useAllDiagram = () => {
  const [user] = useAtom(userAtom);
  return useQuery(["diagrams", user.userPk], () => fetchDiagramAll(user.userPk), {
    enabled: user.userPk !== 0, // user.userPk가 0이 아닐 때만 요청을 보냅니다.
  });
};
//
// export const useAllDiary = () => {
//   return useQuery(["diaries"], () => fetchAllDiaries, {
//     staleTime: 60 * 1000, // 1 분
//   });
// };

// 나중에 빼서 쓸 때는
// const { data, isLoading, isError, status } = useQuery(["diagrams"], fetchDiagramAll);

// 변수 참고하세요.
// status: 쿼리 요청 함수의 상태를 표현하는 status는 4가지의 값이 존재한다.(문자열 형태)
// - idle: 쿼리 데이터가 없고 비었을 때, { enabled: false } 상태로 쿼리가 호출되면 이 상태로 시작된다.
// - loading: 말 그대로 아직 캐시된 데이터가 없고 로딩중일 때 상태
// - error: 요청 에러 발생했을 때 상태
// - success: 요청 성공했을 때 상태
// data: 쿼리 함수가 리턴한 Promise에서 resolved된 데이터
// isLoading: 캐싱 된 데이터가 없을 때 즉, 처음 실행된 쿼리 일 때 로딩 여부에 따라 true/false로 반환된다.
// 이는 캐싱 된 데이터가 있다면 로딩 여부에 상관없이 false를 반환한다.
// isError: 에러가 발생한 경우 true
