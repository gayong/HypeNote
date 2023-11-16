"use client";

import React, { useEffect, useState } from "react";
import type { CountdownProps } from "antd";
import { Statistic, message } from "antd";

interface TimerProps {
  time: number;
  setSubmit: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function Timer({ time, setSubmit }: TimerProps) {
  const { Countdown } = Statistic;

  // const deadline = Date.now() + 30 * time * 1000;
  const [deadline, setDeadline] = useState(Date.now() + 30 * time * 1000);

  useEffect(() => {
    // time prop이 변경되어도 deadline은 변경되지 않습니다.
    // 초기 마운트될 때만 deadline을 설정합니다.
    setDeadline(Date.now() + 30 * time * 1000);
  }, []);
  const onFinish: CountdownProps["onFinish"] = () => {
    console.log("finished!");
    message.info("퀴즈가 끝났습니다!");
    setSubmit(true);
  };

  return (
    <div>
      <Countdown
        title={<div className="text-[#cf1322] ml-8 -mb-3">남은 시간</div>}
        value={deadline}
        onFinish={onFinish}
        className="font-preBd absolute top-5 right-10"
      />
    </div>
  );
}
