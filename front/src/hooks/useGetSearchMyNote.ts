import { fetchSearchMyNote } from "@/api/service/editor";
import { useQuery, useInfiniteQuery } from "react-query";

// export const useGetSearchMyNote = (query: string | undefined) => {
//   return useQuery(["fetchSearchMyNote", query], () => fetchSearchMyNote(query));
// };
export const useGetSearchMyNote = (query: string, enabled = false) => {
  return useQuery(["fetchSearchMyNote", query], () => fetchSearchMyNote(query), { enabled });
};
