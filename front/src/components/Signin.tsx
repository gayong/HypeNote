"use client";
import { FcGoogle } from "react-icons/fc";
import Link from "next/link";
import Image from "next/image";

import Button from "./ui/Button";
import Input from "./ui/Input";
import Label from "./ui/Label";

import logoImg from "../../public/assets/logo_blue.png";
import darkLogoImg from "../../public/assets/logo.png";
import krLogoImg from "../../public/assets/krlogo_blue.png";
import darkKrLogoImg from "../../public/assets/krlogo.png";
import { useState } from "react";
import useSignin from "@/hooks/useSiginin";

export default function Signin() {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const handleEmailChange = (e: any) => setEmail(e.target.value);
  const handlePasswordChange = (e: any) => setPassword(e.target.value);
  const { signin } = useSignin();

  const handleSignin = async () => {
    console.log("로그인하마");
    const success = await signin(email, password);

    // if (success === "success") {
    //   // 회원가입 성공 처리
    //   console.log("회원가입 성공!");
    // } else {
    //   // 회원가입 실패 처리
    //   console.log("회원가입 실패..");
    // }
  };
  return (
    <>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <Image className="mx-auto h-[20vh] w-auto dark:hidden" src={logoImg} alt="일반모드 로고" priority />
          <Image
            className="mx-auto h-[20vh] w-auto dark:block hidden"
            src={darkLogoImg}
            alt="다크모드일때 로고"
            priority
          />
          <h2 className="mt-3 text-center text-2xl leading-9 tracking-tight text-gray-700">
            로그인 후{" "}
            <Image className="inline mb-2 h-[1.6rem] w-auto dark:hidden" priority src={krLogoImg} alt="한글로고" />
            <Image
              className="mb-2 h-[1.6rem] w-auto dark:inline-block hidden"
              priority
              src={darkKrLogoImg}
              alt="다크모드일때 한글로고"
            />
            를 즐겨보세요 ✏️
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <div className="space-y-6">
            <div>
              <Label text="이메일" />
              <div className="mt-2">
                <Input text="Email" type="email" onChange={handleEmailChange} autoComplete="email" />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <Label text="비밀번호" />
              </div>
              <div className="mt-2">
                <Input text="Password" type="password" onChange={handlePasswordChange} autoComplete="new-password" />
              </div>
            </div>

            <div>
              <Button text="로그인" onClick={handleSignin} wFull={true}></Button>
            </div>
          </div>

          <div className="relative my-3">
            <div className="relative flex items-center">
              <div className="my-4 bg-line_primary h-[1px] w-full "></div>
            </div>
            <div className="absolute flex justify-center w-full top-1/2 transform -translate-y-1/2">
              <span className="bg-secondary dark:bg-dark_background px-2">또는</span>
            </div>
          </div>

          <Button
            text="구글로 로그인"
            iconImg={<FcGoogle className="text-xl mt-0.5 mr-1" />}
            onClick={handleSignin}
            wFull={true}
          />

          <p className="mt-10 text-center text-sm text-gray-500">
            아직 회원이 아니신가요?{" "}
            <Link href="/signup" className="font-semibold leading-6 text-hover_primary hover:text-font_secondary">
              회원가입
            </Link>
          </p>
        </div>
      </div>
    </>
  );
}
