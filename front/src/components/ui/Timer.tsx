"use client";

import React from "react";
import type { CountdownProps } from "antd";
import { Statistic } from "antd";

interface TimerProps {
  time: number;
}

export default function Timer({ time }: TimerProps) {
  const { Countdown } = Statistic;

  const deadline = Date.now() + 30 * time * 1000;

  const onFinish: CountdownProps["onFinish"] = () => {
    console.log("finished!");
  };

  return (
    <div>
      <Countdown
        title={<div className="text-[#cf1322]">제한시간</div>}
        value={deadline}
        onFinish={onFinish}
        className="font-preBd"
        valueStyle={{ color: "#cf1322", fontFamily: "PreBd" }}
      />
    </div>
  );
}
