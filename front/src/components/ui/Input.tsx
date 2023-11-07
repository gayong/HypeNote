type Props = {
  text: string;
  type: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  autoComplete?: string;
};
export default function Input({ text, type, onChange, autoComplete }: Props) {
  return (
    <input
      id={text}
      name={text}
      type={type}
      required
      className="pl-3 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-[#375dd4] sm:text-sm sm:leading-6"
      onChange={onChange}
      autoComplete={autoComplete}
    />
  );
}
