"use client";

import axios from "axios";
import Link from "next/link";
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

// 이건 서랍 속 검색!!!!!!
export default function Search() {
  const [open, setOpen] = useAtom(isSearchOpen);
  const { Search } = Input;
  const [keyword, setKeyword] = useState("");
  const [enabled, setEnabled] = useState(false);
  const { data: response, isLoading, refetch } = useGetSearchResult(keyword, enabled);
  const [results, setResults] = useState<Array<SearchType>>([]);
  const [iframeSrc, setIframeSrc] = useState("");
  const [showIframe, setShowIframe] = useState(false);

  const [modalOpen, setModalOpen] = useState(false);
  const [disabled, setDisabled] = useState(true);
  const [bounds, setBounds] = useState({ left: 0, top: 0, bottom: 0, right: 0 });
  const draggleRef = useRef<HTMLDivElement>(null);

  // 검색결과에 날짜 형식 바꾸기
  const formatDate = (datetime: string) => datetime.slice(0, 10).replace(/-/g, ".");

  const showModal = () => {
    setModalOpen(true);
  };

  const handleOk = (e: React.MouseEvent<HTMLElement>) => {
    console.log(e);
    setModalOpen(false);
  };

  const handleCancel = (e: React.MouseEvent<HTMLElement>) => {
    console.log(e);
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
    console.log("value", keyword);
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
    console.log(e.target.value);
  };

  useEffect(() => {
    if (response) {
      console.log("검색결과다", response.data.data.documents);
      setResults(response.data.data.documents);
    }
  }, [response]);

  const items: CollapseProps["items"] = [
    {
      key: "1",
      label: (
        <div className="flex justify-start items-end">
          <h1 className="text-[15px] mr-2">웹 검색</h1>
          <p className="text-[13px] text-line_primary opacity-50">CS와 연관성이 높은 검색 결과를 알려줘요.</p>
        </div>
      ),
      children: (
        <div className="scrollbar-hide">
          <Search
            placeholder="검색어를 입력해주세요"
            value={keyword}
            enterButton
            onPressEnter={handleEnter}
            onChange={onChange}
            onSearch={handleEnter}
            style={{ width: "100%", display: "flex", margin: "auto", marginBottom: "7px" }}
          />
          {results &&
            results.map((item, index) => (
              <div key={index} className="max-w-full scrollbar-hide">
                <div
                  className="flex justify-start items-center"
                  onClick={() => {
                    setIframeSrc(item.url);
                    showModal();
                  }}>
                  <img src={item.thumbnail} alt="썸네일" className="w-16 min-w-16 h-16 mr-2 mb-2 rounded-md" />
                  <div className="pb-2 hover:text-dark_font">
                    <div className="flex justify-start items-end max-w-full ">
                      <p className="font-preBd mb-0 p-0 oneline mr-2 oneline max-w-[205px]">{item.title}</p>
                      <p className="font-preLt mb-0 p-0 text-[10px] text-line_primary">
                        {" "}
                        | {formatDate(item.datetime)}
                      </p>
                    </div>
                    <p className="search_content text-line_primary hover:text-dark_font opacity-70">{item.contents}</p>
                  </div>
                </div>
                <hr className="p-1 opacity-20" />
              </div>
            ))}
          {/* {showIframe && (
            <div>
              <button className="absolute top-0 z-50 text-2xl bg-hover_primary" onClick={() => setShowIframe(false)}>
                사라져!!!
              </button>
              <iframe className="absolute top-20 right-0 w-[300px] h-[600px]" src={iframeSrc}></iframe>
            </div>
          )} */}
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
          <h1 className="text-[15px] mr-2">Chat-GPT</h1>
          <p className="text-[13px] text-line_primary opacity-50">프롬프트를 이용한 대답을 들려줘요.</p>
        </div>
      ),
      children: <h1 className="text-center">Chat-GPT를 기다려요..</h1>,
    },
  ];

  return (
    <div>
      <Button
        className="bg-primary absolute top-8 -right-9 rounded-b-none rotate-[270deg]"
        type="primary"
        onClick={showDrawer}>
        검색 & GPT
      </Button>
      <Drawer
        title="한입 도우미"
        placement="right"
        onClose={onClose}
        open={open}
        zIndex={0}
        mask={false}
        maskClosable={false}
        width={380}
        className="relative">
        <Collapse defaultActiveKey={[""]} ghost items={items} className="scrollbar-hide" />
      </Drawer>
    </div>
  );
}
