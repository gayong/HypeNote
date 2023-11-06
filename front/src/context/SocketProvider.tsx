"use client";

import SockJS from "sockjs-client";
import { useEffect, useState, createContext, useRef } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";
import React, { ReactNode } from "react";
import { QuizRoomInfo, QuizInfo, QuizResultInfo, chatUser } from "@/types/quiz";

interface Props {
  children: ReactNode;
}

export const SocketContext = createContext<{
  client: CompatClient | null;
  quizRooms: Array<QuizRoomInfo>;
  room: QuizRoomInfo | null;
  quizs: Array<QuizInfo>;
  quizResults: Array<QuizResultInfo>;
  quizRanking: Array<number>;
  chatMessages: Array<chatUser>;
  setRoomNumber: (roomNumber: number | null) => void;
  sendReady: (roomNumber: number) => void;
  sendUnReady: (roomNumber: number) => void;
  sendOutRoom: (roomNumber: number) => void;
  sendRooms: () => void;
  sendMessage: (roomNumber: number, messageInput: object) => void;
  sendQuizStart: (roomNumber: number) => void;
}>({
  client: null,
  quizRooms: [],
  room: null,
  quizs: [],
  quizResults: [],
  quizRanking: [],
  chatMessages: [],
  setRoomNumber: () => {},
  sendReady: () => {},
  sendUnReady: () => {},
  sendOutRoom: () => {},
  sendRooms: () => {},
  sendMessage: () => {},
  sendQuizStart: () => {},
});

// const socketFactory = () => new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
// const client = Stomp.over(socketFactory);

// 소켓 기본 연결
export const SocketProvider: React.FC<Props> = ({ children }) => {
  const [roomNumber, setRoomNumber] = useState<number | null>(null);
  const [quizRooms, setQuizRooms] = useState([]);
  const [room, setRoom] = useState<QuizRoomInfo | null>(null);

  const [quizs, setQuizs] = useState<Array<QuizInfo>>([]);
  const [quizResults, setQuizResults] = useState<Array<QuizResultInfo>>([]);
  const [quizRanking, setQuizRanking] = useState<Array<number>>([]);

  const [chatMessages, setChatMessages] = useState<Array<chatUser>>([]);

  // const clientRef = useRef<CompatClient | null>(null);

  // if (clientRef.current === null) {
  //   const socketFactory = () => new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
  //   clientRef.current = Stomp.over(socketFactory()); // clientRef.current가 null인 경우에만 Stomp.over를 호출합니다.
  // }

  // const client = clientRef.current;

  const socketFactory = () => new SockJS(process.env.NEXT_PUBLIC_SERVER_URL + "quiz/stomp/ws");
  const client = Stomp.over(socketFactory);

  // 룸리스트 연결
  useEffect(() => {
    client.connect({}, () => {
      console.log("서버와 연결!", client.connected);

      // 지금 현재 방목록 불러오기
      if (quizRooms.length == 0) {
        subscribeRoomList();
      }
      // 룸 연결
      console.log(roomNumber);
      if (roomNumber) {
        console.log(roomNumber, "null아님");
        sendInRoom(roomNumber);
        subscribeChat(roomNumber);
      }
    });
    // return () => {
    // client.disconnect(() => {
    // console.log("서버 연결 해제");
    // });
    // };
  }, [roomNumber, client]);

  const subscribeRoomList = () => {
    client.subscribe("/sub/quizroom/roomList", (roomList) => {
      setQuizRooms(JSON.parse(roomList.body));
      // console.log(roomList.body);
    });
    sendRooms();
  };

  const sendRooms = () => {
    // if (quizRooms.length === 0) {
    //   console.log("방리스트구독!", client.connected);
    //   subscribeRoomList();
    // }

    client.send("/pub/quizroom/roomList", {});
  };

  // 방 구독
  const subscribeInRoom = (roomId: number) => {
    client.subscribe(`/sub/quiz/${roomId}`, (response) => {
      const responseBody = JSON.parse(response.body);
      console.log(responseBody);
      // 방 정보
      if (responseBody.type === "detail") {
        setRoom(responseBody.result);
      }
      // 퀴즈
      else if (responseBody.type == "quiz") {
        console.log("퀴즈", responseBody.result.question);
        setQuizs(responseBody.result.question);
      }
      // 퀴즈 결과
      else if (responseBody.type == "result") {
        setQuizResults(responseBody.result);
        setQuizRanking(responseBody.ranking);
      }
    });
  };

  // 입장
  const sendInRoom = (roomId: number) => {
    subscribeInRoom(roomId);

    const data = {
      userPk: "2",
      userName: "isc",
    };

    client.send(`/pub/quizroom/in/${roomId}`, {}, JSON.stringify(data));
  };

  //퇴장
  const sendOutRoom = (roomId: number) => {
    setRoomNumber(null);

    const data = {
      userPk: "2",
    };

    client.send(`/pub/quizroom/out/${roomId}`, {}, JSON.stringify(data));
    // sendUnReady(roomId);
    // client.unsubscribe(`/sub/quiz/${roomId}`);

    setQuizs([]);
    setQuizResults([]);
    setQuizRanking([]);
  };

  // 레디
  const sendReady = (roomId: number) => {
    console.log("레디합니데이");
    const data = {
      userPk: 2,
      action: "ready",
    };

    client.send(`/pub/quizroom/ready/${roomId}`, {}, JSON.stringify(data));
  };

  // 언레디
  const sendUnReady = (roomId: number) => {
    console.log("언레디");
    const data = {
      userPk: 2,
      action: "unready",
    };
    client.send(`/pub/quizroom/ready/${roomId}`, {}, JSON.stringify(data));
  };

  //채팅방 구독
  const subscribeChat = (roomId: number) => {
    client.subscribe(`/sub/chat/${roomId}`, (mes) => {
      console.log("채팅", mes.body);
      const message = JSON.parse(mes.body);
      setChatMessages((prevChatMessages) => [...prevChatMessages, message]);
      console.log("@@@@@@@@");
      console.log(chatMessages);
    });
  };
  // 채팅 전송
  const sendMessage = (roomId: number, messageInput: object) => {
    client.send(`/pub/chat/${roomId}`, {}, JSON.stringify(messageInput));
  };

  // 퀴즈 시작
  const sendQuizStart = (roomId: number) => {
    client.send(`/pub/quiz/${roomId}`, {});
  };

  return (
    <SocketContext.Provider
      value={{
        client,
        quizRooms,
        setRoomNumber,
        room,
        sendReady,
        sendOutRoom,
        sendRooms,
        sendUnReady,
        sendMessage,
        sendQuizStart,
        quizs,
        quizRanking,
        quizResults,
        chatMessages,
      }}>
      {children}
    </SocketContext.Provider>
  );
};
