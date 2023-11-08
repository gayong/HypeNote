"use client";

import Link from "next/link";
import Image from "next/image";
import React, { useEffect, useRef, useState } from "react";
import logo_blue from "./logo_blue.png";
import logokr from "./krlogo.png";
import plate from "./plate.png";
import signin from "./signin.png";
import signin_hover from "./signin_hover.png";
import signup from "./signup.png";
import signup_hover from "./signup_hover.png";
import left from "./left.png";
import left_hover from "./left_hover.png";
import bottom from "./bun_bottom.png";
import top from "./bun_top.png";
import lettuce from "./lettuce.png";
import patty from "./patty.png";
import cheese from "./cheese.png";
import arrow from "./arrow.png";
import line from "./line.png";
import line2 from "./line2.png";
import line3 from "./line3.png";
import bite from "./bite.png";
import "./Intro.css";
import { Button } from "antd";
import confetti from "canvas-confetti";

export default function Intro() {
  const [hover, setHover] = useState(false);
  const [inHover, setinHover] = useState(false);
  const [outHover, setoutHover] = useState(false);
  const sectionRef = [useRef(), useRef(), useRef(), useRef(), useRef()];
  const [currentSection, setCurrentSection] = useState(0);

  const toNext = () => {
    if (currentSection < sectionRef.length - 1) {
      setCurrentSection(currentSection + 1);
      sectionRef[currentSection + 1].current.scrollIntoView({ block: "center", behavior: "smooth" });
      // document.querySelector(".signin").style.opacity = 1;
      document.querySelectorAll(".signin").forEach((page) => {
        page.style.opacity = 1;
        page.style.transition = "all 0.5s ease-in-out";
      });
      if (currentSection === 3) {
        var defaults = {
          spread: 360,
          ticks: 50,
          gravity: 0,
          decay: 0.94,
          startVelocity: 30,
          colors: ["FFE400", "FFBD00", "E89400", "FFCA6C", "FDFFB8"],
        };

        function shoot() {
          confetti({
            ...defaults,
            particleCount: 40,
            scalar: 1.2,
            shapes: ["star"],
          });

          confetti({
            ...defaults,
            particleCount: 10,
            scalar: 0.75,
            shapes: ["circle"],
          });
        }

        setTimeout(shoot, 700);
        setTimeout(shoot, 800);
        setTimeout(shoot, 900);
        setTimeout(shoot, 1000);
      }
    }
  };

  const toPrev = () => {
    if (currentSection > 0) {
      setCurrentSection(currentSection - 1);
      sectionRef[currentSection - 1].current.scrollIntoView({ block: "center", behavior: "smooth" });
      // document.querySelector(".signin").style.opacity = 1;
      if (currentSection === 1) {
        document.querySelectorAll(".signin").forEach((page) => {
          page.style.opacity = 0;
        });
      }
    }
  };

  const toTop = () => {
    sectionRef[0].current.scrollIntoView({ block: "center", behavior: "smooth" });
  };

  useEffect(() => {
    let lastScrollY = 0;
    let currentScrollY = window.scrollY;

    let observer = new IntersectionObserver(
      (e) => {
        e.forEach((박스) => {
          if (박스.isIntersecting) {
            박스.target.style.opacity = 1;
            document.querySelectorAll(".patty").style.opacity = 1;
          } else {
            // if (!박스.target.classList.contains("except")) {
            박스.target.style.opacity = 0;
            if (lastScrollY > currentScrollY) {
              console.log("스크롤 위로");
              document.querySelector(".patty").style.opacity = 0;
            }
          }
          // }
          lastScrollY = currentScrollY;
        });
      },
      { threshold: 0.4 }
    );

    document.querySelectorAll(".page").forEach((page) => {
      observer.observe(page);
    });

    const onScroll = () => {
      setLastScrollTop(window.scrollY);
    };

    window.addEventListener("scroll", onScroll);

    return () => {
      window.removeEventListener("scroll", onScroll);
    };
  }, []);

  return (
    <div className="overflow-hidden p-0 m-0 w-full text-center items-center justify-center">
      <div className="mx-auto relative w-[100vw]">
        <div className="mt-[-100px] origin-[24%_50%] animate-spin-slow">
          <Image className="ml-[-150px] w-[68vw]" src={plate} alt="plate" />
          <Image
            className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[100px]"
            src={logo_blue}
            alt="logo"
          />
        </div>
        <Image className="animate-spin-slow w-[430px] absolute top-[5.2%] left-[11%]" src={bottom} alt="bottom" />
        <Image className="animate-spin-slow w-[430px] absolute top-[5.2%] left-[11%] " src={lettuce} alt="lettuce" />

        {/* 랜딩페이지 문구 */}
        <div className="load absolute top-[4.8%] left-[58.5%]" style={{ display: "flex", alignItems: "end" }}>
          <span className="test text-font_primary text-[110px] font-my ml-4 gra" ref={sectionRef[0]}>
            The Best
          </span>
        </div>
        <div className="load absolute top-[6.6%] left-[60%]" style={{ display: "flex", alignItems: "end" }}>
          <span className="test text-font_primary text-[110px] font-my ml-4 gra">Recipe</span>
        </div>
        <div className="load absolute top-[9.5%] left-[59.5%]" style={{ display: "flex", alignItems: "end" }}>
          {/* <span className=" text-font_primary text-[90px] font-marker ml-4">enjoy</span> */}
          {/* <Image className="w-[15vw] mb-4 ml-3" src={logokr} alt="logokr" /> */}
        </div>
        <div className="load absolute top-[8.7%] left-[59%]" style={{ display: "flex", alignItems: "end" }}>
          {/* <Image className="w-[14vw] ml-3" src={logokr} alt="logokr" /> */}
        </div>
        <div className="load absolute top-[9.1%] left-[61.5%]" style={{ display: "flex", alignItems: "end" }}>
          {/* <Image className="w-[14vw] ml-3" src={logokr} alt="logokr" /> */}
          <span className="text-font_primary text-[50px] font-my load ">for</span>
          <Image className="load w-[14vw] ml-3 mb-2" src={logokr} alt="logokr" />
        </div>

        {/* 메인화면 로고 아래 로그인/회원가입 */}
        <Link href="/signin">
          <div
            className="load absolute top-[11%] left-[60.5%]"
            onMouseOver={() => setoutHover(true)}
            onMouseOut={() => setoutHover(false)}>
            <Image className="load w-[100px]" src={outHover ? signin : signin_hover} alt="signin" />
          </div>
        </Link>
        <Link href="/signup">
          <div
            className="load absolute top-[11%] left-[67.5%]"
            onMouseOver={() => setinHover(true)}
            onMouseOut={() => setinHover(false)}>
            <Image className=" load w-[110px]" src={inHover ? signup : signup_hover} alt="signup" />
          </div>
        </Link>

        {/* 첫번째 기능 */}
        <div className="mt-[135px]">
          <div className="origin-[24%_50%] animate-spin-slow">
            <Image className="ml-[-150px] w-[68vw]" src={plate} alt="plate" />
            <Image
              className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[100px]"
              src={logo_blue}
              alt="logo"
            />
          </div>
          <Image className="animate-spin-slow w-[430px] absolute top-[26%] left-[11%]" src={bottom} alt="bottom" />
          <Image className="animate-spin-slow w-[430px] absolute top-[26%] left-[11%] " src={lettuce} alt="lettuce" />
          <Image
            className="z-10 animate-spin-slow w-[430px] absolute top-[26%] left-[11%] patty except"
            src={patty}
            alt="patty"
          />
          <Image className="top-[28.5%] right-[39%] w-[23vw] absolute" src={line} alt="first1" />
          {/* <Image className="top-[26%] left-[39%] w-[25vw] absolute" src={linesha} alt="first1" /> */}
          <h1 className="text-yellow italic font-preBd text-[120px] absolute top-[25.9%] left-[57%] z-20">1</h1>
          <h1 className="text-yellow font-preBd text-[40px] absolute top-[28%] left-[62%] z-20 " ref={sectionRef[1]}>
            더욱 똑똑해진 노트를 이용해요!
          </h1>
          <div className="text-yellow font-preLt text-[17px] w-[30.5rem] text-start absolute top-[29%] left-[62.1%] z-20">
            마크다운 형식을 지원해 간편한 노트 정리가 가능해요. 오른쪽 웹 검색 탭을 이용해 정보를 찾고, 내장된
            Chat-GPT를 활용해보세요.
          </div>
          <Link href="/signin">
            <div
              className="fixed top-[5%] right-6 signin opacity-0"
              onMouseOver={() => setoutHover(true)}
              onMouseOut={() => setoutHover(false)}>
              <Image className=" w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
            </div>
          </Link>
          <Link href="/signup">
            <div
              className="fixed top-[12.5%] right-6 signin opacity-0"
              onMouseOver={() => setinHover(true)}
              onMouseOut={() => setinHover(false)}>
              <Image className=" w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
            </div>
          </Link>
        </div>

        {/* 두번째 기능 */}
        <div className="mt-[110px] ">
          <div className="origin-[24%_50%] animate-spin-slow">
            <Image className="ml-[-150px] w-[68vw]" src={plate} alt="plate" />
            <Image
              className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[100px]"
              src={logo_blue}
              alt="logo"
            />
          </div>
          <Image className="animate-spin-slow w-[430px] absolute top-[46%] left-[11%]" src={bottom} alt="bottom" />
          <Image className="animate-spin-slow w-[430px] absolute top-[46%] left-[11%] " src={lettuce} alt="lettuce" />
          <Image
            className="z-10 animate-spin-slow w-[430px] absolute top-[46%] left-[11%] patty except"
            src={patty}
            alt="patty"
          />
          <Image
            className="z-20 animate-spin-slow w-[430px] absolute top-[46%] left-[11%] patty except"
            src={cheese}
            alt="cheese"
          />
          <Image className="top-[48.2%] right-[39%] w-[23vw] absolute " src={line2} alt="first1" />
          <h1 className="text-yellow italic font-preBd text-[120px] absolute top-[45.7%] left-[56.2%] z-20 ">2</h1>
          <h1 className="text-yellow font-preBd text-[40px] absolute top-[48%] left-[62%] z-20 " ref={sectionRef[2]}>
            노트를 한눈에 보기쉽게
          </h1>
          <div className="text-yellow font-preLt text-[17px] w-[25rem] text-start absolute top-[49%] left-[62.1%] z-20 ">
            노트를 노드 형태로 모아뒀어요. 노드를 누르면 해당 노트로 바로 이동한답니다. 친구에게 공유받은 노트도
            합쳐보세요!
          </div>
          {/* <Link href="/signin">
            <div
              className="fixed top-[5%] right-6"
              onMouseOver={() => setoutHover(true)}
              onMouseOut={() => setoutHover(false)}>
              <Image className=" w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
            </div>
          </Link> */}
          {/* <Link href="/signup">
            <div
              className="fixed top-[12.5%] right-6"
              onMouseOver={() => setinHover(true)}
              onMouseOut={() => setinHover(false)}>
              <Image className=" w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
            </div>
          </Link> */}
        </div>

        {/* 세번째 기능 */}
        <div className="mt-[120px] ">
          <div className="origin-[24%_50%] animate-spin-slow">
            <Image className="ml-[-150px] w-[68vw]" src={plate} alt="plate" />
            <Image
              className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[100px]"
              src={logo_blue}
              alt="logo"
            />
          </div>
          <Image className="animate-spin-slow w-[430px] absolute top-[67%] left-[11%]" src={bottom} alt="bottom" />
          <Image className="animate-spin-slow w-[430px] absolute top-[67%] left-[11%] " src={lettuce} alt="lettuce" />
          <Image
            className="z-10 animate-spin-slow w-[430px] absolute top-[67%] left-[11%] patty except"
            src={patty}
            alt="patty"
          />
          <Image
            className="z-20 animate-spin-slow w-[430px] absolute top-[67%] left-[11%] patty except"
            src={cheese}
            alt="cheese"
          />
          <Image
            className="z-30 animate-spin-slow w-[430px] absolute top-[67%] left-[11%] patty except"
            src={top}
            alt="top"
          />
          <Image className="top-[67.4%] right-[39%] w-[23vw] absolute " src={line3} alt="first1" />
          <h1 className="text-yellow italic font-preBd text-[120px] absolute top-[66.8%] left-[56.2%] z-20">3</h1>
          <h1 className="text-yellow font-preBd text-[40px] absolute top-[69%] left-[62%] z-20" ref={sectionRef[3]}>
            공부한 내용을 퀴즈로 풀어요
          </h1>
          <div className="text-yellow font-preLt text-[17px] w-[30rem] text-start absolute top-[70%] left-[62.1%] z-20">
            Chat-GPT가 생성한 사지선다 문제들을 풀며 복습해보세요. 혼자 풀기, 같이 풀기가 가능합니다. 친구들을 초대해
            대결해보세요! 퀴즈가 끝난 후 채팅을 통해 의견을 나눌 수도 있어요.
          </div>{" "}
          {/* <Link href="/signin">
            <div
              className="fixed top-[5%] right-6"
              onMouseOver={() => setoutHover(true)}
              onMouseOut={() => setoutHover(false)}>
              <Image className=" w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
            </div>
          </Link> */}
          {/* <Link href="/signup">
            <div
              className="fixed top-[12.5%] right-6"
              onMouseOver={() => setinHover(true)}
              onMouseOut={() => setinHover(false)}>
              <Image className=" w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
            </div>
          </Link> */}
        </div>

        {/* 마무리 멘트 */}
        <div className="mt-[120px] -mb-64 p-0 ">
          <div className="origin-[24%_50%] animate-spin-slow">
            <Image className="ml-[-150px] w-[68vw]" src={plate} alt="plate" />
            <Image
              className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[100px]"
              src={logo_blue}
              alt="logo"
            />
          </div>
          <Image className="animate-spin-slow w-[470px] absolute top-[87%] left-[8%]" src={bite} alt="bottom" />
          <h1 className="text-yellow italic font-preBd text-[120px] absolute top-[86.1%] left-[61.6%] z-20">4</h1>
          <h1 className="text-yellow font-preBd text-[40px] absolute top-[88.5%] left-[62%] z-20" ref={sectionRef[4]}>
            한입노트를 맛있게 즐기세요!
          </h1>
          {/* <div className="text-yellow font-preLt text-[17px] w-[30rem] text-start absolute top-[89.5%] left-[62.1%] z-20">
            당신의 CS 공부를 응원합니다..
          </div>{" "} */}
          <div
            onClick={toTop}
            className="border-2 border-yellow rounded-md text-yellow font-preBd text-[18px] h-[2.5rem] w-[8rem] text-center mt-2 pt-[5px] absolute top-[89.5%] left-[62.1%] z-20">
            처음으로
          </div>
          {/* <Link href="/signin">
            <div
              className="fixed top-[5%] right-6"
              onMouseOver={() => setoutHover(true)}
              onMouseOut={() => setoutHover(false)}>
              <Image className=" w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
            </div>
          </Link> */}
          {/* <Link href="/signup">
            <div
              className="fixed top-[12.5%] right-6"
              onMouseOver={() => setinHover(true)}
              onMouseOut={() => setinHover(false)}>
              <Image className=" w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
            </div>
          </Link> */}
        </div>

        {/* 햄버거 재료 */}
        {/* <Image className="animate-spin-slow w-[430px] fixed top-[30%] left-[11%]" src={bottom} alt="bottom" />
        <Image className="animate-spin-slow w-[430px] fixed top-[30%] left-[11%] " src={lettuce} alt="lettuce" /> */}
        {/* <Image
          className="z-10 animate-spin-slow w-[430px] fixed top-[30%] left-[11%] patty except"
          src={patty}
          alt="patty"
        /> */}
        {/* <Image
          className="z-10 animate-spin-slow w-[430px] fixed top-[30%] left-[11%] patty except"
          src={cheese}
          alt="cheese"
        /> */}
        {/* <Image
          className="z-10 animate-spin-slow w-[430px] fixed top-[30%] left-[11%] patty except"
          src={top}
          alt="top"
        /> */}

        {/* 왼쪽 문구 */}
        <div
          className="z-50 fixed top-[calc(50vh-185px)] left-[15px]"
          onMouseOver={() => setHover(true)}
          onMouseOut={() => setHover(false)}>
          <Image className="z-50  w-[150px]" src={hover ? left_hover : left} alt="왼쪽" />
        </div>

        {/* 위아래 화살표 */}
        <Image
          onClick={toPrev}
          className="w-[4vw] fixed top-[77%] right-[2%] rotate-180 animate-pulse hover:-translate-y-1 hover:scale-102 duration-300"
          src={arrow}
          alt="arrow"
        />
        <Image
          onClick={toNext}
          className="w-[4vw] fixed top-[86%] right-[2%] animate-pulse hover:translate-y-1 hover:scale-102 duration-300"
          src={arrow}
          alt="arrow"
        />
      </div>
    </div>
  );
}
