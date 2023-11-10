import { fetchSearchResult } from "@/api/service/editor";
import { useQuery, useInfiniteQuery } from "react-query";

export const useGetSearchResult = (query: string, enabled = false) => {
  return useQuery(["fetchSearchResult", query], () => fetchSearchResult(query), { enabled });
};
