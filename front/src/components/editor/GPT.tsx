import React, { useEffect, useState, useRef } from "react";
import { Button, Input, Spin, message } from "antd";
import useGPT from "@/hooks/useGPT";
import { BsFillSendFill } from "react-icons/bs";
import "./GPT.css";
import { LoadingOutlined } from "@ant-design/icons";

export default function GPT() {
  const [keyword, setKeyword] = useState("");
  const { getGPT } = useGPT();
  const [results, setResults] = useState("");
  const typingRef = useRef<HTMLDivElement>(null);
  const [typingIdx, setTypingIdx] = useState(0);
  const [typingText, setTypingText] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();

  const error = () => {
    messageApi.open({
      type: "error",
      content: "답변 생성에 실패했습니다. 새로고침 후 시도해주세요.",
    });
  };

  const handleEnter = async () => {
    console.log("지피티 질문", keyword);
    try {
      setIsLoading(true);
      setTypingText([]);
      if (typingRef.current) {
        typingRef.current.innerHTML = "";
      }
      const response = await getGPT(keyword);
      setKeyword("");
      console.log("GPT 응답", response);
      console.log(response[0].message.content);
      setResults(response[0].message.content);
      setTypingText(response[0].message.content.split(""));
      setTypingIdx(0);
      setIsLoading(false);
    } catch (err) {
      console.log(err);
      setIsLoading(false);
      error();
    }
  };

  const onChange = (e: any) => {
    setKeyword(e.target.value);
    console.log(e.target.value);
  };

  useEffect(() => {
    const typing = () => {
      if (typingRef.current && typingIdx < typingText.length) {
        typingRef.current.innerHTML += typingText[typingIdx];
        setTypingIdx(typingIdx + 1);
      }
    };
    const typingInterval = setInterval(typing, 50);
    return () => {
      clearInterval(typingInterval);
    };
  }, [typingIdx, typingText]);

  return (
    <>
      {contextHolder}
      {results && (
        <>
          <div
            className="mb-3 typing w-full min-w-full bg-primary rounded-xl p-3 text-font_primary rounded-l-none rounded-t-xl"
            ref={typingRef}></div>
          <br />
        </>
      )}

      <div className="flex">
        <Input
          className="z-50 rounded-none rounded-l-md"
          placeholder="Chat-GPT에게 질문하세요"
          value={keyword}
          onChange={onChange}
          onPressEnter={handleEnter}
          allowClear
          style={{ height: "32px" }}
        />
        <div style={{ flexGrow: 1 }}>
          <Button onClick={handleEnter} className="bg-[#a5b3e2] rounded-none rounded-r-md w-[50px] h-full">
            {isLoading ? (
              <Spin indicator={<LoadingOutlined style={{ fontSize: 16, color: "white" }} spin />} />
            ) : (
              <BsFillSendFill className="text-font_primary" />
            )}
          </Button>
        </div>
      </div>
    </>
  );
}
