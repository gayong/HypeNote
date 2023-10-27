"use client";

import { useEffect, useRef, useState } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export default function Connects() {
  const client = useRef<CompatClient | null>(null);
  const [message, setMessage] = useState("");
  const [input, setInput] = useState("");

  const roomId = 123;

  useEffect(() => {
    const chatConnect = () => {
      //socket 연결
      const socket = new SockJS("https://k9e101.p.ssafy.io/wss/stomp/chat");
      client.current = Stomp.over(socket);
      console.log("소켓연결완료");

      client.current.connect(
        { "Content-Type": "application/json" },
        () => {
          // callback 함수 설정, 여기서 구독(발행하는 메세지 받기)
          if (client.current) {
            client.current.subscribe(`/sub/chat/${roomId}`, (message) => {
              setMessage(JSON.parse(message.body));
            });
          }
        },
        (error: any) => {
          // Connection failed callback
          console.error("Connection failed:", error);
        }
      );
    };

    chatConnect();
  }, []);
  return (
    <>
      <div>소켓연결연결</div>
    </>
  );
}

// 채팅 전송
// function sendMessage(e: React.FormEvent<HTMLFormElement>) {
//   e.preventDefault();
//   setInput({
//               userPk:1,
//               content:"안녕",
//           });
//   if (input.content !== "") {
//       if (client.current && client.current.connected) {
//           // 여기서 발행
//           client.current.send(`/pub/chat/${roomId}`, {}, JSON.stringify(input));
//           // 인풋 초기화
//           setInput({
//               user:pk,
//               content:"",
//           });
//   }
// }
