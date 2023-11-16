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

  const handleEnter = () => {
    router.push(`/search?keyword=${encodeURIComponent(keyword)}`);
  };

  const onChange = (e: any) => {
    setKeyword(e.target.value);
    console.log(e.target.value);
  };

  return (
    <div>
      <Search
        placeholder="게시글을 검색하세요"
        value={keyword}
        // enterButton
        onPressEnter={handleEnter}
        onChange={onChange}
        onSearch={handleEnter}
        className="flex items-center w-[97%] mx-auto bg-transparent text-[15px] focus:outline-none placeholder-gray-600"
      />
    </div>
  );
}
