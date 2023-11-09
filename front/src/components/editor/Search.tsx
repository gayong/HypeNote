import axios from "axios";
import Link from "next/link";
import React, { useEffect, useState } from "react";
import { Button, Drawer, Input } from "antd";
import { useAtom } from "jotai";
import { isSearchOpen } from "../../store/searchOpen";
import { useGetSearchResult } from "@/hooks/useGetSearchResult";

// 이건 서랍 속 검색
export default function Search() {
  const [open, setOpen] = useAtom(isSearchOpen);
  const { Search } = Input;
  const [keyword, setKeyword] = useState("");
  const [enabled, setEnabled] = useState(false);
  const { data: response, isLoading, refetch } = useGetSearchResult(keyword, enabled);
  const [results, setResults] = useState([]);

  const showDrawer = () => {
    setOpen(true);
    console.log("서랍 열었다");
  };

  const onClose = () => {
    setOpen(false);
    console.log("서랍 닫았다");
  };

  const handleEnter = () => {
    console.log("value", keyword);
    // api get 요청
    setEnabled(true);
    refetch();
  };

  const onChange = (e: any) => {
    setKeyword(e.target.value);
    console.log(e.target.value);
  };

  // useEffect(() => {
  //   if (response) {
  //     console.log("응답", response.data);
  //     setResults(response.data.items);
  //   }
  // }, [response]);

  // const getResult = () => {
  //   useGetSearchResult(keyword);
  //   if (response) {
  //     console.log("받아온 검색결과", response);
  //   }
  // }, [response])

  return (
    <div>
      <Button
        className="bg-primary absolute top-5 -right-5 rounded-b-none rotate-[270deg]"
        type="primary"
        onClick={showDrawer}>
        웹 검색
      </Button>
      <Drawer
        title="웹 검색"
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
        {/* {results.map((item, index) => (
          <>
            <p>{item.title}</p>
            <p>{item.link}</p>
            <p>{item.snippet}</p>
            <p>{item.pagemap[0][0]}</p>
          </>
        ))} */}
      </Drawer>
    </div>
  );
}
