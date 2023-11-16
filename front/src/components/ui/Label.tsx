type Props = {
  text: string;
};
export default function Label({ text }: Props) {
  return (
    <label
      htmlFor={text}
      className="block text-sm font-medium leading-6 text-gray-900"
    >
      {text}
    </label>
  );
}
