import { fetchEditorUploadImage } from "@/api/service/editor";

const useImageUpload = () => {
  const ImageUpload = async (file: File) => {
    console.log(file);
    try {
      const response = await fetchEditorUploadImage(file);
      return response.data.data.url;
    } catch (err) {
      console.log(err);
    }
    return "";
  };
  return { ImageUpload };
};
export default useImageUpload;
