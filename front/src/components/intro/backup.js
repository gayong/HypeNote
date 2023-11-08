"use client";

import Link from "next/link";
import Image from "next/image";
import React, { useEffect, useRef, useState } from "react";
import intro from "./intro_logo.png";
import logo from "./logo.png";
import logo_blue from "./logo_blue.png";
import logokr from "./krlogo.png";
import white from "./download.svg";
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
import "./Intro.css";

export default function Intro() {
  const [hover, setHover] = useState(false);
  const [inHover, setinHover] = useState(false);
  const [outHover, setoutHover] = useState(false);
  const [lastScrollTop, setLastScrollTop] = useState(0);

  useEffect(() => {
    let lastScrollY = 0;
    let currentScrollY = window.scrollY;

    let observer = new IntersectionObserver(
      (e) => {
        e.forEach((박스) => {
          if (박스.isIntersecting) {
            박스.target.style.opacity = 1;
            document.querySelector(".patty").style.opacity = 1;
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
        {/* <div className="mt-[-100px] origin-[24%_50%] animate-spin-slow">
          <Image className="ml-[-150px] w-[65rem]" src={plate} alt="plate" />
          <Image
            className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[6rem]"
            src={logo_blue}
            alt="logo"
          />
        </div> */}

        {/* 랜딩페이지 문구 */}
        <div className="load absolute top-[4.8%] left-[58.5%]" style={{ display: "flex", alignItems: "end" }}>
          <span className="test text-font_primary text-[110px] font-my ml-4 gra">The Best</span>
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
        <div className="mt-[135px] page">
          <div className="origin-[24%_50%] animate-spin-slow">
            <Image className="ml-[-150px] w-[68vw]" src={plate} alt="plate" />
            <Image
              className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[100px]"
              src={logo_blue}
              alt="logo"
            />
          </div>
          <h1 className="text-font_primary text-3xl absolute top-[27%] right-[20%]">1. 내 뇌를 보여줘요</h1>

          <Link href="/signin">
            <div
              className="fixed top-[5%] right-6"
              onMouseOver={() => setoutHover(true)}
              onMouseOut={() => setoutHover(false)}>
              <Image className="load w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
            </div>
          </Link>
          <Link href="/signup">
            <div
              className="fixed top-[12.5%] right-6"
              onMouseOver={() => setinHover(true)}
              onMouseOut={() => setinHover(false)}>
              <Image className="load w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
            </div>
          </Link>
        </div>

        {/* 두번째 기능 */}
        <div className="mt-[110px] page">
          <div className="origin-[24%_50%] animate-spin-slow">
            <Image className="ml-[-150px] w-[68vw]" src={plate} alt="plate" />
            <Image
              className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[100px]"
              src={logo_blue}
              alt="logo"
            />
          </div>
          <h1 className="text-font_primary text-3xl absolute top-[48%] right-[20%]">2. 에디터 기능을 줘요</h1>
          <Link href="/signin">
            <div
              className="fixed top-[5%] right-6"
              onMouseOver={() => setoutHover(true)}
              onMouseOut={() => setoutHover(false)}>
              <Image className="load w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
            </div>
          </Link>
          <Link href="/signup">
            <div
              className="fixed top-[12.5%] right-6"
              onMouseOver={() => setinHover(true)}
              onMouseOut={() => setinHover(false)}>
              <Image className="load w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
            </div>
          </Link>
        </div>

        {/* 세번째 기능 */}
        <div className="mt-[120px] page">
          <div className="origin-[24%_50%] animate-spin-slow">
            <Image className="ml-[-150px] w-[68vw]" src={plate} alt="plate" />
            <Image
              className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[100px]"
              src={logo_blue}
              alt="logo"
            />
          </div>
          <h1 className="text-font_primary text-3xl absolute top-[68%] right-[20%]">3. 퀴즈를 줘요</h1>
          <Link href="/signin">
            <div
              className="fixed top-[5%] right-6"
              onMouseOver={() => setoutHover(true)}
              onMouseOut={() => setoutHover(false)}>
              <Image className="load w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
            </div>
          </Link>
          <Link href="/signup">
            <div
              className="fixed top-[12.5%] right-6"
              onMouseOver={() => setinHover(true)}
              onMouseOut={() => setinHover(false)}>
              <Image className="load w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
            </div>
          </Link>
        </div>

        {/* 마무리 멘트 */}
        <div className="mt-[120px] page">
          <div className="origin-[24%_50%] animate-spin-slow">
            <Image className="ml-[-150px] w-[68vw]" src={plate} alt="plate" />
            <Image
              className="rotate-[27deg] absolute top-[4.5%] left-[35%] mx-auto pt-10 w-[100px]"
              src={logo_blue}
              alt="logo"
            />
          </div>
          <h1 className="text-font_primary text-3xl absolute top-[90%] right-[20%]">4. Enjoy your meal</h1>

          <Link href="/signin">
            <div
              className="fixed top-[5%] right-6"
              onMouseOver={() => setoutHover(true)}
              onMouseOut={() => setoutHover(false)}>
              <Image className="load w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
            </div>
          </Link>
          <Link href="/signup">
            <div
              className="fixed top-[12.5%] right-6"
              onMouseOver={() => setinHover(true)}
              onMouseOut={() => setinHover(false)}>
              <Image className="load w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
            </div>
          </Link>
        </div>

        {/* 햄버거 재료 */}
        <Image className="animate-spin-slow w-[430px] fixed top-[30%] left-[11%]" src={bottom} alt="bottom" />
        <Image
          className="animate-spin-slow w-[430px] fixed top-[30%] left-[11%] patty except"
          src={lettuce}
          alt="lettuce"
        />
        <Image
          className="z-10 animate-spin-slow w-[430px] fixed top-[30%] left-[11%] patty except"
          src={patty}
          alt="patty"
        />
        <Image
          className="z-10 animate-spin-slow w-[430px] fixed top-[30%] left-[11%] patty except"
          src={cheese}
          alt="cheese"
        />
        <Image
          className="z-10 animate-spin-slow w-[430px] fixed top-[30%] left-[11%] patty except"
          src={top}
          alt="top"
        />
        <Link href="/signin" className="patty except opacity-0">
          <div
            className="fixed top-[5%] right-6"
            onMouseOver={() => setoutHover(true)}
            onMouseOut={() => setoutHover(false)}>
            <Image className="load w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
          </div>
        </Link>
        <Link href="/signup" className="patty except opacity-0">
          <div
            className="fixed top-[12.5%] right-6"
            onMouseOver={() => setinHover(true)}
            onMouseOut={() => setinHover(false)}>
            <Image className="load w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
          </div>
        </Link>
        {/* <Image
          className="z-10 animate-spin-slow w-[430px] fixed top-[30%] left-[11%] opacity-0 patty except"
          src={patty}
          alt="patty"
        /> */}
        {/* <Link href="/signin">
          <div
            className="fixed top-[5%] right-6"
            onMouseOver={() => setoutHover(true)}
            onMouseOut={() => setoutHover(false)}>
            <Image className="load w-[150px]" src={outHover ? signin_hover : signin} alt="signin" />
          </div>
        </Link> */}
        {/* <Link href="/signup">
          <div
            className="fixed top-[12.5%] right-6"
            onMouseOver={() => setinHover(true)}
            onMouseOut={() => setinHover(false)}>
            <Image className="load w-[150px]" src={inHover ? signup_hover : signup} alt="signup" />
          </div>
        </Link> */}

        <div
          className="z-50 fixed top-[calc(50vh-185px)] left-[15px]"
          onMouseOver={() => setHover(true)}
          onMouseOut={() => setHover(false)}>
          <Image className="z-50 load w-[150px]" src={hover ? left_hover : left} alt="왼쪽" />
        </div>
        <h1 className="text-[70px] text-font_primary rotate-90 fixed top-[85%] right-[2%] animate-pulse">{">"}</h1>
      </div>
    </div>
  );
}
