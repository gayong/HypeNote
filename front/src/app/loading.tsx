import loading from "../../public/assets/loading.gif";
import Image from "next/image";

export default function Loading() {
  return (
    <div className="flex mr-[19rem] justify-center items-center h-[100vh]">
      <Image src={loading} alt="로딩" priority />
    </div>
  );
}
