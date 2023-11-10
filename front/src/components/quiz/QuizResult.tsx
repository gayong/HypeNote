"use client";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useEffect, useState } from "react";
import Image from "next/image";
import confetti from "canvas-confetti";

import { Button, Radio } from "antd";

export default function QuizResult() {
  const { quizResults, quizRanking } = useContext(SocketContext);
  const [tab, setTab] = useState("rank");

  const handleGetQuiz = () => {
    console.log("틀린문제를 공개해");
  };

  const handleGetRank = () => {
    console.log("랭킹을 공개해라");
  };

  useEffect(() => {
    var count = 200;
    var defaults = {
      origin: { y: 0.7 },
    };

    function fire(particleRatio: any, opts: any) {
      confetti({
        ...defaults,
        ...opts,
        particleCount: Math.floor(count * particleRatio),
      });
    }

    fire(0.25, {
      spread: 26,
      startVelocity: 55,
    });
    fire(0.2, {
      spread: 60,
    });
    fire(0.35, {
      spread: 120,
      decay: 0.91,
      scalar: 0.8,
    });
    fire(0.1, {
      spread: 160,
      startVelocity: 25,
      decay: 0.92,
      scalar: 1.2,
    });
    fire(0.1, {
      spread: 160,
      startVelocity: 45,
    });
  }, []);

  return (
    <section className="bg-white dark:bg-gray-900 w-full max-h-full pr-4">
      <div className="pt-6 flex justify-center">
        <Radio.Group defaultValue="rank" buttonStyle="solid">
          <Radio.Button
            value="rank"
            onClick={() => {
              handleGetRank();
              setTab("rank");
            }}>
            랭킹 보기
          </Radio.Button>
          <Radio.Button
            value="quiz"
            onClick={() => {
              handleGetQuiz();
              setTab("quiz");
              console.log("랭킹으로 바꿈");
            }}>
            문제 다시보기
          </Radio.Button>
        </Radio.Group>
      </div>

      {tab === "rank" ? (
        <div className="mr-2 mt-4 h-[620px] max-h-[620px] overflow-scroll scrollbar-hide">
          <h1 className="mt-2 font-extrabold text-2xl text-primary text-center dark:text-font_primary">
            퀴즈 결과입니다!
          </h1>

          {quizRanking.map((user, index) => (
            <>
              <div key={user.userPk} className="h-[70px] flex justify-between items-center px-6 ">
                <div className="flex items-center">
                  <h1 className="font-extrabold text-3xl text-primary dark:text-font_primary mr-6 drop-shadow-lg">
                    {user.ranking}
                  </h1>
                  {/* <div className="mr-2 rounded-full w-10 h-10 bg-cover bg-[url('/assets/profile.jpg')]" /> */}
                  <Image src={user.userImg} alt="프로필 이미지" width={20} height={20} priority />
                  <br />
                  <h1>{user.userName}</h1>
                </div>
                <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                  {user.correct} / {user.total}
                </p>
              </div>
              {index !== quizRanking.length - 1 && <hr className="opacity-20" />}
            </>
          ))}
        </div>
      ) : (
        <div className="mr-2 mt-4 h-[620px] max-h-[620px] flex items-center justify-center">
          {" "}
          <h1>시험지를 다시 띄워달라</h1>
        </div>
      )}
    </section>
  );
}
