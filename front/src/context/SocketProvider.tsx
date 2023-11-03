"use client";

import SockJS from "sockjs-client";
import { useEffect, useRef, useState, createContext } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";
import React, { ReactNode } from "react";
import { QuizRoomInfo, QuizRoomDetail } from "@/types/quiz";

interface Props {
  children: ReactNode;
}

export const SocketContext = createContext<{
  client: CompatClient | null;
  quizRooms: Array<QuizRoomInfo>;
  room: QuizRoomInfo | null;
  setRoomNumber: (roomNumber: number | null) => void;
}>({
  client: null,
  quizRooms: [],
  room: null,
  setRoomNumber: () => {},
});

// 소켓 기본 연결
export const SocketProvider: React.FC<Props> = ({ children }) => {
  const [roomNumber, setRoomNumber] = useState<number | null>(null);
  const [quizRooms, setQuizRooms] = useState([]);
  const [room, setRoom] = useState<QuizRoomInfo | null>(null);

  const socketFactory = () => new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
  const client = Stomp.over(socketFactory);

  // const socket = new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
  // const client = Stomp.over(socket);

  useEffect(() => {
    client.connect({}, () => {
      console.log("서버와 연결!", client.connected);
      sendRooms();
      // 룸 연결
      if (roomNumber) {
        sendRoom(roomNumber);
      }
    });

    return () => {
      client.disconnect(() => {
        console.log("서버 연결 해제");
      });
    };
  }, [roomNumber]);

  const subscribeRoomList = () => {
    client.subscribe("/sub/quizroom/roomList", (roomList) => {
      setQuizRooms(JSON.parse(roomList.body));
      console.log(roomList.body);
    });
  };

  const sendRooms = () => {
    if (quizRooms.length === 0) {
      console.log("방리스트구독!", client.connected);
      subscribeRoomList();
    }
    client.send("/pub/quizroom/roomList", {});
  };

  const subscribeRoom = (roomId: number) => {
    console.log("진짜 제발요");

    client.subscribe(`/sub/quiz/${roomId}`, (response) => {
      const responseBody = JSON.parse(response.body);
      setRoom(responseBody.result);
    });
  };

  const sendRoom = (roomId: number) => {
    console.log(room, "dalkghakldgjhlakdjgklajlkg");
    // if (room !== roomId) {
    subscribeRoom(roomId);
    // }

    const data = {
      userPk: "2",
      userName: "윤자현",
    };

    client.send(`/pub/quizroom/in/${roomId}`, {}, JSON.stringify(data));
  };

  useEffect(() => {
    return () => {};
  }, []);

  const subscribeChat = (roomId: number) => {
    client.subscribe(`/sub/chat/${roomId}`, (message) => {
      console.log("채팅", message);
    });
    const input = {
      userPk: "1",
      content: "난 바보야.",
    };
    client.send(`/pub/chat/${roomId}`, {}, JSON.stringify(input));
  };

  return <SocketContext.Provider value={{ client, quizRooms, setRoomNumber, room }}>{children}</SocketContext.Provider>;
};
