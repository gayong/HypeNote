import Editor from '@/components/editor/Editor';

type Props = {
  params: {
    id: string;
  };
};

export default function EditorPage({ params: { id } }: Props) {
  return (
    <>
      <Editor id={id} />
    </>
  );
}
