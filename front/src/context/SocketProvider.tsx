"use client";

import SockJS from "sockjs-client";
import { useEffect, useRef, useState, createContext } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";
import React, { ReactNode } from "react";

interface Props {
  children: ReactNode;
}

const SocketContext = createContext<CompatClient | null>(null);
// 소켓 기본 연결
export const SocketProvider: React.FC<Props> = ({ children }) => {
  const [quizRooms, setQuizRooms] = useState("");

  const socket = new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
  const client = Stomp.over(socket);

  useEffect(() => {
    client.connect({}, () => {
      console.log("서버와 연결!");
      roomList();
    });

    return () => {
      client.disconnect(() => {
        console.log("서버 연결 해제");
      });
    };
  }, []);

  const roomList = () => {
    client.subscribe("/sub/quizroom/roomList", (roomList) => {
      console.log("방보여줘");
      console.log(roomList.body);
      setQuizRooms(JSON.parse(roomList.body));
    });
  };

  return <SocketContext.Provider value={client}>{children}</SocketContext.Provider>;
};
