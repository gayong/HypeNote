"use client";

import Link from "next/link";
import Image from "next/image";
import React, { useEffect, useRef, useState } from "react";
import intro from "./intro_logo.png";
import logo from "./logo.png";
import logo_blue from "./logo_blue.png";
import white from "./download.svg";
import plate from "./plate.png";
import signin from "./signin.png";
import signin_hover from "./signin_hover.png";
import signup from "./signup.png";
import signup_hover from "./signup_hover.png";
import left from "./left.png";
import left_hover from "./left_hover.png";
import bottom from "./bun_bottom.png";
import lettuce from "./lettuce.png";
import patty from "./patty.png";
import "./Intro.css";

export default function Intro() {
  const [hover, setHover] = useState(false);
  const [inHover, setinHover] = useState(false);
  const [outHover, setoutHover] = useState(false);

  return (
    <div className="overflow-hidden p-0 m-0 w-full text-center items-center justify-center">
      {/* <Image className="w-[800px]" src={intro} alt="logo" /> */}
      <Image className="animate-pulse mx-auto pt-10 w-[120px]" src={logo} alt="logo" />
      <div className="mt-5 mx-auto relative w-[100vw] h-[300vh]">
        {/* <div className="animate-spin w-[100vw] h-[200vh] rounded-full bg-opacity-90 bg-[#faf5ef]"></div> */}
        <div className="animate-spin-slow">
          <Image className="w-[100vw]" src={plate} alt="plate" />
          <Image
            className="rotate-6 absolute top-[1.5%] left-[50%] mx-auto pt-10 w-[120px]"
            src={logo_blue}
            alt="logo"
          />
        </div>
        <Image className="animate-spin-slow w-[600px] absolute top-[20%] left-[30%]" src={bottom} alt="bottom" />
        <Image className="animate-spin-slow w-[600px] absolute top-[20%] left-[30%]" src={lettuce} alt="lettuce" />
        {/* <Image className="animate-spin-slow w-[600px] absolute top-[20%] left-[30%]" src={patty} alt="patty" /> */}

        <div
          className="fixed top-[10%] left-[-20px]"
          onMouseOver={() => setHover(true)}
          onMouseOut={() => setHover(false)}>
          <Image className="load w-[300px]" src={hover ? left_hover : left} alt="왼쪽" />
        </div>

        <Link href="/signin">
          <div
            className="fixed top-[10%] right-6"
            onMouseOver={() => setoutHover(true)}
            onMouseOut={() => setoutHover(false)}>
            <Image className="load w-[180px]" src={outHover ? signin_hover : signin} alt="signin" />
          </div>
        </Link>
        <Link href="/signup">
          <div
            className="fixed top-[19%] right-6"
            onMouseOver={() => setinHover(true)}
            onMouseOut={() => setinHover(false)}>
            <Image className="load w-[180px]" src={inHover ? signup_hover : signup} alt="signup" />
          </div>
        </Link>
      </div>
    </div>
  );
}
