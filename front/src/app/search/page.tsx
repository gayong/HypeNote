import MySearch from "@/components/MySearch";
import { Metadata } from "next";
import { useAtom } from "jotai";
import { mynoteResults } from "@/store/mynoteResults";

export const metadata: Metadata = {
  title: "Search",
  description: "Search my notes",
};

export default function MySearchPage() {
  // const [results] = useAtom(mynoteResults);

  return (
    <section className="flex items-center justify-center h-screen">
      <MySearch />
      <h1 className="text-3xl mx-auto text-center">검색 결과입니다.</h1>
      {/* {results &&
        results.length > 0 &&
        results.map((item, index) => (
          <div key={index}>
            <h1>{item.title}</h1>
            <h1>{item.content}</h1>
          </div>
        ))} */}
    </section>
  );
}
