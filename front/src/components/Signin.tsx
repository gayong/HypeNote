"use client";
import Link from "next/link";
import Image from "next/image";

import { message } from "antd";

import Button from "./ui/Button";
import Input from "./ui/Input";
import Label from "./ui/Label";

import logoImg from "../../public/assets/logo_blue.png";
import darkLogoImg from "../../public/assets/logo.png";
import krLogoImg from "../../public/assets/krlogo_blue.png";
import darkKrLogoImg from "../../public/assets/krlogo.png";
import { useState } from "react";
import useSignin from "@/hooks/useSiginin";
import { useRouter } from "next/navigation";
import object_bottom from "../../public/assets/object_bottom.png";
import object_top from "../../public/assets/object_top.png";
import dd from "../../public/assets/dd.png";

export default function Signin() {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const handleEmailChange = (e: any) => setEmail(e.target.value);
  const handlePasswordChange = (e: any) => setPassword(e.target.value);
  const { signin } = useSignin();
  const router = useRouter();

  const handleSignin = async (event: any) => {
    event.preventDefault();
    const success = await signin(email, password);

    if (success === "success") {
      message.success("로그인 성공!");
      router.push("/main");
    } else {
      message.error(success);
    }
  };

  return (
    <>
      <div className="flex flex-1 flex-col my-auto">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <Image className="no-drag mx-auto h-[20vh] w-auto dark:hidden" src={logoImg} alt="일반모드 로고" priority />
          <Image
            className="no-drag mx-auto h-[20vh] w-auto dark:block hidden"
            src={darkLogoImg}
            alt="다크모드일때 로고"
            priority
          />
          <h2 className="mt-3 text-center text-2xl leading-9 tracking-tight text-gray-700">
            로그인 후{" "}
            <Image
              className="no-drag inline mb-2 h-[1.6rem] w-auto dark:hidden"
              priority
              src={krLogoImg}
              alt="한글로고"
            />
            <Image
              className="no-drag mb-2 h-[1.6rem] w-auto dark:inline-block hidden"
              priority
              src={darkKrLogoImg}
              alt="다크모드일때 한글로고"
            />
            를 즐겨보세요 ✏️
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm ">
          <form className="space-y-6" onSubmit={handleSignin}>
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
              <button className="w-full flex text-font_primary justify-center rounded-md bg-primary px-3 py-1.5 text-sm font-semibold leading-6 shadow-sm hover:bg-hover_primary focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                로그인
              </button>
            </div>
          </form>

          <p className="mt-10 text-center text-sm text-gray-500">
            아직 회원이 아니신가요?{" "}
            <Link href="/signup" className="font-semibold leading-6 text-hover_primary hover:text-font_secondary">
              회원가입
            </Link>
          </p>
        </div>
      </div>

      <Image
        src={object_bottom}
        className="absolute bottom-0 -left-20 w-[600px] overflow-hidden no-drag"
        alt="bottom"
      />
      <Image src={dd} className="absolute bottom-0 -left-20 w-[600px] z-50 overflow-hidden no-drag " alt="bottom" />
      <Image src={object_top} className="absolute top-0 right-0 w-[550px] select-none no-drag" alt="top" />
    </>
  );
}
