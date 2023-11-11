/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "s3-hype-note.s3.ap-northeast-2.amazonaws.com",
        port: "",
        pathname: "/member/**",
      },
    ],
  },
};

module.exports = nextConfig;
