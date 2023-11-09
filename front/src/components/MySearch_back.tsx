// "use client";

// import axios from "axios";
// import Link from "next/link";
// import React, { useEffect, useState } from "react";
// import { Button, Drawer, Input } from "antd";
// import { useGetSearchMyNote } from "@/hooks/useGetSearchMyNote";

// export default function MySearch() {
//   const { Search } = Input;
//   const [keyword, setKeyword] = useState("");
//   const [enabled, setEnabled] = useState(false);
//   const { data: fetchSearchMyNote, isLoading, refetch } = useGetSearchMyNote(keyword);
//   const [results, setResults] = useState([]);

//   const handleEnter = () => {
//     console.log("value", keyword);
//     // api get 요청
//     setEnabled(true);
//     refetch();
//     if (fetchSearchMyNote) {
//       console.log("제대로왓나..");
//     }
//   };

//   const onChange = (e: any) => {
//     setKeyword(e.target.value);
//     console.log(e.target.value);
//   };

//   useEffect(() => {
//     if (fetchSearchMyNote) {
//       console.log("응답", fetchSearchMyNote.data);
//       setResults(fetchSearchMyNote.data.notes);
//     }
//   }, [fetchSearchMyNote]);

//   return (
//     <>
//       <Search
//         placeholder="게시글을 검색하세요"
//         value={keyword}
//         enterButton
//         onPressEnter={handleEnter}
//         onChange={onChange}
//         onSearch={handleEnter}
//         className="flex items-center w-[97%] mx-auto bg-transparent text-[15px] focus:outline-none placeholder-gray-600"
//       />
//     </>
//   );
// }

// // 박은거
// ("use client");

// import axios from "axios";
// import Link from "next/link";
// import React, { useEffect, useState } from "react";
// import { Button, Drawer, Input } from "antd";
// import { useGetSearchMyNote } from "@/hooks/useGetSearchMyNote";

// export default function MySearch() {
//   const { Search } = Input;
//   const [keyword, setKeyword] = useState("");
//   const { data: fetchSearchMyNote, isLoading, refetch } = useGetSearchMyNote(keyword);
//   const [results, setResults] = useState([]);

//   const getResult = async (keyword: string) => {
//     try {
//       const response = await axios.get("https://k9e101.p.ssafy.io/api/editor", {
//         params: { query: keyword },
//       });
//       console.log("오니?", response.data);
//       if (response.data && response.data.notes && response.data.length > 0) {
//         setResults(response.data.items);
//       }
//     } catch (error) {
//       console.error("Error:", error);
//     }
//   };

//   const handleEnter = () => {
//     console.log("value", keyword);
//     // api get 요청
//     getResult(keyword);
//   };

//   const onChange = (e: any) => {
//     setKeyword(e.target.value);
//     console.log(e.target.value);
//   };

//   useEffect(() => {}, []);

//   return (
//     <>
//       <Search
//         placeholder="게시글을 검색하세요"
//         value={keyword}
//         enterButton
//         onPressEnter={handleEnter}
//         onChange={onChange}
//         onSearch={handleEnter}
//         className="flex items-center w-[97%] mx-auto bg-transparent text-[15px] focus:outline-none placeholder-gray-600"
//       />
//     </>
//   );
// }
