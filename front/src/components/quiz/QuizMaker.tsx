"use client";

import React, { useState } from "react";
import { TreeSelect, Select, Button, message, Steps, theme } from "antd";

export default function QuizMaker() {
  const { SHOW_PARENT } = TreeSelect;

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

  const steps = [
    {
      title: <h1 className="dark:text-font_primary">퀴즈 범위를 선택해주세요</h1>,
      content: (
        <div>
          <h1 style={{ margin: 0, padding: 0 }}>내 노트, 공유받은 페이지 중에서 퀴즈로 풀 페이지를 골라주세요.</h1>
          <TreeSelect {...tProps} />
        </div>
      ),
    },
    {
      title: <h1 className="dark:text-font_primary">문제 개수를 선택해주세요</h1>,
      content: (
        <div>
          <h1 style={{ margin: 0, padding: 0 }}>페이지 당 생성 가능한 문제 수는 최소 1개, 최대 3개입니다.</h1>
          <Select
            defaultValue="1"
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
    {
      title: <h1 className="dark:text-font_primary">같이 풀 친구를 초대하세요</h1>,
      content: "Last-content",
    },
  ];

  const { token } = theme.useToken();
  const [current, setCurrent] = useState(0);
  const [value, setValue] = useState(["0-0-0"]);

  const next = () => {
    setCurrent(current + 1);
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
    alignItems: "center",
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
      <h1 className="text-2xl font-bold">퀴즈를 만들어봅시다</h1>
      <br />
      <Steps current={current} items={items} />
      <div style={contentStyle}>{steps[current].content}</div>
      <div style={{ fontFamily: "preRg", marginTop: 24 }}>
        {current < steps.length - 1 && (
          <Button style={{ fontFamily: "preRg", backgroundColor: "#2946A2" }} type="primary" onClick={() => next()}>
            다음
          </Button>
        )}
        {current === steps.length - 1 && (
          <Button
            style={{ fontFamily: "preRg", backgroundColor: "#2946A2" }}
            type="primary"
            onClick={() => message.success("생성 완료!")}>
            생성하기
          </Button>
        )}
        {current > 0 && (
          <Button style={{ fontFamily: "preRg", backgroundColor: "white", margin: "0 8px" }} onClick={() => prev()}>
            이전
          </Button>
        )}
      </div>
    </div>
  );
}
