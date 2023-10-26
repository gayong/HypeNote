'use client';
import LogoImg from '../../public/assets/logo.png';
import Image from 'next/image';
import Link from 'next/link';
import { usePathname } from 'next/navigation';

export default function Navbar() {
  const pathname = usePathname();
  if (pathname === '/login') {
    return null;
  }

  return (
    <>
      {/* navbar */}
      <span
        className="absolute text-white text-4xl top-5 left-4 cursor-pointer"
        // onclick="openSidebar()"
      >
        <i className="bi bi-filter-left px-2 bg-gray-900 rounded-md"></i>
      </span>
      <div className="sidebar fixed top-0 bottom-0 lg:left-0 p-2 w-[290px] overflow-y-auto text-center bg-[#2946A2]">
        <div className="text-gray-100 text-xl">
          <div className="p-2.5 mt-1 flex items-center justify-items-start">
            <Link href="/">
              <Image src={LogoImg} alt="우리로고" className="h-16 w-auto"></Image>
            </Link>
            {/* <h1 className="text-gray-200 text-[17px] ml-3">가영님, 안녕하세요</h1> */}
            <i
              className="bi bi-x cursor-pointer ml-28 lg:hidden"
              // onclick="openSidebar()"
            ></i>
          </div>
          <h1 className="text-start text-gray-200 text-[17px] ml-3">가영님, 안녕하세요</h1>
          {/* <div className="my-2 bg-gray-600 h-[1px]"></div> */}
          <br />
        </div>
        <div className="w-[95%] mx-auto p-1.5 flex items-center rounded-lg duration-300 bg-[#FFFFFF] bg-opacity-75 text-black">
          <i className="bi bi-search text-sm"></i>
          <input
            type="text"
            placeholder="🔎 게시글을 검색하세요"
            className="text-[15px] ml-1 bg-transparent focus:outline-none placeholder-gray-600"
          />
        </div>
        <div className="py-2.5 mt-1 mb-3 flex items-center rounded-md px-4 duration-300 cursor-pointer hover:bg-blue-700 text-white">
          <i className="bi bi-house-door-fill"></i>
          <span className="text-[15px] ml-2 font-bold">나의 뇌</span>
        </div>

        {/* <div className="my-4 bg-gray-500 h-[1px]"></div> */}

        <div className="inline-flex items-center justify-center w-full">
          <hr className="w-full h-px my-1 bg-gray-500 border-0"></hr>
          <span className="text-[13px] absolute px-3 text-gray-300 -translate-x-1/2 bg-[#2946A2] left-1/2">
            내 노트
          </span>
        </div>

        <div
          className="p-2.5 mt-3 flex items-center rounded-md px-4 duration-300 cursor-pointer hover:bg-blue-700 text-white"
          // onclick="dropdown()"
        >
          <i className="bi bi-chat-left-text-fill"></i>
          <div className="flex justify-between w-full items-center">
            {/* 하나의 책 단위 */}
            <span className="text-[15px] ml-2 text-white font-bold">MY CS BOOK</span>
            <span className="text-sm rotate-180" id="arrow">
              <i className="bi bi-chevron-down"></i>
            </span>
          </div>
        </div>
        <div className="text-left text-[14px] mx-6 text-white" id="submenu">
          {/* 책 카테고리 */}
          <h1 className="cursor-pointer p-2 hover:bg-blue-700 rounded-md">운영체제</h1>
          <h1 className="cursor-pointer p-2 hover:bg-blue-700 rounded-md">네트워크</h1>
          <h1 className="cursor-pointer p-2 hover:bg-blue-700 rounded-md">자료구조</h1>
        </div>

        <br />
        <br />
        <div className="inline-flex items-center justify-center w-full">
          <hr className="w-full h-px my-1 bg-gray-500 border-0"></hr>
          <span className="text-[13px] absolute px-3 text-gray-300 -translate-x-1/2 bg-[#2946A2] left-1/2">
            공유받은 페이지
          </span>
        </div>

        <div
          className="p-2.5 mt-3 flex items-center rounded-md px-4 duration-300 cursor-pointer hover:bg-blue-700 text-white"
          // onclick="dropdown()"
        >
          <i className="bi bi-chat-left-text-fill"></i>
          <div className="flex justify-between w-full items-center">
            {/* 하나의 책 단위 */}
            <span className="text-[15px] ml-2 text-white font-bold">1주차 스터디</span>
            <span className="text-sm rotate-180" id="arrow">
              <i className="bi bi-chevron-down"></i>
            </span>
          </div>
        </div>
        <div className="text-left text-[14px] mx-6 text-white" id="submenu">
          {/* 책 카테고리 */}
          <h1 className="cursor-pointer p-2 hover:bg-blue-700 rounded-md">운영체제</h1>
          <h1 className="cursor-pointer p-2 hover:bg-blue-700 rounded-md">네트워크</h1>
          <h1 className="cursor-pointer p-2 hover:bg-blue-700 rounded-md">자료구조</h1>
        </div>
      </div>
    </>
  );
}
