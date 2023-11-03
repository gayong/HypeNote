"use client";

import { SocketContext } from "@/context/SocketProvider";
import { useContext, useEffect, useState } from "react";
import { QuizRoomInfo } from "@/types/quiz";

interface QuizRoomProps {
  roomId: number;
}

export default function QuizRoom(props: QuizRoomProps) {
  const { setRoomNumber, room } = useContext(SocketContext);
  const [quizRoom, setQuizRoom] = useState<QuizRoomInfo | null>(null);

  useEffect(() => {
    setRoomNumber(props.roomId);
    console.log(props.roomId);

    if (room) {
      setQuizRoom(room);
      console.log(room);
      console.log("아ㅣㅁㅎ어ㅣㅓㅇ히ㅏㅁ이ㅏ허ㅣㅏㅓ");
    }
  }, [room]);

  return (
    <div>
      <h1 className="text-3xl font-bold">퀴즈방</h1>
      <div>{props.roomId}</div>
      <div>{quizRoom?.roomName}.</div>
      <div>레디 중 : {quizRoom?.readyCnt}</div>
      <div>
        {quizRoom?.roomCnt}/{quizRoom?.roomMax}
      </div>
      {quizRoom?.users.map((user) => {
        return <div key={user.userPk}>{user.userName}</div>;
      })}
    </div>
  );
}
