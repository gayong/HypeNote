"use client";

import Image from "next/image";
import React, { useEffect, useState, useRef } from "react";
import { Button, Drawer, Input, Collapse, Modal } from "antd";
import { useAtom } from "jotai";
import { isSearchOpen } from "../../store/searchOpen";
import { useGetSearchResult } from "@/hooks/useGetSearchResult";
import { SearchType } from "@/types/ediotr";
import type { CollapseProps } from "antd";
import type { DraggableData, DraggableEvent } from "react-draggable";
import Draggable from "react-draggable";
import "../../app/search/search.css";
import GPT from "./GPT";
import { usePathname } from "next/navigation";

// 이건 서랍 속 검색!!!!!!
export default function Search() {
  const [open, setOpen] = useAtom(isSearchOpen);
  const { Search } = Input;
  const [keyword, setKeyword] = useState("");
  const [enabled, setEnabled] = useState(false);
  const { data: response, isLoading, refetch } = useGetSearchResult(keyword, enabled);
  const [results, setResults] = useState<Array<SearchType>>([]);
  const [iframeSrc, setIframeSrc] = useState("");
  const [modalOpen, setModalOpen] = useState(false);
  const [disabled, setDisabled] = useState(true);
  const [bounds, setBounds] = useState({ left: 0, top: 0, bottom: 0, right: 0 });
  const draggleRef = useRef<HTMLDivElement>(null);
  const pathname = usePathname();

  // 모달 시 스크롤 방지
  useEffect(() => {
    if (pathname === "/") {
      const handleWheel = (e: WheelEvent) => {
        e.preventDefault();
      };
      if (modalOpen) {
        window.addEventListener("wheel", handleWheel, { passive: false });
      } else {
        window.removeEventListener("wheel", handleWheel);
      }
    }
    return () => {
      if (modalOpen) {
        window.removeEventListener("wheel", function (e) {
          e.preventDefault();
        });
      }
    };
  }, [modalOpen]);

  // 검색결과에 날짜 형식 바꾸기
  const formatDate = (datetime: string) => datetime.slice(0, 10).replace(/-/g, ".");

  const showModal = () => {
    setModalOpen(true);
  };

  const handleCancel = (e: React.MouseEvent<HTMLElement>) => {
    setModalOpen(false);
  };

  const onStart = (_event: DraggableEvent, uiData: DraggableData) => {
    const { clientWidth, clientHeight } = window.document.documentElement;
    const targetRect = draggleRef.current?.getBoundingClientRect();
    if (!targetRect) {
      return;
    }
    setBounds({
      left: -targetRect.left + uiData.x,
      right: clientWidth - (targetRect.right - uiData.x) - 10,
      top: -targetRect.top + uiData.y,
      bottom: clientHeight - (targetRect.bottom - uiData.y),
    });
  };

  const showDrawer = () => {
    setOpen(true);
  };

  const onClose = () => {
    setOpen(false);
  };

  const handleEnter = () => {
    setEnabled(true);
  };

  useEffect(() => {
    if (enabled) {
      refetch();
      setEnabled(false);
    }
  }, [enabled]);

  const onChange = (e: any) => {
    setKeyword(e.target.value);
    setEnabled(false);
  };

  useEffect(() => {
    if (response) {
      setResults(response.data.data.documents);
    }
  }, [response]);

  const items: CollapseProps["items"] = [
    {
      key: "1",
      label: (
        <div className="flex justify-start items-end">
          <h1 className="text-[15px] mr-2 font-preBd">웹 검색</h1>
          <p className="text-[13px] text-line_primary opacity-50 font-preRg">
            CS와 연관성이 높은 검색 결과를 알려줘요.
          </p>
        </div>
      ),
      children: (
        <div>
          <Search
            className="font-preRg"
            placeholder="검색어를 입력해주세요"
            value={keyword}
            enterButton
            onPressEnter={handleEnter}
            onChange={onChange}
            onSearch={handleEnter}
            style={{ width: "100%", display: "flex", margin: "auto", marginBottom: "7px" }}
          />
          {!response || results.length > 0 ? (
            results.map((item, index) => (
              <div key={index} className="max-w-full">
                <div
                  className="flex justify-start items-center"
                  onClick={() => {
                    setIframeSrc(item.url);
                    showModal();
                  }}>
                  {item.thumbnail ? (
                    <img src={item.thumbnail} alt="썸네일" className="w-16 min-w-16 h-16 mr-2 mb-2 rounded-md" />
                  ) : null}
                  <div className="pb-2 hover:text-dark_font">
                    <div className="flex justify-start items-end max-w-full ">
                      {item.thumbnail ? (
                        <p className="font-preBd mb-0 p-0 oneline mr-2 oneline max-w-[205px]">{item.title}</p>
                      ) : (
                        <p className="font-preBd mb-0 p-0 oneline mr-2 oneline max-w-[270px]">{item.title}</p>
                      )}
                      <p className="font-preLt mb-0 p-0 text-[10px] text-line_primary">
                        {" "}
                        | {formatDate(item.datetime)}
                      </p>
                    </div>
                    <p className="search_content font-preRg text-line_primary hover:text-dark_font opacity-70">
                      {item.contents}
                    </p>
                  </div>
                </div>
                <hr className="p-1 opacity-20" />
              </div>
            ))
          ) : (
            <div>
              <h1 className="text-center font-preRg">검색결과가 없습니다.</h1>
            </div>
          )}
          <Modal
            title={
              <div
                style={{
                  width: "100%",
                  cursor: "move",
                  marginTop: "-5px",
                  fontFamily: "preLt",
                  fontSize: "15px",
                  color: "gray",
                  opacity: "40%",
                }}
                onMouseOver={() => {
                  if (disabled) {
                    setDisabled(false);
                  }
                }}
                onMouseOut={() => {
                  setDisabled(true);
                }}
                onFocus={() => {}}
                onBlur={() => {}}>
                이 부분을 잡고 위치를 이동하세요.
              </div>
            }
            open={modalOpen}
            // onOk={handleOk}
            onCancel={handleCancel}
            mask={false}
            maskClosable={false}
            zIndex={0}
            footer={null}
            modalRender={(modal) => (
              <Draggable
                disabled={disabled}
                bounds={bounds}
                nodeRef={draggleRef}
                defaultPosition={{ x: 480, y: -100 }}
                onStart={(event, uiData) => onStart(event, uiData)}>
                <div ref={draggleRef}>{modal}</div>
              </Draggable>
            )}>
            <iframe className="w-[490px] h-[620px]" src={iframeSrc}></iframe>
          </Modal>
        </div>
      ),
    },
    {
      key: "2",
      label: (
        <div className="flex justify-start items-end">
          <h1 className="text-[15px] mr-2 font-preBd">Chat-GPT</h1>
          <p className="text-[13px] text-line_primary opacity-50 font-preRg">프롬프트를 이용한 대답을 들려줘요.</p>
        </div>
      ),
      children: <GPT />,
    },
  ];
  return (
    <div>
      <Button
        className=" bg-primary absolute top-8 -right-8 rounded-b-none rotate-[270deg] font-preRg"
        type="primary"
        onClick={showDrawer}>
        한입 도우미
      </Button>
      <Drawer
        title={
          <div className="font-preBd flex">
            한입 도우미
            <Image src="/assets/packman.png" alt="팩맨" width={24} height={20} className="object-cover ml-2" />
          </div>
        }
        placement="right"
        onClose={onClose}
        open={open}
        zIndex={0}
        mask={false}
        maskClosable={false}
        width={380}
        className="relative">
        <Collapse defaultActiveKey={[""]} ghost items={items} />
      </Drawer>
    </div>
  );
}
