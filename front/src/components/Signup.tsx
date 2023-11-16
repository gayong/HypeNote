"use client";
import Image from "next/image";
import axios from "axios";
import Input from "./ui/Input";
import Label from "./ui/Label";

import { message } from "antd";

import logoImg from "../../public/assets/logo_blue.png";
import darkLogoImg from "../../public/assets/logo.png";
import krLogoImg from "../../public/assets/krlogo_blue.png";
import darkKrLogoImg from "../../public/assets/krlogo.png";

import { useRouter } from "next/navigation";

import { useRef, useState } from "react";
import useSignup from "@/hooks/useSignup";
import object_bottom from "../../public/assets/object_bottom.png";
import object_top from "../../public/assets/object_top.png";
import dd from "../../public/assets/dd.png";

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
  const [file, setFile] = useState<any | null>(null);

  const { signup } = useSignup();
  const imgRef = useRef<HTMLInputElement>(null);
  const router = useRouter();

  const handleSignup = async (event: any) => {
    event.preventDefault();

    if (password !== password2) {
      message.error("비밀번호가 일치하지 않습니다.");
      return;
    }

    if (file === null) {
      message.error("프로필 사진을 등록해주세요.");
      return;
    }

    // axios.post()
    // console.log(file);
    // const success = await signup(email, password, nickName, file);
    const formData = new FormData();
    formData.append("email", email);
    formData.append("password", password);
    formData.append("nickName", nickName);
    formData.append("profileImage", file);

    try {
      const response = await axios.post(process.env.NEXT_PUBLIC_SERVER_URL + "auth/signup", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      if (response.status === 200) {
        message.success("회원가입에 성공했습니다. 로그인 후 사이트 이용해 주세요.");
        router.push("/signin");
      }
    } catch (error: any) {
      console.log(error);
      message.error(error.response.data);
    }
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
      <div className="flex min-h-full flex-1 flex-col justify-center ">
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

        <div className="mt-2 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-1" onSubmit={handleSignup}>
            <div>
              <Label text="닉네임" />
              <div className="mt-1">
                <Input text="NickName" type="text" onChange={handleNickNameChange} />
              </div>
            </div>
            <div>
              <Label text="이메일" />
              <div className="mt-1">
                <Input text="Email" type="email" onChange={handleEmailChange} autoComplete="email" />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <Label text="비밀번호" />
              </div>
              <div className="mt-1">
                <Input text="Password" type="password" onChange={handlePasswordChange} autoComplete="new-password" />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <Label text="비밀번호 확인" />
              </div>
              <div className="mt-1">
                <Input text="Password2" type="password" onChange={handlePassword2Change} autoComplete="new-password" />
              </div>
            </div>

            <div>
              <Label text="프로필 사진" />
              <div className="flex items-center">
                <Image
                  src={profileImage ? profileImage : `/assets/유령.png`}
                  alt="프로필 이미지"
                  width={70}
                  height={70}
                  className="rounded-full object-cover mr-2 h-[50px] w-[50px] mt-1"
                />

                <div className="flex-col justify-between">
                  <span className="text-sm font-medium">프로필 사진 등록은 필수입니다.</span>

                  <label className="block">
                    <input
                      type="file"
                      accept="image/*"
                      ref={imgRef}
                      onChange={handleImageChange}
                      style={{ display: "none" }}
                    />
                    <div
                      className="block w-16 text-center text-[13px] text-slate-500
                    border-[1px] rounded-md bg-primary hover:bg-hover_primary
                    border-transparent text-font_primary pt-[1px]">
                      파일 선택
                    </div>
                  </label>
                </div>
              </div>
            </div>

            <div className="pt-3">
              <button className="w-full flex text-font_primary justify-center rounded-md bg-primary px-3 py-1.5 text-sm font-semibold leading-6 shadow-sm hover:bg-hover_primary focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                회원가입
              </button>
            </div>
          </form>
        </div>
      </div>
      <Image src={object_bottom} className="absolute bottom-0 -left-20 w-[600px]" alt="bottom" />
      <Image src={dd} className="absolute bottom-0 -left-20 w-[600px] z-50 overflow-hidden no-drag " alt="bottom" />
      <Image src={object_top} className="absolute top-0 right-0 w-[550px]" alt="top" />
    </>
  );
}
