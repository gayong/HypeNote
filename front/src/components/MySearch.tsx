"use client";

import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import React, { useEffect, useState } from "react";
import { Button, Drawer, Input } from "antd";
import { useGetSearchMyNote } from "@/hooks/useGetSearchMyNote";
import { NoteType } from "@/types/ediotr";
import { useSearchParams } from "next/navigation";

// 이건 네브바에 내 문서 검색!!!!!!
export default function MySearch() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const search = searchParams.get("search");
  const { Search } = Input;
  const [keyword, setKeyword] = useState("");
  const [results, setResults] = useState<NoteType[]>([]);
  const [enabled, setEnabled] = useState(false);

  const { data: response, isLoading, refetch } = useGetSearchMyNote(keyword, enabled);

  // const getResult = async (keyword: string) => {
  //   try {
  //     const response = await axios.get("https://k9e101.p.ssafy.io/api/editor", {
  //       params: { query: keyword },
  //     });
  //     console.log("오니?", response.data.data.notes);
  //     if (response.data.data) {
  //       setResults(response.data.data.notes);
  //     }
  //   } catch (error) {
  //     console.error("Error:", error);
  //   }
  // };

  const handleEnter = () => {
    console.log("value", keyword);
    // api get 요청
    // getResult(keyword);
    // setEnabled(true);

    // router.push("/search");
    router.push(`/search?keyword=${encodeURIComponent(keyword)}`);
  };

  // useEffect(() => {
  //   if (enabled) {
  //     refetch();
  //   }
  // }, [enabled]);

  // useEffect(() => {
  //   if (response) {
  //     console.log("여기", response.data.data.notes);
  //     // console.log("응답", response.data);
  //     setResults(response.data.data.notes);
  //   }
  // }, [response]);

  const onChange = (e: any) => {
    setKeyword(e.target.value);
    console.log(e.target.value);
  };

  return (
    <div>
      <Search
        placeholder="게시글을 검색하세요"
        value={keyword}
        enterButton
        onPressEnter={handleEnter}
        onChange={onChange}
        onSearch={handleEnter}
        className="flex items-center w-[97%] mx-auto bg-transparent text-[15px] focus:outline-none placeholder-gray-600"
      />
    </div>
  );
}
