import { chatUser } from "@/types/quiz";

const formatTime = (datetime: string) => datetime.slice(11);

export function YourChat(props: chatUser) {
  return (
    <div className="flex w-full mt-2 space-x-3 max-w-xs">
      <div className="flex flex-col items-center justify-center flex-shrink-0 h-15 w-15 rounded-full">
        <div className="h-[50px] w-[50px] flex items-center justify-center">
          <img src={props.userImg} alt="이미지" className="rounded-full object-cover h-[40px] w-[40px]"></img>
        </div>
      </div>
      <div>
        <p className="text-xs text-right">{props.userName}</p>
        <div className="bg-primary text-font_primary p-2 rounded-r-lg rounded-bl-lg">
          <p className="text-sm">{props.content}</p>
        </div>
        <span className="w-full text-xs leading-none text-right block mt-1">{formatTime(props.chatTime)}</span>
      </div>
    </div>
  );
}

export function MyChat(props: chatUser) {
  return (
    <div className="flex w-full mt-2 space-x-3 max-w-xs ml-auto justify-end items-start">
      <div>
        <p className="text-xs text-right">{props.userName}</p>
        <div className="bg-line_primary text-font_primary text-white p-2 rounded-l-lg rounded-br-lg">
          <p className="text-sm">{props.content}</p>
        </div>
        <span className="w-full text-xs leading-none text-right block mt-1">{formatTime(props.chatTime)}</span>
      </div>
      <div className="flex flex-col items-center justify-center flex-shrink-0 h-15 w-15">
        <div className="h-[50px] w-[50px] flex items-center justify-center">
          <img src={props.userImg} alt="이미지" className="rounded-full object-cover h-[40px] w-[40px]"></img>
        </div>
      </div>
    </div>
  );
}
