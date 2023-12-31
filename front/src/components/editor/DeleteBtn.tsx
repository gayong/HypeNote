import useDeleteNote from "@/hooks/useDeleteNote";
import { Button, Modal, Popconfirm, Space } from "antd";
import { TbTrashXFilled } from "react-icons/tb";
import { useRouter } from "next/navigation";
// import { userAtom } from "@/store/authAtom";
import { useAtom } from "jotai";
import useGetUserInfo from "@/hooks/useGetUserInfo";

type Props = {
  id: string;
};

export default function DeleteBtn({ id }: Props) {
  const { DeleteNote } = useDeleteNote();
  const { data: user, isLoading, isError, error } = useGetUserInfo();

  // const [user] = useAtom(userAtom);

  const router = useRouter();

  const handleDelete = () => {
    if (id) {
      DeleteNote(id)
        .then(() => {
          // @ts-ignore
          const index = user.documentsRoots.indexOf(id);
          if (index > -1) {
            user?.documentsRoots.splice(index, 1);
          }
          router.push("/main");
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  return (
    <>
      <Popconfirm
        placement="bottom"
        title={<div className="font-preBd">DELETE</div>}
        description={<div className="font-preRg">정말 해당 노트를 삭제하시겠습니까?</div>}
        onConfirm={handleDelete}
        // onCancel={}
        okText="네"
        okButtonProps={{
          danger: true,
          className: "hover:none bg-red font-preRg",
        }}
        cancelButtonProps={{ danger: true }}
        cancelText="아니오">
        <TbTrashXFilled
          className="dark:text-font_primary text-2xl
         hover:text-red dark:hover:text-red absolute top-5 right-20"
          title="문서 삭제"
        />
      </Popconfirm>
    </>
  );
}
