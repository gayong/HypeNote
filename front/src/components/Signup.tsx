"use client";
import Image from "next/image";

import Button from "./ui/Button";
import Input from "./ui/Input";
import Label from "./ui/Label";

import { message } from "antd";

import logoImg from "../../public/assets/logo_blue.png";
import darkLogoImg from "../../public/assets/logo.png";
import krLogoImg from "../../public/assets/krlogo_blue.png";
import darkKrLogoImg from "../../public/assets/krlogo.png";
import { useRef, useState } from "react";
import useSignup from "@/hooks/useSignup";

export default function Signup() {
  // 각 입력 필드에 대한 State들을 생성합니다.
  const [nickName, setNickName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [password2, setPassword2] = useState<string>("");
  const [profileImage, setProfileImage] = useState("");

  // 입력 필드가 변경될 때 마다 해당 State를 업데이트합니다.
  const handleNickNameChange = (e: any) => setNickName(e.target.value);
  const handleEmailChange = (e: any) => setEmail(e.target.value);
  const handlePasswordChange = (e: any) => setPassword(e.target.value);
  const handlePassword2Change = (e: any) => setPassword2(e.target.value);
  const [file, setFile] = useState(null);

  const { signup } = useSignup();
  const imgRef = useRef<HTMLInputElement>(null);
  const handleSignup = async () => {
    console.log("회원가입 실행");
    // 비밀번호와 비밀번호 확인이 일치하는지 검사합니다.
    if (password !== password2) {
      message.error("비밀번호가 일치하지 않습니다.");
      return;
    }

    const success = await signup(email, password, nickName, profileImage);

    if (success === "success") {
      // 회원가입 성공 처리
      console.log("회원가입 성공!");
    } else {
      // 회원가입 실패 처리
      console.log("회원가입 실패..");
    }
    alert("회원가입실행");
  };
  const handleImageChange = (e: any) => {
    setFile(e.target.files[0]);
    const reader = new FileReader();
    reader.onloadend = () => {
      if (reader.result) {
        setProfileImage(reader.result as string);
      }
    };
    reader.readAsDataURL(e.target.files[0]);
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
            회원가입 후{" "}
            <Image
              className="inline mb-2 h-[1.6rem] w-auto dark:hidden"
              priority
              src={krLogoImg}
              alt="일반모드 한글로고"
            />
            <Image
              className="mb-2 h-[1.6rem] w-auto dark:inline-block hidden"
              priority
              src={darkKrLogoImg}
              alt="다크모드일때 한글로고"
            />
            를 즐겨보세요 ✏️
          </h2>
        </div>

        <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-sm">
          <div className="space-y-3">
            <div>
              <Label text="닉네임" />
              <div className="mt-2">
                <Input text="NickName" type="text" onChange={handleNickNameChange} />
              </div>
            </div>
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
              <div className="flex items-center justify-between">
                <Label text="비밀번호 확인" />
              </div>
              <div className="mt-2 ">
                <Input text="Password2" type="password" onChange={handlePassword2Change} autoComplete="new-password" />
              </div>
            </div>

            <div>
              <Label text="프로필 사진" />
              <Image
                src={profileImage ? profileImage : `/assets/유령.png`}
                alt="프로필 이미지"
                width={30}
                height={30}
                // className="rounded"
              />
              <div className="mt-2">
                <label className="block">
                  <input
                    type="file"
                    className="block w-full text-sm text-slate-500
            file:mr-4 file:py-1 file:px-3
            file:rounded-md file:border-0
            file:text-xs
            file:bg-primary file:text-font_primary
            hover:file:bg-hover_primary
            "
                    accept="image/*"
                    ref={imgRef}
                    onChange={handleImageChange}
                  />
                </label>
              </div>
            </div>

            <div className="pt-5">
              <Button text="회원가입" onClick={handleSignup} wFull={true}></Button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
