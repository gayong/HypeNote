import React from "react";
import loading from "../../public/assets/loading.gif";
import Image from "next/image";

export default function Loading() {
  return (
    <div className="flex mx-auto justify-center items-center">
      <Image src={loading} alt="로딩" priority />
    </div>
  );
}
