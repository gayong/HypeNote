import SockJS from "sockjs-client";
import { useEffect, useRef, useState } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";

export default function useConnectSocket() {
  const client = useRef<CompatClient | null>(null);

  const socket = new SockJS(
    process.env.NEXT_PUBLIC_SERVER_URL + "wss/stomp/chat"
  );

  client.current = Stomp.over(socket);
  console.log("소켓연결완료");

  if (client.current) {
    client.current.connect(
      { "Content-Type": "application/json" },
      () => {},
      (error: any) => {
        console.error("Connection failed:", error);
      }
    );
  }

  return;
}
