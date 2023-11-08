import { chatUser } from "@/types/quiz";
import Image from "next/image";

export function YourChat(props: chatUser) {
  return (
    <div className="flex w-full mt-2 space-x-3 max-w-xs">
      <div className="flex flex-col items-center justify-center flex-shrink-0 h-10 w-10 rounded-full">
        <Image src={props.userImg} alt="이미지" width={30} height={30}></Image>
        <p className="text-xs text-center">{props.userName}</p>
      </div>
      <div>
        <div className="bg-primary text-font_primary p-3 rounded-r-lg rounded-bl-lg">
          <p className="text-sm">{props.content}</p>
        </div>
        <span className="w-full text-xs text-left block mt-1 leading-none">{props.chatTime}</span>
      </div>
    </div>
  );
}

export function MyChat(props: chatUser) {
  return (
    <div className="flex w-full mt-2 space-x-3 max-w-xs ml-auto justify-end">
      <div>
        <div className="bg-line_primary text-font_primary text-white p-3 rounded-l-lg rounded-br-lg">
          <p className="text-sm">{props.content}</p>
        </div>
        <span className="w-full text-xs leading-none text-right block mt-1">{props.chatTime}</span>
      </div>
      <div className="flex flex-col items-center justify-center flex-shrink-0 h-10 w-10 rounded-full">
        <Image src={props.userImg} alt="이미지" width={30} height={30}></Image>
        <p className="text-xs text-center">{props.userName}</p>
      </div>
    </div>
  );
}
