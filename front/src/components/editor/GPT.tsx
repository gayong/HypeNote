"use client";

import React, { useEffect, useState, useRef } from "react";
import { Button, Drawer, Input, Collapse, Modal } from "antd";
import useGPT from "@/hooks/useGPT";
import { SearchType } from "@/types/ediotr";
import "../../app/search/search.css";
import { BsFillSendFill } from "react-icons/bs";

function useEventSource(url: string) {
  const [data, setData] = useState("");

  useEffect(() => {
    const source = new EventSource(url);

    source.onmessage = (event) => {
      const result = JSON.parse(event.data);
      if (result && result.choices && result.choices[0] && result.choices[0].delta) {
        setData((prevData) => prevData + result.choices[0].delta.content);
      }
    };

    return () => {
      source.close();
    };
  }, [url]);

  return data;
}

export default function GPT() {
  const [keyword, setKeyword] = useState("");
  const [enabled, setEnabled] = useState(false);
  const { getGPT } = useGPT();
  const [results, setResults] = useState<Array<SearchType>>([]);
  const data = useEventSource("/api/gpt/chat");

  // const handleEnter = () => {
  //   console.log("지피티 질문", keyword);
  //   // setEnabled(true);
  // };

  const handleEnter = async () => {
    console.log("지피티 질문", keyword);
    try {
      const response = await getGPT(keyword);
      console.log("GPT 응답", response);
      setResults(response);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (enabled) {
      // refetch();
      // setEnabled(false);
    }
  }, [enabled]);

  const onChange = (e: any) => {
    setKeyword(e.target.value);
    console.log(e.target.value);
    // setEnabled(false);
  };

  // useEffect(() => {
  //   if (response) {
  //     console.log("검색결과다", response.data);
  //   }
  // }, [response]);

  // useEffect(() => {
  //   if (response) {
  //     console.log("검색결과다", response.data);
  //     setResults(response.data);
  //   }
  // }, [response]);

  return (
    <div className="flex">
      <Input.TextArea
        className="z-50 rounded-none rounded-l-md"
        placeholder="Chat-GPT에게 질문하세요"
        value={keyword}
        onChange={onChange}
        onPressEnter={handleEnter}
        allowClear
        autoSize
      />
      <div style={{ flexGrow: 1 }}>
        <Button onClick={handleEnter} className="bg-[#a5b3e2] rounded-none rounded-r-md w-[50px] h-full">
          <BsFillSendFill className="text-font_primary" />
        </Button>
      </div>
      <div>{data}</div>
    </div>
  );
}
