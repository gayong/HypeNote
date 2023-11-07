"use client";

import { useWebSocket } from "@/context/SocketProvider";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useEffect, useState } from "react";
import { QuizRoomInfo } from "@/types/quiz";
import { Button } from "antd";
import { useRouter } from "next/navigation";
import ChatRoom from "./ChatRoom";
import QuizStart from "./QuizStart";
import QuizResult from "./QuizResult";

interface QuizRoomProps {
  roomId: number;
}

export default function QuizRoom(props: QuizRoomProps) {
  const { room, quizs } = useContext(SocketContext);
  const [quizRoom, setQuizRoom] = useState<QuizRoomInfo | null>(null);
  const [ready, setReady] = useState<"unready" | "ready">("unready");
  const stompClient = useWebSocket();

  const router = useRouter();

  useEffect(() => {
    const data = {
      userPk: "2",
      userName: "isc",
    };
    if (stompClient) {
      stompClient.send(`/pub/quizroom/in/${props.roomId}`, {}, JSON.stringify(data));
    }
  }, []);
  useEffect(() => {
    const data = {
      userPk: 2,
      action: ready === "ready" ? "ready" : "unready",
    };
    if (stompClient) {
      console.log("보냄");
      stompClient.send(`/pub/quizroom/ready/${props.roomId}`, {}, JSON.stringify(data));
    }
  }, [ready]);

  useEffect(() => {
    setQuizRoom(room);
  }, [room]);

  const outRoom = () => {
    router.push("/quiz/room");
    console.log("방나가기");
  };

  return (
    <div>
      <h1 className="text-3xl font-bold">{room?.roomName}</h1>
      <div>{props.roomId}</div>
      <Button
        className="dark:border dark:border-font_primary"
        style={{ fontFamily: "preRg", backgroundColor: "#2946A2" }}
        type="primary"
        onClick={() => outRoom()}>
        퇴장
      </Button>
      {quizs.length > 0 ? (
        // 퀴즈게임중
        <QuizStart />
      ) : (
        // 퀴즈 게임 전
        <>
          <div>레디 중 : {room?.readyCnt}</div>
          <div>
            {room?.roomCnt}/{room?.roomMax}
          </div>
          {room?.users.map((user) => {
            return <div key={user.userPk}>{user.userName}</div>;
          })}

          <Button
            className="dark:border dark:border-font_primary font-preRg bg-primary"
            type="primary"
            onClick={() => setReady(ready === "ready" ? "unready" : "ready")}>
            {ready === "ready" ? "레디 취소" : "레디"}
          </Button>
          <QuizResult />
          <ChatRoom roomId={props.roomId} />
        </>
      )}
    </div>
  );
}
