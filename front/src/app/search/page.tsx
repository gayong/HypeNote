"use client";
import { Suspense } from "react";

import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import { useGetSearchMyNote } from "@/hooks/useGetSearchMyNote";
import { NoteType } from "@/types/ediotr";
import MySearch from "@/components/MySearch";
import "./search.css";
import Loading from "@/components/Loading";

function SearchBarFallback() {
  return <Loading />;
}

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
      setResults(response.data.data.notes);
    }
  }, [response]);

  return (
    <div className="h-screen pt-16">
      <h1 className="text-[25px] mx-auto text-center">
        <span className="font-bold">{search}</span>에 대한 검색 결과입니다.
      </h1>
      <div className="w-[600px] mx-auto my-10">
        <Suspense fallback={<SearchBarFallback />}>
          <MySearch />
        </Suspense>
      </div>
      {results.length > 0 ? (
        results.map((item, index) => (
          <div key={index} className="px-40">
            <Link href={`/editor/${item.id}`}>
              <h1 className="font-preBd hover:text-dark_font text-[18px] mb-1"> {item.title}</h1>
            </Link>
            <h1 className="search_content">{item.content}</h1>
            <br />
            <hr className="opacity-20" />
            <br />
          </div>
        ))
      ) : (
        <div className="font-preBd text-lg text-center">검색 결과가 없습니다.</div>
      )}
    </div>
  );
}
