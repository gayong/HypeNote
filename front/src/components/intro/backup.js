"use client";

import Link from "next/link";
import React, { useEffect, useRef } from "react";
import { GlowParticle } from "./glowparticle";

const COLORS = [
  { r: 41, g: 70, b: 162 }, // 메인 컬러
  { r: 72, g: 61, b: 139 }, // 진한 보라
  { r: 54, g: 233, b: 84 }, // 초록
  { r: 255, g: 104, b: 248 }, // 보라
];

export default function Intro() {
  // const [element, setElement] = useState(null);
  const canvasRef = useRef(null);

  useEffect(() => {
    // const canvas = document.createElement("canvas");
    const canvas = canvasRef.current;
    // document.body.appendChild(canvas);
    const ctx = canvas.getContext("2d");

    const pixelRatio = window.devicePixelRatio > 1 ? 2 : 1;
    const totalParticles = 15;
    let particles = [];
    const maxRadius = 700;
    const minRadius = 400;

    const stageWidth = document.body.clientWidth;
    const stageHeight = document.body.clientHeight;

    // resize()
    function resize() {
      const stageWidth = document.body.clientWidth;
      const stageHeight = document.body.clientHeight;

      canvas.width = window.innerWidth * pixelRatio;
      canvas.height = window.innerHeight * pixelRatio;
      ctx.scale(pixelRatio, pixelRatio);

      ctx.globalCompositeOperation = "saturation";

      createParticles();
    }

    //createParticles()
    function createParticles() {
      let curColor = 0;
      particles = [];

      for (let i = 0; i < totalParticles; i++) {
        const item = new GlowParticle(
          Math.random() * stageWidth,
          Math.random() * stageHeight,
          Math.random() * (maxRadius - minRadius) + minRadius,
          COLORS[curColor]
        );

        if (++curColor >= COLORS.length) {
          curColor = 0;
        }

        particles[i] = item;
      }
    }

    //animate()
    function animate() {
      window.requestAnimationFrame(animate.bind(this));
      ctx.clearRect(0, 0, stageWidth, stageHeight);

      for (let i = 0; i < totalParticles; i++) {
        const item = particles[i];
        item.animate(ctx, stageWidth, stageHeight);
      }
    }

    window.addEventListener("resize", resize.bind(this), false);
    resize();

    window.requestAnimationFrame(animate.bind(this));

    // setElement(canvas);
  }, []);

  return (
    <div className="w-full">
      {/* <h1>이것은 인트로페이지</h1> */}
      {/* {element && <div dangerouslySetInnerHTML={{ __html: element.outerHTML }} />} */}
      <canvas ref={canvasRef} />
    </div>
  );
}
