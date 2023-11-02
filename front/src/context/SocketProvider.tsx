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

  const socketFactory = () => new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
  const client = Stomp.over(socketFactory);

  // const socket = new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
  // const client = Stomp.over(socket);

  useEffect(() => {
    console.log("영어어어어어ㅓ언결확인좀");
    console.log(client.connected);
    console.log("영어어어어어ㅓ언결확인좀");
  }, [client.connected]);

  useEffect(() => {
    client.connect({}, () => {
      console.log("서버와 연결!", client.connected);
      sendRooms();
      // subscribeChat(8);
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
      console.log("방보여줘!", client.connected);

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
    client.subscribe(`/sub/quizroom/detail/${roomId}`, (room) => {
      setRoom(JSON.parse(room.body));
      console.log("방 들어간다.");
    });
  };
  const sendRoom = (roomId: number) => {
    console.log("여기");
    console.log("방구독!", client.connected);
    // if (!room) {
    // console.log("방이 있다잉");
    subscribeRoom(roomId);
    // }

    // const data = {
    //   userPk: "2",
    //   userName: "isc",
    //   host: false,
    //   ready: false,
    // };

    // client.send(`/pub/quizroom/in/${roomId}`, {}, JSON.stringify(data));
  };

  const subscribeChat = (roomId: number) => {
    client.subscribe(`/sub/chat/${roomId}`, (message) => {
      console.log("채팅", message);
    });
    const input = {
      userPk: "1",
      content: "Hello World!",
    };
    client.send(`/pub/chat/${roomId}`, {}, JSON.stringify(input));
  };

  return <SocketContext.Provider value={{ client, quizRooms, sendRoom }}>{children}</SocketContext.Provider>;
};
