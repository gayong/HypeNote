import useLinkNote from "@/hooks/useLinkNote";
import style from "./Category.module.css";
import useCreateNote from "@/hooks/useCreateNote";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { HiChevronDown, HiChevronRight } from "react-icons/hi";

type childProps = { Id: string; title: string; parentId: string; children: childProps[] };

interface categoryProps {
  childProps: childProps;
  value: number;
}

export default function Category({ childProps, value }: categoryProps) {
  const { createDocument } = useCreateNote();
  const { LinkNote } = useLinkNote();
  const router = useRouter();
  const [collapsed, setCollapsed] = useState(true);
  const [rootCollapsed, setRootCollapsed] = useState(false);
  const icon = collapsed ? <HiChevronRight /> : <HiChevronDown />;
  const Rooticon = rootCollapsed ? <HiChevronRight /> : <HiChevronDown />;
  useEffect(() => {
    if (childProps.parentId === "") {
      setRootCollapsed(true);
    }
  }, [childProps.parentId]);

  const userId = 1;
  function toggleCollapse(event: React.MouseEvent) {
    event.stopPropagation();

    setCollapsed((prevValue) => !prevValue);
  }
  function toggleRootCollapse(event: React.MouseEvent) {
    event.stopPropagation();

    setRootCollapsed((prevValue) => !prevValue);
  }
  const onClickHandler = async (event: React.MouseEvent, id: string) => {
    event.stopPropagation();
    console.log(id, "여기는 + 핸들러");

    // try {
    //   const documentId = await createDocument(userId);
    //   if (id) {
    //     LinkNote(userId, id, documentId);
    //   }
    //   router.push(`/editor/${documentId}`);
    // } catch (error) {
    //   console.log(error);
    // }
  };

  const onClickPage = (event: React.MouseEvent, pageId: string) => {
    // router.push(`/editor/${pageId}`);
    event.stopPropagation();
    console.log(pageId, "여기는 + 페이지 이동 핸들러");
  };

  return (
    <>
      {childProps.parentId === "" && (
        <div
          className="p-2.5 mt-3 flex items-center rounded-md px-4 duration-300 cursor-pointer hover:bg-hover_primary hover:bg-opacity-50 dark:hover:bg-line_primary dark:hover:bg-opacity-50"
          // onclick="dropdown()"
          onClick={(event) => onClickPage(event, childProps.Id)}>
          <i className="bi bi-chat-left-text-fill"></i>
          <div className="group flex justify-between w-full items-center">
            <div className="text-[15px] ml-2 text-white font-bold flex items-center">
              {childProps.title}
              <div className="ml-3" style={{ fontSize: "20px" }} onClick={(event) => toggleRootCollapse(event)}>
                {childProps.children.length > 0 && Rooticon}
              </div>
            </div>

            {value === 1 && (
              <h1
                className="pb-[3px] m-0 text-right invisible group-hover:visible text-2xl leading-3"
                onClick={(event) => onClickHandler(event, childProps.Id)}>
                +
              </h1>
            )}
          </div>
        </div>
      )}
      <div style={{ maxHeight: !rootCollapsed ? "100%" : "0", overflow: "hidden" }}>
        {childProps.children.length > 0 &&
          childProps.children.map((subject) => (
            <div
              key={subject.Id}
              className="text-[14px] mx-6 text-white"
              id="submenu"
              onClick={(event) => onClickPage(event, subject.Id)}>
              {/* 책 카테고리 */}
              <div className="group flex justify-between items-center cursor-pointer p-2 hover:bg-hover_primary hover:bg-opacity-50 dark:hover:bg-line_primary dark:hover:bg-opacity-50 rounded-md">
                <h1 className="text-left flex items-center">
                  {subject.title}
                  <div className="ml-3" style={{ fontSize: "20px" }} onClick={(event) => toggleCollapse(event)}>
                    {subject.children.length > 0 && icon}
                  </div>
                </h1>
                {value === 1 && (
                  <h1
                    className="pb-[3px] m-0 text-right invisible group-hover:visible text-2xl leading-3"
                    onClick={(event) => onClickHandler(event, subject.Id)}>
                    +
                  </h1>
                )}
              </div>
              <div style={{ maxHeight: !collapsed ? "100%" : "0", overflow: "hidden" }}>
                <Category childProps={subject} value={value} key={subject.Id} />
              </div>
            </div>
          ))}
      </div>
    </>
  );
}
