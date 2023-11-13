import useDeleteNote from "@/hooks/useDeleteNote";
import { Button, Modal, Popconfirm, Space } from "antd";
import { TbTrashXFilled } from "react-icons/tb";
import { useRouter } from "next/navigation";

type Props = {
  id: string;
};

export default function DeleteBtn({ id }: Props) {
  const { DeleteNote } = useDeleteNote();
  const router = useRouter();
  const handleDelete = () => {
    if (id) {
      try {
        DeleteNote(id);
        router.push("/");
      } catch (error) {
        console.log(error);
      }
    }
  };

  return (
    <>
      <Popconfirm
        placement="bottom"
        title={"노트 삭제"}
        description={"정말 삭제하시겠습니까?"}
        onConfirm={handleDelete}
        // onCancel={}
        okText="네"
        okButtonProps={{ className: "bg-[#e60000]" }}
        cancelText="아니오">
        <TbTrashXFilled
          className="dark:text-font_primary text-2xl
         hover:text-[#e60000] dark:hover:text-[#e60000] absolute top-5 right-20"
          title="문서 삭제"
        />
      </Popconfirm>
    </>
  );
}
