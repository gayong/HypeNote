"use client";

import React, { createContext, useContext, useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import type { CompatClient } from "@stomp/stompjs";
import type { ReactNode } from "react";

const WebSocketContext = createContext<CompatClient | undefined>(undefined);

export function useEditorWebSocket(): CompatClient | undefined {
  return useContext(WebSocketContext);
}

type WebSocketProviderProps = {
  children: ReactNode;
};

export default function SocketEditorProvider({ children }: WebSocketProviderProps) {
  const [stompClient, setStompClient] = useState<CompatClient | undefined>(undefined);

  useEffect(() => {
    const socketFactory = () => new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "editor/ws");
    const client = Stomp.over(socketFactory);

    client.debug = () => {};

    function connect() {
      client.connect({}, function connection() {
        setStompClient(client);
      });
    }

    connect();

    return () => {
      if (client) {
        client.disconnect();
      }
    };
  }, []);

  return <WebSocketContext.Provider value={stompClient}>{children}</WebSocketContext.Provider>;
}
