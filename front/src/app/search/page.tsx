"use client";

// import MySearch from "@/components/MySearch";
// import { Metadata } from "next";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import { useGetSearchMyNote } from "@/hooks/useGetSearchMyNote";
import { NoteType } from "@/types/ediotr";

// export const metadata: Metadata = {
//   title: "Search",
//   description: "Search my notes",
// };

export default function SearchPage() {
  const searchParams = useSearchParams();
  const search = searchParams.get("keyword");
  const [results, setResults] = useState<NoteType[]>([]);
  const [enabled, setEnabled] = useState(false);
  const { data: response, isLoading, refetch } = useGetSearchMyNote(search as string, enabled);

  useEffect(() => {
    if (search) {
      setEnabled(true);
    }
  }, [search]);

  useEffect(() => {
    if (enabled) {
      refetch();
    }
  }, [enabled]);

  useEffect(() => {
    if (response) {
      console.log("여기", response.data.data.notes);
      setResults(response.data.data.notes);
    }
  }, [response]);

  return (
    <section className="h-screen">
      {/* <MySearch /> */}
      <h1 className="text-3xl mx-auto text-center">{search}검색 결과입니다.</h1>
      {results.length > 0 &&
        results.map((item, index) => (
          <div key={index}>
            <h1>title:{item.title}</h1>
            <h1>content:{item.content}</h1>
            <br />
          </div>
        ))}
    </section>
  );
}
