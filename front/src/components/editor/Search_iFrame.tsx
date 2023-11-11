// "use client";

// import axios from "axios";
// import Link from "next/link";
// import Image from "next/image";
// import React, { useEffect, useState } from "react";
// import { Button, Drawer, Input } from "antd";
// import { useAtom } from "jotai";
// import { isSearchOpen } from "../../store/searchOpen";
// import { useGetSearchResult } from "@/hooks/useGetSearchResult";
// import { SearchType } from "@/types/ediotr";
// import type { CollapseProps } from "antd";
// import { Collapse } from "antd";
// import "../../app/search/search.css";

// // 이건 서랍 속 검색!!!!!!
// export default function Search() {
//   const [open, setOpen] = useAtom(isSearchOpen);
//   const { Search } = Input;
//   const [keyword, setKeyword] = useState("");
//   const [enabled, setEnabled] = useState(false);
//   const { data: response, isLoading, refetch } = useGetSearchResult(keyword, enabled);
//   const [results, setResults] = useState<Array<SearchType>>([]);
//   const [iframeSrc, setIframeSrc] = useState("");
//   const [showIframe, setShowIframe] = useState(false);

//   const showDrawer = () => {
//     setOpen(true);
//   };

//   const onClose = () => {
//     setOpen(false);
//   };

//   const handleEnter = () => {
//     console.log("value", keyword);
//     setEnabled(true);
//   };

//   useEffect(() => {
//     if (enabled) {
//       refetch();
//     }
//   }, [enabled]);

//   const onChange = (e: any) => {
//     setKeyword(e.target.value);
//     console.log(e.target.value);
//   };

//   useEffect(() => {
//     if (response) {
//       console.log("검색결과다", response.data.data.items);
//       setResults(response.data.data.items);
//     }
//   }, [response]);

//   const items: CollapseProps["items"] = [
//     {
//       key: "1",
//       label: (
//         <div className="flex justify-start items-end">
//           <h1 className="text-[15px] mr-2">웹 검색</h1>
//           <p className="text-[13px] text-line_primary opacity-50">CS와 연관성이 높은 검색 결과를 알려줘요.</p>
//         </div>
//       ),
//       children: (
//         <div className="scrollbar-hide">
//           <Search
//             placeholder="검색어를 입력해주세요"
//             value={keyword}
//             enterButton
//             onPressEnter={handleEnter}
//             onChange={onChange}
//             onSearch={handleEnter}
//             style={{ width: "100%", display: "flex", margin: "auto", marginBottom: "7px" }}
//           />
//           {results &&
//             results.map((item, index) => (
//               <div key={index} className="max-w-full scrollbar-hide">
//                 <div
//                   onClick={() => {
//                     setIframeSrc(item.link);
//                     setShowIframe(true);
//                   }}>
//                   <div className="flex justify-start items-center">
//                     <img
//                       src={item.pagemap.cse_thumbnail[0].src}
//                       alt="썸네일"
//                       className="w-16 min-w-16 h-16 mr-2 mb-2 rounded-md"
//                     />
//                     <div className="pb-2">
//                       <p className="font-preBd mb-0 p-0 oneline">{item.title}</p>
//                       <p className="search_content text-line_primary hover:text-dark_font opacity-70">{item.snippet}</p>
//                     </div>
//                   </div>
//                 </div>
//                 <hr className="p-1 opacity-20" />
//               </div>
//             ))}
//           {showIframe && (
//             <div>
//               <button className="absolute top-0 z-50 text-2xl bg-hover_primary" onClick={() => setShowIframe(false)}>
//                 사라져!!!
//               </button>
//               <iframe className="absolute top-20 right-0 w-[300px] h-[600px]" src={iframeSrc}></iframe>
//             </div>
//           )}
//         </div>
//       ),
//     },
//     {
//       key: "2",
//       label: (
//         <div className="flex justify-start items-end">
//           <h1 className="text-[15px] mr-2">Chat-GPT</h1>
//           <p className="text-[13px] text-line_primary opacity-50">프롬프트를 이용한 대답을 들려줘요.</p>
//         </div>
//       ),
//       children: <h1 className="text-center">Chat-GPT를 기다려요..</h1>,
//     },
//   ];

//   return (
//     <div>
//       <Button
//         className="bg-primary absolute top-8 -right-9 rounded-b-none rotate-[270deg]"
//         type="primary"
//         onClick={showDrawer}>
//         검색 & GPT
//       </Button>
//       <Drawer
//         title="한입 도우미"
//         placement="right"
//         onClose={onClose}
//         open={open}
//         zIndex={0}
//         mask={false}
//         maskClosable={false}
//         width={380}
//         className="relative">
//         <Collapse defaultActiveKey={[""]} ghost items={items} className="scrollbar-hide" />
//       </Drawer>
//     </div>
//   );
// }
