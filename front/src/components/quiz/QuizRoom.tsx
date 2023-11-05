"use client";

import { SocketContext } from "@/context/SocketProvider";
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
  const { setRoomNumber, room, sendReady, sendOutRoom, sendUnReady, quizs } = useContext(SocketContext);
  const [quizRoom, setQuizRoom] = useState<QuizRoomInfo | null>(null);
  const [ready, setReady] = useState<boolean>(false);

  const router = useRouter();

  useEffect(() => {
    setRoomNumber(props.roomId);
    if (room) {
      setQuizRoom(room);
    }
  }, [room]);

  const outRoom = () => {
    sendOutRoom(props.roomId);
    router.push("/quiz/room");
  };

  const readyBtn = () => {
    if (ready) {
      sendUnReady(props.roomId);
    } else {
      sendReady(props.roomId);
    }

    setReady(!ready);
  };

  return (
    <div>
      <h1 className="text-3xl font-bold">{quizRoom?.roomName}</h1>
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
          <div>레디 중 : {quizRoom?.readyCnt}</div>
          <div>
            {quizRoom?.roomCnt}/{quizRoom?.roomMax}
          </div>
          {quizRoom?.users.map((user) => {
            return <div key={user.userPk}>{user.userName}</div>;
          })}

          <Button
            className="dark:border dark:border-font_primary"
            style={{ fontFamily: "preRg", backgroundColor: "#2946A2" }}
            type="primary"
            onClick={() => readyBtn()}>
            {ready ? "언레디" : "레디"}
          </Button>
          <QuizResult />
          <ChatRoom roomId={props.roomId} />
        </>
      )}
    </div>
  );
}
