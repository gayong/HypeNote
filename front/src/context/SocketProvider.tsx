"use client";

import SockJS from "sockjs-client";
import { useEffect, useRef, useState, createContext } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";
import React, { ReactNode } from "react";
import { QuizRoomInfo } from "@/types/quiz";

interface Props {
  children: ReactNode;
}

export const SocketContext = createContext<{
  client: CompatClient | null;
  quizRooms: Array<QuizRoomInfo>;
  sendRoom: (roomId: number) => void;
}>({
  client: null,
  quizRooms: [],
  sendRoom: () => {},
});

// 소켓 기본 연결
export const SocketProvider: React.FC<Props> = ({ children }) => {
  const [quizRooms, setQuizRooms] = useState([]);
  const [room, setRoom] = useState({});
  const socket = new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
  const client = Stomp.over(socket);

  useEffect(() => {
    client.connect({}, () => {
      console.log("서버와 연결!");
      sendRooms();
    });

    return () => {
      client.disconnect(() => {
        console.log("서버 연결 해제");
      });
    };
  }, []);

  const subscribeRoomList = () => {
    client.subscribe("/sub/quizroom/roomList", (roomList) => {
      console.log("방보여줘");
      setQuizRooms(JSON.parse(roomList.body));
      console.log(roomList.body);
    });
  };

  const sendRooms = () => {
    if (quizRooms.length === 0) {
      console.log("출발");
      subscribeRoomList();
    }
    client.send("/pub/quizroom/roomList", {});
  };

  const subscribeRoom = (roomId: number) => {
    client.subscribe(`/sub/quizroom/detail/${roomId}`, (room) => {
      setRoom(JSON.parse(room.body));
      console.log("방 들어간다.");
    });
  };
  const sendRoom = (roomId: number) => {
    if (room) {
      console.log("방이 있다잉");
      subscribeRoom(roomId);
    }
    client.send(`/sub/quizroom/detail/${roomId}`, {});
  };

  return <SocketContext.Provider value={{ client, quizRooms, sendRoom }}>{children}</SocketContext.Provider>;
};
