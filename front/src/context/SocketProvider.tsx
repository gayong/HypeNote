"use client";

import SockJS from "sockjs-client";
import { useEffect, useRef, useState, createContext } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";
import React, { ReactNode } from "react";
import { QuizRoomInfo } from "@/types/quiz";

interface Props {
  children: ReactNode;
}

export const SocketContext = createContext<{ client: CompatClient | null; quizRooms: Array<QuizRoomInfo> }>({
  client: null,
  quizRooms: [],
});

// 소켓 기본 연결
export const SocketProvider: React.FC<Props> = ({ children }) => {
  const [quizRooms, setQuizRooms] = useState([]);

  const socket = new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
  const client = Stomp.over(socket);

  useEffect(() => {
    client.connect({}, () => {
      console.log("서버와 연결!");
      getRoom();
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
      setQuizRooms(JSON.parse(roomList.body));
      console.log(roomList.body);
      // console.log("확인%%%", quizRooms);
    });
  };

  useEffect(() => {
    console.log("확인%%%", quizRooms);
  }, [quizRooms]);

  const getRoom = () => {
    console.log("담겻나", quizRooms);
    if (quizRooms.length === 0) {
      console.log("출발");
      roomList();
    }
    client.send("/pub/quizroom/roomList", {});
    // console.log("확인", quizRooms);
  };

  return <SocketContext.Provider value={{ client, quizRooms }}>{children}</SocketContext.Provider>;
};
