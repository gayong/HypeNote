"use client";

import SockJS from "sockjs-client";
import { useEffect, useState, createContext, useRef } from "react";

import React, { ReactNode } from "react";
import { useWebSocket } from "./SocketProvider";
import { QuizRanking, QuizRoomInfo, QuizInfo, QuizResultInfo, chatUser } from "@/types/quiz";
import { useAtom } from "jotai";
// import { userAtom } from "@/store/authAtom";
import { message } from "antd";
import useGetUserInfo from "@/hooks/useGetUserInfo";

interface Props {
  roomId: number;
  children: ReactNode;
}

export const SocketContext = createContext<{
  quizRooms: Array<QuizRoomInfo>;
  room: QuizRoomInfo | null;
  quizs: Array<QuizInfo>;
  quizResults: QuizResultInfo | null;
  quizRanking: Array<QuizRanking>;
  chatMessages: Array<chatUser>;
  quizReady: boolean;
}>({
  quizRooms: [],
  room: null,
  quizs: [],
  quizResults: null,
  quizRanking: [],
  chatMessages: [],
  quizReady: false,
});

export default function SubscribeProvider({ roomId, children }: { roomId: number; children: React.ReactNode }) {
  const stompClient = useWebSocket();
  const { data: user, isLoading, isError, error } = useGetUserInfo();

  const [quizRooms, setQuizRooms] = useState([]);
  const [room, setRoom] = useState<QuizRoomInfo | null>(null);
  const [quizs, setQuizs] = useState<Array<QuizInfo>>([]);
  const [quizResults, setQuizResults] = useState<QuizResultInfo | null>(null);
  const [quizRanking, setQuizRanking] = useState<Array<QuizRanking>>([]);
  const [chatMessages, setChatMessages] = useState<Array<chatUser>>([]);
  const [quizReady, setQuizReady] = useState<boolean>(false);
  // const [user] = useAtom(userAtom);

  useEffect(() => {
    const accessToken = localStorage.getItem("accessToken");

    if (stompClient && user) {
      // 방 구독
      stompClient.subscribe(
        `/sub/quiz/${roomId}`,
        (response) => {
          const responseBody = JSON.parse(response.body);
          // 방 정보
          if (responseBody.type === "detail") {
            setRoom(responseBody.result);
            // 퀴즈가 다 준비됐다면
            if (responseBody.quizReady && !quizReady) {
              message.info("퀴즈가 다 준비됐어요. READY버튼을 누르고 퀴즈를 시작해주세요.");
              setQuizReady(true);
            }
          }
          // 퀴즈
          else if (responseBody.type == "quiz") {
            message.warning(`퀴즈가 시작됩니다! ${responseBody.result.question.length * 30}초 이내에 푸십시오`);
            setQuizs(responseBody.result.question);
          }
          // 퀴즈 결과
          else if (responseBody.type == "result") {
            message.info("퀴즈가 끝났어요! 복습을 하신 후에 방을 나가주세요.");
            setQuizReady(true);
            setQuizResults(responseBody.result[user.userPk as number]);
            setQuizRanking(responseBody.ranking);
          }
        },
        {}
      );
      // 채팅방 구독
      stompClient.subscribe(
        `/sub/chat/${roomId}`,
        (mes) => {
          const message = JSON.parse(mes.body);
          setChatMessages((prevChatMessages) => [...prevChatMessages, message]);
        },
        {}
      );
    }

    return () => {
      // 방나가기
      const data = {
        userPk: user?.userPk,
      };
      if (stompClient) {
        stompClient.send(`/pub/quizroom/out/${roomId}`, {}, JSON.stringify(data));
        stompClient.unsubscribe(`/sub/quiz/${roomId}`);
        stompClient.unsubscribe(`/sub/chat/${roomId}`);
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
        quizReady,
      }}>
      {children}
    </SocketContext.Provider>
  );
}
