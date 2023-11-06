"use client";

import SockJS from "sockjs-client";
import { useEffect, useState, createContext, useRef } from "react";
import { Stomp, CompatClient } from "@stomp/stompjs";
import React, { ReactNode } from "react";
import { useWebSocket } from "./SocketProvider";
import { QuizRoomInfo, QuizInfo, QuizResultInfo, chatUser } from "@/types/quiz";

interface Props {
  roomId: number;
  children: ReactNode;
}

export const SocketContext = createContext<{
  quizRooms: Array<QuizRoomInfo>;
  room: QuizRoomInfo | null;
  quizs: Array<QuizInfo>;
  quizResults: Array<QuizResultInfo>;
  quizRanking: Array<number>;
  chatMessages: Array<chatUser>;
}>({
  quizRooms: [],
  room: null,
  quizs: [],
  quizResults: [],
  quizRanking: [],
  chatMessages: [],
});

export default function SubscribeProvider({ roomId, children }: { roomId: number; children: React.ReactNode }) {
  const stompClient = useWebSocket();

  const [quizRooms, setQuizRooms] = useState([]);
  const [room, setRoom] = useState<QuizRoomInfo | null>(null);
  const [quizs, setQuizs] = useState<Array<QuizInfo>>([]);
  const [quizResults, setQuizResults] = useState<Array<QuizResultInfo>>([]);
  const [quizRanking, setQuizRanking] = useState<Array<number>>([]);
  const [chatMessages, setChatMessages] = useState<Array<chatUser>>([]);

  useEffect(() => {
    console.log(stompClient?.connected);
    if (stompClient) {
      console.log(roomId);

      // 방 구독
      // const roomSubscription =
      stompClient.subscribe(`/sub/quiz/${roomId}`, (response) => {
        console.log("방구독");
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

      // 채팅방 구독
      stompClient.subscribe(`/sub/chat/${roomId}`, (mes) => {
        console.log("채팅", mes.body);
        const message = JSON.parse(mes.body);
        setChatMessages((prevChatMessages) => [...prevChatMessages, message]);
      });
    }

    return () => {
      // 방나가기
      const data = {
        userPk: "2",
      };
      if (stompClient) {
        stompClient.send(`/pub/quizroom/out/${roomId}`, {}, JSON.stringify(data));
      }
    };
  }, [stompClient]);

  return (
    <SocketContext.Provider
      value={{
        quizRooms,
        room,
        quizs,
        quizRanking,
        quizResults,
        chatMessages,
      }}>
      {children}
    </SocketContext.Provider>
  );
}
