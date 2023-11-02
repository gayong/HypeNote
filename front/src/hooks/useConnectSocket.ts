import SockJS from "sockjs-client";
import { useEffect, useRef, useState } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";

export const useConnectSocket = (topic: string, roomId?: number) => {
  const [message, setMessage] = useState("");

  const socket = new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");

  const client = useRef<CompatClient>(Stomp.over(socket));

  useEffect(() => {
    client.current.connect({ "Content-Type": "application/json" }, () => {
      client.current?.subscribe(`${topic}`, (response) => {
        setMessage(JSON.parse(response.body));
      });
    });
    return () => {
      // client.current?.disconnect(undefined, { id: `${roomId}` });
    };
  }, []);

  const sendMessage = (content: string) => {};
};
