import { ReactNode } from "react";

type Props = {
  text: string;
  iconImg?: ReactNode;
  onClick: () => void;
  wFull?: boolean;
};
export default function Button({ text, iconImg, onClick, wFull }: Props) {
  return (
    <button
      type="submit"
      className={`flex text-font_primary justify-center rounded-md bg-primary px-3 py-1.5 text-sm font-semibold leading-6 shadow-sm hover:bg-hover_primary focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 ${
        wFull ? `w-full` : ""
      }`}>
      <span className="">{iconImg}</span>
      {text}
    </button>
  );
}
