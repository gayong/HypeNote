"use client";

import React, { useEffect, useState, useRef } from "react";
import { Button, Drawer, Input, Collapse, Modal } from "antd";
import useGPT from "@/hooks/useGPT";
import { SearchType } from "@/types/ediotr";
import "../../app/search/search.css";
import { BsFillSendFill } from "react-icons/bs";

async function fetchEventSource(url: string, keyword: string) {
  // const controller = new AbortController();
  let data = "";

  if (keyword) {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ question: keyword }),
    });

    const contentType = response.headers.get("Content-Type");
    if (response.body && contentType && contentType.includes("application/json")) {
      const reader = response.body.getReader();
      const decoder = new TextDecoder();

      while (true) {
        const { value, done } = await reader.read();
        if (done) break;

        let chunk = decoder.decode(value || new Uint8Array(), { stream: !done });
        if (chunk.startsWith("data:")) {
          chunk = chunk.slice(5);
        }
        // const jsonData = chunk.slice(6);
        const result = JSON.parse(chunk);

        if (result && result.choices && result.choices[0]) {
          data += result.choices[0].delta.content;
          console.log("dd", data);
          console.log("result", result);
          console.log("chunk", chunk);
        } else if (contentType && contentType.includes("text/html")) {
          console.log("json 타입이 아님 html임 stream 하지마셈");
        } else {
          throw new Error(`Unexpected content type: ${contentType}`);
        }
      }
    }
  }
  return data;
}

// function useEventSource(url: string, keyword: string) {
//   const [data, setData] = useState("");

//   useEffect(() => {
//     if (keyword) {
//       fetch(url, {
//         method: "POST",
//         headers: {
//           "Content-Type": "application/json",
//         },
//         body: JSON.stringify({ question: keyword }),
//       }).then((response) => {
//         const contentType = response.headers.get("Content-Type");
//         if (response.body && contentType && contentType.includes("application/json")) {
//           const reader = response.body.getReader();
//           const decoder = new TextDecoder();

//           function readChunk(): any {
//             return reader.read().then(({ value, done }) => {
//               if (done) return;

//               const chunk = decoder.decode(value || new Uint8Array(), { stream: !done });
//               const jsonData = chunk.slice(6);
//               const result = JSON.parse(jsonData);

//               console.log("이건 자른 데이터", jsonData);
//               console.log("이건 자른 데이터 json화", result);

//               if (result && result.choices && result.choices[0] && result.choices[0].delta) {
//                 setData((prevData) => prevData + result.choices[0].delta.content);
//               }

//               return readChunk();
//             });
//           }

//           return readChunk();
//         } else if (contentType && contentType.includes("text/html")) {
//           console.log("json 타입이 아님 html임 stream 하지마셈");
//         } else {
//           throw new Error(`Unexpected content type: ${contentType}`);
//         }
//       });
//     }
//   }, []);

//   return data;
// }

//     .then((response) => {
//       if (response.body) {
//         const reader = response.body.getReader();
//         const decoder = new TextDecoder();

//         function readChunk(): any {
//           return reader.read().then(({ value, done }) => {
//             if (done) return;

//             const chunk = decoder.decode(value || new Uint8Array(), { stream: !done });
//             const jsonData = chunk.slice(6);
//             const result = JSON.parse(jsonData);

//             console.log("이건 자른 데이터", jsonData);
//             console.log("이건 자른 데이터 json화", result);

//             if (result && result.choices && result.choices[0] && result.choices[0].delta) {
//               setData((prevData) => prevData + result.choices[0].delta.content);
//             }

//             return readChunk();
//           });
//         }

//         return readChunk();
//       }
//     });
//   }, [url, keyword]);

//   return data;
// }

export default function GPT() {
  const [keyword, setKeyword] = useState("");
  const [enabled, setEnabled] = useState(false);
  const { getGPT } = useGPT();
  const [results, setResults] = useState("");
  // const data = useEventSource("/api/gpt/chat");
  // const data = useEventSource("/api/gpt/chat", keyword);

  const handleEnter = async () => {
    console.log("지피티 질문", keyword);
    const data = await fetchEventSource("/api/gpt/chat", keyword);

    try {
      const response = await getGPT(keyword);
      console.log("GPT 응답", response);

      setResults(data);
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
  };

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
      <div>{results}</div>
    </div>
  );
}
