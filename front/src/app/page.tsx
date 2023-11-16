import Intro from "@/components/intro/Intro";
import { Suspense } from "react";
import loading from "../../public/assets/loading.gif";
import Image from "next/image";

export default function Home() {
  return (
    <div className="h-[5352px] bg-primary">
      <Suspense
        fallback={
          <div className="flex mx-auto justify-center items-center">
            <Image src={loading} alt="로딩" priority />
          </div>
        }>
        <Intro />
      </Suspense>
    </div>
  );
}
