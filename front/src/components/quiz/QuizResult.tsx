"use client";
import { SocketContext } from "@/context/SubscribeProvider";
import { useContext, useEffect, useState } from "react";
import Image from "next/image";
import confetti from "canvas-confetti";
import { useRouter } from "next/navigation";
import { Button, Radio } from "antd";

import first from "../../../public/assets/first.png";
import second from "../../../public/assets/second.png";
import third from "../../../public/assets/third.png";

export default function QuizResult() {
  const { quizResults, quizRanking } = useContext(SocketContext);
  const [tab, setTab] = useState("rank");
  const router = useRouter();

  const handleGetQuiz = () => {
    console.log("틀린문제를 공개해");
  };

  const handleGetRank = () => {
    console.log("랭킹을 공개해라");
  };

  const outRoom = () => {
    router.replace("/quiz/room");
    console.log("방나가기");
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
    <section className="bg-white dark:bg-gray-900 w-full max-h-full col-span-1">
      <span
        className="hover:text-hover_primary text-lg font-PreBd font-normal text-dark_background dark:text-font_primary absolute left-72 p-1 rounded-md outline outline-2 outline-dark_background dark:outline-font_primary "
        onClick={() => outRoom()}>
        {"< 나가기"}
      </span>
      <div className="-mt-7 flex justify-center">
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

          <div className="mt-3 flex justify-center items-center p-3">
            {quizRanking.length >= 3
              ? [quizRanking[1], quizRanking[0], quizRanking[2]].map((item, index) => {
                  // 멤버가 세 명일때
                  if (index === 0) {
                    // 2등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[150px] h-[170px]">
                        <div>
                          <Image src={second} className="w-[80px] h-auto absolute ml-[-40px] mt-[-17px]" alt="2" />
                          <div className="h-[100px] w-[100px] flex items-center justify-center">
                            <Image
                              src={item.userImg}
                              alt="프로필 이미지"
                              width={100}
                              height={100}
                              priority
                              className="rounded-full object-cover h-[80px] w-[80px]"></Image>
                          </div>
                          {/* <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" /> */}
                          <div className="dark:text-dark_primary flex justify-center">{item.userName}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  } else if (index === 1) {
                    // 1등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[180px] h-[190px]">
                        <div>
                          <Image src={first} className="w-[100px] h-auto absolute ml-[-60px] mt-[-29px]" alt="1" />
                          <div className="h-[100px] w-[100px] flex items-center justify-center">
                            <Image
                              src={item.userImg}
                              alt="프로필 이미지"
                              width={100}
                              height={100}
                              priority
                              className="rounded-full object-cover h-[100px] w-[100px]"></Image>
                          </div>
                          {/* <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" /> */}
                          <div className="dark:text-dark_primary flex justify-center">{item.userName}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  } else {
                    // 3등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[150px] h-[170px]">
                        <div>
                          <Image src={third} className="w-[80px] h-auto absolute ml-[-40px]  mt-[-17px]" alt="3" />
                          <div className="h-[100px] w-[100px] flex items-center justify-center">
                            <Image
                              src={item.userImg}
                              alt="프로필 이미지"
                              width={100}
                              height={100}
                              priority
                              className="rounded-full object-cover h-[80px] w-[80px]"></Image>
                          </div>
                          {/* <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" /> */}
                          <div className="dark:text-dark_primary flex justify-center">{item.userName}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  }
                })
              : quizRanking.length === 2 // 멤버가 두 명일때
              ? [quizRanking[0], quizRanking[1]].map((item, index) => {
                  if (index === 0) {
                    // 1등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[180px] h-[190px]">
                        <div>
                          <Image src={first} className="w-[100px] h-auto absolute ml-[-60px] mt-[-29px]" alt="1" />
                          <div className="h-[100px] w-[100px] flex items-center justify-center">
                            <Image
                              src={item.userImg}
                              alt="프로필 이미지"
                              width={100}
                              height={100}
                              priority
                              className="rounded-full object-cover h-[100px] w-[100px]"></Image>
                          </div>
                          {/* <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" /> */}
                          <div className="flex justify-center dark:text-dark_primary">{item.userName}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  } else if (index === 1) {
                    // 2등
                    return (
                      <div
                        key={index}
                        className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[150px] h-[170px]">
                        <div>
                          <Image src={second} className="w-[80px] h-auto absolute ml-[-40px]  mt-[-17px]" alt="2" />
                          <div className="h-[100px] w-[100px] flex items-center justify-center">
                            <Image
                              src={item.userImg}
                              alt="프로필 이미지"
                              width={100}
                              height={100}
                              priority
                              className="rounded-full object-cover h-[80px] w-[80px]"></Image>
                          </div>
                          {/* <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" /> */}
                          <div className="flex justify-center dark:text-dark_primary">{item.userName}</div>
                          <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                            {item.correct} / {item.total}
                          </p>
                        </div>
                      </div>
                    );
                  }
                })
              : quizRanking.map((item, index) => {
                  // 혼자일 때 => 혹시 혼자풀기 결과도 같은 페이지 쓴다면 필요, 아님 그냥 null 해도 됨
                  return (
                    <div
                      key={index}
                      className="hover:-translate-y-2 duration-300 p-5 flex items-center justify-center mr-5 bg-font_primary shadow-lg dark:shadow-font_secondary rounded-xl w-[180px] h-[190px]">
                      <div>
                        <Image src={first} className="w-[100px] h-auto absolute ml-[-60px] mt-[-29px]" alt="1" />
                        <div className="h-[100px] w-[100px] flex items-center justify-center">
                          <Image
                            src={item.userImg}
                            alt="프로필 이미지"
                            width={100}
                            height={100}
                            priority
                            className="rounded-full object-cover h-[100px] w-[100px]"></Image>
                        </div>
                        {/* <div className="mb-2 ml-2 mr-2 rounded-full w-16 h-16 bg-cover bg-[url('/assets/profile.jpg')]" /> */}
                        <div className="flex justify-center">{item.userName}</div>
                        <p className="font-PreBd text-sm text-[#ffd51c] flex justify-center">
                          {item.correct} / {item.total}
                        </p>
                      </div>
                    </div>
                  );
                })}
          </div>

          {quizRanking.slice(3).map((user, index) => (
            <>
              <div key={user.userPk} className="h-[70px] flex justify-between items-center px-6 ">
                <div className="flex items-center">
                  <h1 className="font-extrabold text-3xl text-primary dark:text-font_primary mr-6 drop-shadow-lg">
                    {user.ranking}
                  </h1>
                  {/* <div className="mr-2 rounded-full w-10 h-10 bg-cover bg-[url('/assets/profile.jpg')]" /> */}
                  <div className="h-[100px] w-[10px] flex items-center justify-center dark:text-dark_primary">
                    <Image
                      src={user.userImg}
                      alt="프로필 이미지"
                      width={100}
                      height={100}
                      priority
                      className="rounded-full object-cover h-[40px] w-[40px]"></Image>
                  </div>
                  <div className="w-4"></div>
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
        <div className="mr-2 h-[620px] max-h-[620px] flex items-center justify-center mt-5 ">
          <div className="overflow-y-scroll overflow-x-hidden mt-5 h-full">
            {quizResults?.questionResult.map((quiz, index) => (
              <div key={index} className="pt-16">
                <div className="flex items-center relative px-10">
                  {quiz.myAnswer === quiz.answer ? (
                    <Image src="/assets/cor.png" alt="맞음" width={110} height={110} className="absolute left-0.5" />
                  ) : (
                    <Image src="/assets/wr.png" alt="틀림" width={70} height={70} className="absolute left-6 -top-10" />
                  )}

                  <h1 className="text-4xl font-preBd text-dark_font">{quiz.id}.</h1>
                  <span className="ml-2 mt-1 text-xl font-preBd dark:text-font_primary">{quiz.question}</span>
                </div>

                {/* <h2>{quiz.myAnswer}</h2> */}

                <div className="ml-7">
                  {quiz.example.map((ex, index) => (
                    <p key={ex.ex} className="w-full">
                      <p
                        className={`pr-3 w-full py-3 mx-2 text-lg ${
                          // 퀴즈 정답인 것
                          quiz.answer === ex.ex ? "font-bold text-dark_font" : ""
                        }
                            ${
                              quiz.myAnswer === ex.ex && quiz.myAnswer !== quiz.answer
                                ? "underline decoration-[#ff4a4a] decoration-2 underline-offset-4 decoration-wavy font-bold"
                                : ""
                            }`}>
                        {ex.ex}. {ex.content}
                      </p>
                    </p>
                  ))}
                </div>
                <br />
              </div>
            ))}
          </div>
        </div>
      )}
    </section>
  );
}
