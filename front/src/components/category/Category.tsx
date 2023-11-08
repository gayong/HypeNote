import style from "./Category.module.css";
import useCreateNote from "@/hooks/useCreateNote";
interface categoryProps {
  title: string;
  value: number;
}

const subjects = [
  {
    id: "1",
    title: "운영체제",
  },
  {
    id: "2",
    title: "네트워크",
  },
  {
    id: "3",
    title: "자료구조",
  },
];

export default function Category(props: categoryProps) {
  const { createDocument } = useCreateNote();

  const onClickHandler = async (id: string) => {
    const documentId = createDocument(1);
    console.log(documentId);
  };

  return (
    <>
      <div
        className="p-2.5 mt-3 flex items-center rounded-md px-4 duration-300 cursor-pointer hover:bg-hover_primary hover:bg-opacity-50 dark:hover:bg-line_primary dark:hover:bg-opacity-50"
        // onclick="dropdown()"
      >
        <i className="bi bi-chat-left-text-fill"></i>
        <div className="group flex justify-between w-full items-center">
          <span className="text-[15px] ml-2 text-white font-bold">{props.title}</span>

          <span className="text-sm rotate-180" id="arrow">
            <i className="bi bi-chevron-down"></i>
          </span>
          {props.value === 1 && (
            <h1
              className="pb-[3px] m-0 text-right invisible group-hover:visible text-2xl leading-3"
              onClick={() => onClickHandler(props.title)}>
              +
            </h1>
          )}
        </div>
      </div>
      <div className="text-[14px] mx-6 text-white" id="submenu">
        {/* 책 카테고리 */}
        {subjects.map((subject) => (
          <div
            key={subject.id}
            className="group flex justify-between items-center cursor-pointer p-2 hover:bg-hover_primary hover:bg-opacity-50 dark:hover:bg-line_primary dark:hover:bg-opacity-50 rounded-md">
            <h1 className="text-left flex items-center">{subject.title}</h1>
            {props.value === 1 && (
              <h1
                className="pb-[3px] m-0 text-right invisible group-hover:visible text-2xl leading-3"
                onClick={() => onClickHandler(subject.id)}>
                +
              </h1>
            )}
          </div>
        ))}
      </div>
    </>
  );
}
