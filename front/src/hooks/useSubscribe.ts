// 구독
import { useState, useRef } from "react";
import { CompatClient } from "@stomp/stompjs";

export default function useSubscribe(topic: string) {
  const client = useRef<CompatClient | null>(null);
  const [message, setMessage] = useState("");

  // 지금 내가 소켓에 들어가 있다면
  // /sub/chat/${roomId} => 채팅
  if (client.current) {
    client.current.subscribe(topic, (message) => {
      setMessage(JSON.parse(message.body));
    });
  }

  return message;
}
