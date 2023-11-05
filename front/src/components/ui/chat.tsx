import { chatUser } from "@/types/quiz";

export function YourChat(props: chatUser) {
  return (
    <div className="flex w-full mt-2 space-x-3 max-w-xs">
      <div className="flex-shrink-0 h-10 w-10 rounded-full">{props.userPk}얼굴</div>
      <div>
        <div className="bg-primary text-font_primary p-3 rounded-r-lg rounded-bl-lg">
          <p className="text-sm">{props.content}</p>
        </div>
        <span className="text-xs text-gray-500 leading-none">{props.chatTime}</span>
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
        <span className="text-xs text-gray-500 leading-none">{props.chatTime}</span>
      </div>
      <div className="flex-shrink-0 h-10 w-10 rounded-full">{props.userPk}얼굴</div>
    </div>
  );
}
