"use client";

import axios from "axios";
import Link from "next/link";
import Image from "next/image";
import React, { useEffect, useState } from "react";
import { Button, Drawer, Input } from "antd";
import { useAtom } from "jotai";
import { isSearchOpen } from "../../store/searchOpen";
import { useGetSearchResult } from "@/hooks/useGetSearchResult";
import { SearchType } from "@/types/ediotr";

// 이건 서랍 속 검색!!!!!!
export default function Search() {
  const [open, setOpen] = useAtom(isSearchOpen);
  const { Search } = Input;
  const [keyword, setKeyword] = useState("");
  const [enabled, setEnabled] = useState(false);
  const { data: response, isLoading, refetch } = useGetSearchResult(keyword, enabled);
  const [results, setResults] = useState<Array<SearchType>>([]);

  const showDrawer = () => {
    setOpen(true);
    console.log("서랍 열었다");
  };

  const onClose = () => {
    setOpen(false);
    console.log("서랍 닫았다");
  };

  // const handleEnter = () => {
  //   console.log("value", keyword);
  //   // api get 요청
  //   setEnabled(true);
  //   refetch();
  // };
  const handleEnter = () => {
    console.log("value", keyword);
    // api get 요청
    setEnabled(true);
  };

  useEffect(() => {
    if (enabled) {
      refetch();
    }
  }, [enabled]);

  const onChange = (e: any) => {
    setKeyword(e.target.value);
    console.log(e.target.value);
  };

  useEffect(() => {
    if (response) {
      console.log("여기", response.data.data.items);
      // console.log("응답", response.data);
      setResults(response.data.data.items);
    }
  }, [response]);

  // const getResult = () => {
  //   useGetSearchResult(keyword);
  //   if (response) {
  //     console.log("받아온 검색결과", response);
  //   }
  // }, [response])

  return (
    <div>
      <Button
        className="bg-primary absolute top-8 -right-9 rounded-b-none rotate-[270deg]"
        type="primary"
        onClick={showDrawer}>
        검색 & GPT
      </Button>
      <Drawer
        title="한입 도우미"
        placement="right"
        onClose={onClose}
        open={open}
        zIndex={0}
        mask={false}
        maskClosable={false}
        width={380}
        className="relative">
        <Search
          placeholder="검색어를 입력해주세요"
          value={keyword}
          enterButton
          onPressEnter={handleEnter}
          onChange={onChange}
          onSearch={handleEnter}
        />
        {/* {results &&
          results.map((item, index) => (
            <div key={index}>
              <Image
                src={item.pagemap.cse_thumbnail[0].src}
                alt="썸네일"
                width={100}
                height={100}
                // width={item.pagemap.cse_thumbnail[0].width}
                // height={item.pagemap.cse_thumbnail[0].height}
              />
              <p>{item.title}</p>
              <p>{item.link}</p>
              <p>{item.snippet}</p>
            </div>
          ))} */}
      </Drawer>
    </div>
  );
}
