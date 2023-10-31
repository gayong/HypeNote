import SockJS from "sockjs-client";
import { useEffect, useRef, useState } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";
// import { useAtom } from "jotai";
// import { socketAtom } from "@/store/socket";

export default function useConnectSocket(topic: string, roomId?: number) {
  const client = useRef<CompatClient | null>(null);
  const [message, setMessage] = useState("");
  // const [client, setClient] = useAtom(socketAtom);
  // let roomId = 1;

  useEffect(() => {
    const chatConnect = () => {
      const socket = new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
      client.current = Stomp.over(socket);
      // 이렇게 해야 연결되는 거 같음.
      client.current.connect(
        { "Content-Type": "application/json" },
        () => {
          console.log("하하하하이이이이이이이이이이이이");
          if (client.current) {
            client.current.subscribe(`${topic}${roomId ? roomId : ""}`, (message) => {
              console.log("하이");
              setMessage(JSON.parse(message.body));
              console.log("메메메메시지", message);
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
  return;
}
