import LogoImg from '../../public/assets/logo.png';
import Image from 'next/image';

export default function Navbar() {
  return (
    <>
      {/* navbar */}
      <span
        className="absolute text-white text-4xl top-5 left-4 cursor-pointer"
        // onclick="openSidebar()"
      >
        <i className="bi bi-filter-left px-2 bg-gray-900 rounded-md"></i>
      </span>
      <div className="sidebar fixed top-0 bottom-0 lg:left-0 p-2 w-[300px] overflow-y-auto text-center bg-[#2946A2]">
        <div className="text-gray-100 text-xl">
          <div className="p-2.5 mt-1 flex items-center justify-items-start">
            <Image src={LogoImg} alt="우리로고" className="h-14 w-auto"></Image>
            <h1 className="font-bold text-gray-200 text-[15px] ml-3">ㅇㅇ님의 한입노트</h1>
            <i
              className="bi bi-x cursor-pointer ml-28 lg:hidden"
              // onclick="openSidebar()"
            ></i>
          </div>
          {/* <div className="my-2 bg-gray-600 h-[1px]"></div> */}
        </div>
        <div className="p-2.5 flex items-center rounded-md px-4 duration-300 cursor-pointer bg-[#FFFFFF] text-black">
          <i className="bi bi-search text-sm"></i>
          <input
            type="text"
            placeholder="Search"
            className="text-[15px] ml-4 w-full bg-transparent focus:outline-none"
          />
        </div>
        <div className="p-2.5 mt-3 flex items-center rounded-md px-4 duration-300 cursor-pointer hover:bg-blue-600 text-white">
          <i className="bi bi-house-door-fill"></i>
          <span className="text-[15px] ml-4 text-gray-200 font-bold">내 뇌</span>
        </div>

        <div className="my-4 bg-gray-600 h-[1px]"></div>

        <div
          className="p-2.5 mt-3 flex items-center rounded-md px-4 duration-300 cursor-pointer hover:bg-blue-600 text-white"
          // onclick="dropdown()"
        >
          <i className="bi bi-chat-left-text-fill"></i>
          <div className="flex justify-between w-full items-center">
            {/* 하나의 책 단위 */}
            <span className="text-[15px] ml-4 text-gray-200 font-bold">MY CS BOOK</span>
            <span className="text-sm rotate-180" id="arrow">
              <i className="bi bi-chevron-down"></i>
            </span>
          </div>
        </div>
        <div className="text-left text-sm mt-2 w-4/5 mx-auto text-gray-200 font-bold" id="submenu">
          {/* 책 카테고리 */}
          <h1 className="cursor-pointer p-2 hover:bg-blue-600 rounded-md mt-1">운영체제</h1>
          <h1 className="cursor-pointer p-2 hover:bg-blue-600 rounded-md mt-1">네트워크</h1>
          <h1 className="cursor-pointer p-2 hover:bg-blue-600 rounded-md mt-1">자료구조</h1>
        </div>
        <div className="my-4 bg-gray-600 h-[1px]"></div>
        <div
          className="p-2.5 mt-3 flex items-center rounded-md px-4 duration-300 cursor-pointer hover:bg-blue-600 text-white"
          // onclick="dropdown()"
        >
          <i className="bi bi-chat-left-text-fill"></i>
          <div className="flex justify-between w-full items-center">
            {/* 하나의 책 단위 */}
            <span className="text-[15px] ml-4 text-gray-200 font-bold">MY CS BOOK</span>
            <span className="text-sm rotate-180" id="arrow">
              <i className="bi bi-chevron-down"></i>
            </span>
          </div>
        </div>
        <div className="text-left text-sm mt-2 w-4/5 mx-auto text-gray-200 font-bold" id="submenu">
          {/* 책 카테고리 */}
          <h1 className="cursor-pointer p-2 hover:bg-blue-600 rounded-md mt-1">운영체제</h1>
          <h1 className="cursor-pointer p-2 hover:bg-blue-600 rounded-md mt-1">네트워크</h1>
          <h1 className="cursor-pointer p-2 hover:bg-blue-600 rounded-md mt-1">자료구조</h1>
        </div>
      </div>
    </>
  );
}
