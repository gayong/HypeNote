"use client";

import useLinkNote from "@/hooks/useLinkNote";
import style from "./Category.module.css";
import useCreateNote from "@/hooks/useCreateNote";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { HiChevronDown, HiChevronRight } from "react-icons/hi";
import useCreateChildNote from "@/hooks/useCreateChildNote";
import { useAtom } from "jotai";
// import { userAtom } from "@/store/authAtom";
import { DocumentsType } from "@/types/ediotr";
import useGetUserInfo from "@/hooks/useGetUserInfo";

// type childProps = { id: string; title: string; parentId: string; children: childProps[] };

interface categoryProps {
  childProps: DocumentsType;
  value: number;
  depth: number;
}

export default function Category({ childProps, value, depth }: categoryProps) {
  const { createChildDocument } = useCreateChildNote();
  // const [user] = useAtom(userAtom);
  const { data: user, isLoading, isError, error } = useGetUserInfo();

  const { LinkNote } = useLinkNote();
  const router = useRouter();
  const [collapsed, setCollapsed] = useState(true);
  const [rootCollapsed, setRootCollapsed] = useState(false);
  const icon = collapsed ? <HiChevronRight /> : <HiChevronDown />;
  const Rooticon = rootCollapsed ? <HiChevronRight /> : <HiChevronDown />;
  useEffect(() => {
    if (childProps.parentId === "root") {
      setRootCollapsed(true);
    }
  }, [childProps.parentId]);

  function toggleCollapse(event: React.MouseEvent) {
    event.stopPropagation();

    setCollapsed((prevValue) => !prevValue);
  }
  function toggleRootCollapse(event: React.MouseEvent) {
    event.stopPropagation();

    setRootCollapsed((prevValue) => !prevValue);
  }
  const onClickHandler = async (event: React.MouseEvent, id: DocumentsType) => {
    event.stopPropagation();
    console.log(id.id, "여기는 + 핸들러");

    if (!user) {
      return;
    }
    // const userId = user.userPk;

    try {
      const documentId = await createChildDocument(user.userPk, id.id);
      console.log(documentId);
      router.push(`/editor/${documentId}`);
    } catch (error) {
      console.log(error);
    }
  };

  const onClickPage = (event: React.MouseEvent, pageId: string) => {
    router.push(`/editor/${pageId}`);
    event.stopPropagation();
    console.log(pageId, "여기는 + 페이지 이동 핸들러");
  };

  return (
    <>
      {depth === 0 && (
        <div
          className="no-drag p-2.5 flex items-center rounded-md px-4 duration-300 cursor-pointer hover:bg-hover_primary hover:bg-opacity-50 dark:hover:bg-line_primary dark:hover:bg-opacity-50"
          // onclick="dropdown()"
          onClick={(event) => onClickPage(event, childProps.id)}>
          <i className="bi bi-chat-left-text-fill"></i>
          <div className="no-drag group flex justify-between w-full items-center">
            <div className="no-drag text-[15px] ml-2 text-white font-bold flex items-center truncate ...">
              {childProps.title}
              <div className="no-drag ml-3" style={{ fontSize: "20px" }} onClick={(event) => toggleCollapse(event)}>
                {childProps.children && Rooticon}
              </div>
            </div>

            {value === 1 && (
              <h1
                className="no-drag pb-[3px] m-0 text-right invisible group-hover:visible text-2xl leading-3"
                onClick={(event) => onClickHandler(event, childProps)}>
                +
              </h1>
            )}
          </div>
        </div>
      )}

      {depth !== 0 && (
        <div
          key={childProps.id}
          className="no-drag text-[14px] mx-6 text-white"
          id="submenu"
          onClick={(event) => onClickPage(event, childProps.id)}>
          {/* 책 카테고리 */}
          <div className="no-drag group flex justify-between items-center cursor-pointer p-2 hover:bg-hover_primary hover:bg-opacity-50 dark:hover:bg-line_primary dark:hover:bg-opacity-50 rounded-md">
            <h1 className="no-drag text-left flex items-center truncate ...">
              {childProps.title}
              <div className="no-drag ml-3" style={{ fontSize: "20px" }} onClick={(event) => toggleCollapse(event)}>
                {childProps.children && icon}
              </div>
            </h1>
            {value === 1 && (
              <h1
                className="no-drag pb-[3px] m-0 text-right invisible group-hover:visible text-2xl leading-3"
                onClick={(event) => onClickHandler(event, childProps)}>
                +
              </h1>
            )}
          </div>
        </div>
      )}
      {childProps.children &&
        childProps.children.map((subject) => (
          <div key={subject.id} className="no-drag text-[14px] mx-6 text-white" id="submenu">
            <div style={{ maxHeight: !collapsed ? "100%" : "0", overflow: "hidden" }}>
              <Category childProps={subject} value={value} key={subject.id} depth={depth + 1} />
            </div>
          </div>
        ))}
    </>
  );
}
