"use client";

import React, { useMemo, useEffect, useState } from "react";
import { TreeSelect, Select, Button, message, Steps, theme } from "antd";
import Input from "../ui/Input";
import type { SelectProps, RadioChangeEvent } from "antd";
import { useAtom } from "jotai";
import { isSoloAtom } from "../../store/isSolo";
import { useCreateRoom } from "@/hooks/useCreateRoom";
// import { useMutation } from "react-query";
import Loading from "@/app/loading";
import { useRouter } from "next/navigation";
import { userAtom } from "@/store/authAtom";
import { stompClient } from "../editor/store";

const handleChange = (value: string | string[]) => {
  console.log(`Selected: ${value}`);
};

export default function QuizMaker() {
  const { token } = theme.useToken();
  const [current, setCurrent] = useState(0);
  const [value, setValue] = useState(["0-0-0"]);
  const { SHOW_PARENT } = TreeSelect;
  const [isSolo] = useAtom(isSoloAtom);
  const { createRoomMutation, inviteUserInfo, inviteUserMutation, roomInfo, roomId } = useCreateRoom();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const handleTitleChange = (e: any) => setTitle(e.target.value);
  const handleContentChange = (e: any) => setContent(e.target.value);
  const [user] = useAtom(userAtom);

  const router = useRouter();

  useEffect(() => {}, [isSolo]);

  // 퀴즈 방 만들기 STEP 1
  const handleCreateRoom = () => {
    createRoomMutation.mutate({
      roomName: title,
      pages: [1, 2, 3],
      sharePages: [1, 2],
      quizCnt: 2,
      content: content,
      single: false,
    });
  };

  const userOptions = useMemo(() => {
    if (inviteUserInfo) {
      return inviteUserInfo.map((user) => ({ value: user.userName, label: user.userName }));
    } else {
      return [];
    }
  }, [inviteUserInfo]);

  // 퀴즈 방 만들기 STEP 2
  const users = [
    { userPk: user.userPk, userName: user.nickName, userImg: user.profileImage },
    { userPk: 1, userName: "csi", userImg: "성공" },
    { userPk: 5, userName: "isc", userImg: "성공" },
  ];

  const handleSumbitCreateRoom = () => {
    if (roomInfo) {
      // const { inviteUsers, ...rest } = roomInfo;
      inviteUserMutation.mutate({ ...roomInfo, users: users });
    }
  };

  useEffect(() => {
    if (inviteUserMutation.isSuccess) {
      message.success("방을 만들었어요! 잠시 후 이동합니다.");
      // router.push(`/quiz/room/${roomId}`);
      setTimeout(() => router.push(`/quiz/room/${roomId}`), 1000);
    } else if (inviteUserMutation.isError) {
      message.error("에러가 발생했어요. 잠시 후 다시 만들어주세요.");
    }
  }, [roomId]);

  const treeData = [
    {
      title: "MY CS BOOK",
      value: "0-0",
      key: "0-0",
      children: [
        {
          title: "네트워크",
          value: "0-0-0",
          key: "0-0-0",
        },
      ],
    },
    {
      title: "1주차",
      value: "0-1",
      key: "0-1",
      children: [
        {
          title: "네트워크",
          value: "0-1-0",
          key: "0-1-0",
        },
        {
          title: "운영체제",
          value: "0-1-1",
          key: "0-1-1",
        },
        {
          title: "자료구조",
          value: "0-1-2",
          key: "0-1-2",
        },
      ],
    },
  ];

  const tProps = {
    treeData,
    treeCheckable: true,
    showCheckedStrategy: SHOW_PARENT,
    placeholder: "페이지를 선택해주세요",
    style: {
      width: "300px",
      marginTop: "10px",
    },
  };

  const steps = useMemo(
    () => [
      {
        title: <h1 className="dark:text-font_primary">퀴즈 범위</h1>,
        content: (
          <div>
            <h1
              className="dark:text-font_primary text-dark_primary text-2xl font-preBd"
              style={{ marginTop: "30px", marginBottom: "10px", padding: 0 }}>
              퀴즈 범위
            </h1>
            <h1 className="dark:text-font_primary" style={{ marginBottom: "60px", padding: 0 }}>
              내 노트, 공유받은 페이지에서 퀴즈로 풀 페이지를 골라주세요.
            </h1>
            <TreeSelect {...tProps} />
          </div>
        ),
      },
      {
        title: <h1 className="dark:text-font_primary">문제 개수</h1>,
        content: (
          <div>
            <h1
              className="dark:text-font_primary text-dark_primary text-2xl font-preBd"
              style={{ marginTop: "30px", marginBottom: "10px", padding: 0 }}>
              문제 개수
            </h1>
            <h1 className="dark:text-font_primary" style={{ marginBottom: "60px", padding: 0 }}>
              페이지 당 생성 가능한 문제 수는 최소 1개, 최대 3개입니다.
            </h1>
            <Select
              defaultValue={{ value: "1", label: "1개" }}
              style={{ width: 120, marginTop: "10px" }}
              options={[
                { value: 1, label: "1개" },
                { value: 2, label: "2개" },
                { value: 3, label: "3개" },
              ]}
            />
          </div>
        ),
      },
      ...(isSolo
        ? []
        : [
            {
              title: <h1 className="dark:text-font_primary">방 정보</h1>,
              content: (
                <div>
                  <h1
                    className="dark:text-font_primary text-dark_primary text-2xl font-preBd"
                    style={{ marginTop: "30px", marginBottom: "10px", padding: 0 }}>
                    방 정보
                  </h1>
                  <h1 className="dark:text-font_primary" style={{ marginBottom: "60px", padding: 0 }}>
                    방 제목과 방 내용을 작성해주세요.
                  </h1>
                  <Input type="text" text="Title" onChange={handleTitleChange} placeholder="방 제목을 입력해주세요." />
                  <br />
                  <Input
                    type="text"
                    text="Title"
                    onChange={handleContentChange}
                    placeholder="방 내용을 입력해주세요."
                  />
                </div>
              ),
            },
            {
              title: <h1 className="dark:text-font_primary">친구 초대</h1>,
              content: (
                <div>
                  <h1
                    className="dark:text-font_primary text-dark_primary text-2xl font-preBd"
                    style={{ marginTop: "30px", marginBottom: "10px", padding: 0 }}>
                    친구 초대
                  </h1>
                  <h1 className="dark:text-font_primary" style={{ marginBottom: "60px", padding: 0 }}>
                    닉네임 검색을 통해 친구를 초대해보세요.
                  </h1>
                  <Select
                    // defaultValue={["가영"]}
                    mode="tags"
                    size="middle"
                    placeholder="닉네임을 검색하세요"
                    onChange={handleChange}
                    style={{ width: "400px" }}
                    options={userOptions}
                  />
                </div>
              ),
            },
          ]),
    ],
    [userOptions]
  );

  const next = () => {
    setCurrent(current + 1);

    if (current === 2) {
      handleCreateRoom();
    }
  };

  const prev = () => {
    setCurrent(current - 1);
  };

  const items = steps.map((item) => ({ key: item.title, title: item.title }));

  const contentStyle: React.CSSProperties = {
    height: "350px",
    width: "900px",
    textAlign: "center",
    display: "flex",
    alignItems: "start",
    justifyContent: "center",
    color: token.colorTextTertiary,
    backgroundColor: token.colorFillAlter,
    borderRadius: token.borderRadiusLG,
    border: `1px dashed ${token.colorBorder}`,
    marginTop: 16,
    fontFamily: "preRg",
  };

  return (
    <div className="items-center mx-auto">
      <h1 className="text-2xl font-bold text-center">퀴즈를 만들어봅시다</h1>
      <br />
      <Steps current={current} items={items} />
      <div style={contentStyle}>{steps[current].content}</div>
      <div style={{ display: "flex", justifyContent: "flex-end", fontFamily: "preRg", marginTop: 24 }}>
        {/* 첫번째 관문 로딩 페이지 */}
        {createRoomMutation.isLoading ? (
          <Loading /> // 로딩 컴포넌트를 렌더링
        ) : (
          <div>
            {current > 0 && (
              <Button style={{ fontFamily: "preRg", backgroundColor: "white", margin: "0 8px" }} onClick={() => prev()}>
                이전
              </Button>
            )}
            {current < steps.length - 1 && (
              <Button
                className="dark:border dark:border-font_primary font-preRg bg-primary"
                type="primary"
                onClick={() => next()}>
                다음
              </Button>
            )}
            {current === steps.length - 1 && (
              <Button
                className="dark:border dark:border-font_primary font-preRg bg-primary"
                type="primary"
                onClick={() => handleSumbitCreateRoom()}>
                생성하기
              </Button>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
