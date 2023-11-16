"use client";

import { useWebSocket } from "@/context/SocketProvider";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useEffect, useState } from "react";
import { QuizRoomInfo } from "@/types/quiz";
import { Button, message } from "antd";
import { useRouter } from "next/navigation";
import ChatRoom from "./ChatRoom";
import QuizStart from "./QuizStart";
import Card2 from "../ui/Card2";
import Timer from "../ui/Timer";
import QuizResult from "./QuizResult";
import Loading from "@/components/Loading";
import useGetUserInfo from "@/hooks/useGetUserInfo";

interface QuizRoomProps {
  roomId: number;
}

const notYet = {
  userName: "기다리는 중",
  userPk: 0,
  userImg: "/assets/유령5.png",
};

export default function QuizRoom(props: QuizRoomProps) {
  const { room, quizs, quizResults, quizReady } = useContext(SocketContext);
  const [ready, setReady] = useState<"unready" | "ready">("unready");
  const [start, setStart] = useState<boolean>(false);
  const [submit, setSubmit] = useState<boolean>(false);

  const stompClient = useWebSocket();
  // const [user] = useAtom(userAtom);
  const { data: user, isLoading, isError, error } = useGetUserInfo();

  const router = useRouter();

  useEffect(() => {
    if (!user) {
      return;
    }
    const data = {
      userPk: user.userPk,
      userName: user.nickName,
      userImg: user.profileImage,
    };
    if (stompClient) {
      stompClient.send(`/pub/quizroom/in/${props.roomId}`, {}, JSON.stringify(data));
    }
  }, [stompClient]);

  useEffect(() => {}, [room]);

  useEffect(() => {
    if (!user) {
      return;
    }

    const data = {
      userPk: user.userPk,
      action: ready === "ready" ? "ready" : "unready",
    };
    if (room && stompClient && user.userPk != room?.host) {
      stompClient.send(`/pub/quizroom/ready/${props.roomId}`, {}, JSON.stringify(data));
    }
  }, [ready]);

  // 퀴즈 시작
  useEffect(() => {
    if (stompClient && start) {
      stompClient.send(`/pub/quiz/${props.roomId}`, {});

      // 1초 후 퀴즈 배열 확인
      if (!quizReady) {
        message.loading("퀴즈를 만들고 있는 중이에요. 잠시 후 다시 시도해주세요.", 2);
        setStart(false);
      }
    }
  }, [start]);

  const outRoom = () => {
    router.replace("/quiz/room");
    console.log("방나가기");
  };

  return (
    <>
      {submit && quizResults ? (
        // 퀴즈 모두 다 푼 경우 => 랭킹 보여줌
        <>
          {/* <span
            className="font-bold absolute left-60 hover:text-hover_primary dark:hover:text-dark_font text-lg font-PreBd text-dark_background dark:text-font_primary absolute left-0 p-1 rounded-md"
            onClick={() => outRoom()}>
            {"< 나가기"}
          </span> */}

          <section className="px-10 pr-6 grid grid-cols-12 h-screen w-full max-w-full items-center">
            <QuizResult />
            <div className="pr-6 col-span-5">
              <ChatRoom roomId={props.roomId} height={80} />
            </div>
          </section>
        </>
      ) : submit && !quizResults ? (
        // 제출했지만 결과가 아직 없는 경우 => 로딩 화면
        <div className="px-10 pr-6 grid grid-cols-12 h-screen w-full max-w-full items-center">
          <div>
            <Loading />
            <h1>다른 친구들이 다 풀 때까지 잠시만 기다려 주세요.</h1>
            <h1>퀴즈 맞힌 개수가 같다면, 더 빨리 푼 사람이 이겨요!</h1>
          </div>
          <div className="pr-6 col-span-5">
            <ChatRoom roomId={props.roomId} height={80} />
          </div>
        </div>
      ) : quizs.length > 0 ? (
        // 퀴즈 게임 중
        <>
          <QuizStart roomId={props.roomId} setSubmit={setSubmit} />
          <Timer time={quizs.length} setSubmit={setSubmit} />
        </>
      ) : (
        // 퀴즈 게임 전
        <>
          <section className="grid grid-cols-12">
            <div className="col-span-7 flex flex-col">
              <div className="h-16 flex justify-center items-center relative">
                <span
                  className="font-bold hover:text-hover_primary dark:hover:text-dark_font text-lg font-PreBd text-dark_background dark:text-font_primary absolute left-0 p-1 rounded-md"
                  onClick={() => outRoom()}>
                  {"< 나가기"}
                </span>
                <div className="text-center">
                  <h1 className="text-3xl my-4 font-bold dark:text-dark_font text-primary flex">
                    {room?.roomName} {"   "}
                    <span className="ml-2 mt-1 text-xl font-bolder dark:text-font_primary text-yellow rounded-2xl">
                      {room?.roomCnt}
                      {"   "} / {"   "}
                      {room?.roomMax}명
                    </span>
                  </h1>
                </div>
              </div>

              <div className="mt-1 lg:grid lg:grid-cols-3 xl:grid-cols-4 sm:gap-6 xl:gap-2 lg:space-y-0">
                {room?.users.map((user) => {
                  return (
                    <>
                      <Card2 key={user.userPk} user={user} />
                    </>
                  );
                })}
                {/* 빈 카드 추가 (정원 8명에서 빼기)*/}
                {Array((room?.inviteUsers.length ?? 0) - (room?.users.length ?? 0))
                  .fill(0)
                  .map((_, index) => (
                    <Card2 key={`notyet-${index}`} user={notYet} />
                  ))}
                {/* 빈 카드 추가 (정원 8명에서 빼기)*/}
                {Array(8 - (room?.inviteUsers.length ?? 0))
                  .fill(0)
                  .map((_, index) => (
                    <Card2 key={`empty-${index}`} />
                  ))}
              </div>
            </div>

            <div className="col-span-5 pl-10 min-h-full mt-12">
              <div className="flex-col justify-between">
                <ChatRoom roomId={props.roomId} height={60} />
                <br />
                {user && room?.host === user.userPk ? (
                  // 방장일 경우
                  // 시작 가능
                  room.roomCnt === room.readyCnt ? (
                    <Button
                      className="dark:border-none dark:border-font_primary font-preRg bg-primary w-full h-16 text-3xl tracking-widest font-bold"
                      type="primary"
                      onClick={() => setStart(true)}>
                      START
                    </Button>
                  ) : (
                    // 시작 불가능
                    <Button
                      className="dark:border-none dark:border-font_primary font-preRg bg-[#4096ff] w-full h-16 text-3xl tracking-widest font-bold hover:bg-primary hover:text-font_primary"
                      type="primary"
                      onClick={() => message.warning("모두가 READY할 때까지 기다려주세요.")}>
                      WAIT
                    </Button>
                  )
                ) : // 방장이 아닐 경우
                // 레디한경우
                ready === "ready" ? (
                  <Button
                    className="dark:border-none dark:border-font_primary font-preRg bg-primary w-full h-16 text-3xl tracking-widest font-bold"
                    type="primary"
                    onClick={() => setReady("unready")}>
                    UNREADY
                  </Button>
                ) : (
                  // 레디안한경우
                  <Button
                    className="dark:border-none dark:border-font_primary font-preRg bg-[#4096ff] text-font_primary w-full h-16 text-3xl tracking-widest font-bold hover:bg-primary hover:text-font_primary"
                    // type="primary"
                    onClick={() => setReady("ready")}>
                    READY
                  </Button>
                )}
              </div>
            </div>
          </section>
        </>
      )}
    </>
  );
}
