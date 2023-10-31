"use client";

import React, { useRef, useEffect } from "react";
import * as THREE from "three";
import { OrbitControls } from "three/addons/controls/OrbitControls.js";

const ThreeScene: React.FC = () => {
  console.log("찍힘");
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (typeof window !== "undefined") {
      const scene = new THREE.Scene();
      const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
      const renderer = new THREE.WebGLRenderer();
      renderer.setSize(window.innerWidth, window.innerHeight);
      renderer.setClearColor("#faf5ef");
      containerRef.current?.appendChild(renderer.domElement);
      camera.position.z = 5;

      const geometry = new THREE.SphereGeometry(2, 32, 16);
      const material = new THREE.MeshNormalMaterial(); // 조명에 반응하는 재질
      const sphere = new THREE.Mesh(geometry, material);
      scene.add(sphere);

      const edges = new THREE.EdgesGeometry(geometry); // 구체의 엣지 정보를 계산
      const lineMaterial = new THREE.LineBasicMaterial({ color: 0xffffff }); // 테두리의 재질 및 색상
      const lines = new THREE.LineSegments(edges, lineMaterial); // 테두리를 라인으로 표현
      scene.add(lines);

      // const light = new THREE.DirectionalLight(0xffffff, 1); // 흰색, 강도 1의 방향광
      // light.position.set(1, 1, 1); // 광원의 위치 설정
      // scene.add(light);

      // const geometry = new THREE.BoxGeometry();
      // const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
      // const cube = new THREE.Mesh(geometry, material);
      // scene.add(cube);

      // Render the scene and camera
      renderer.render(scene, camera);

      const renderScene = () => {
        sphere.rotation.x += 0.02;
        sphere.rotation.y += 0.02;
        renderer.render(scene, camera);
        requestAnimationFrame(renderScene);
      };

      // Call the renderScene function to start the animation loop
      renderScene();

      const controls = new OrbitControls(camera, renderer.domElement);
      controls.target = sphere.position;

      // const handleResize = () => {
      //   const width = window.innerWidth;
      //   const height = window.innerHeight;

      //   camera.aspect = width / height;
      //   camera.updateProjectionMatrix();

      //   renderer.setSize(width, height);
      // };

      // window.addEventListener("resize", handleResize);
      // return () => {
      //   window.removeEventListener("resize", handleResize);
      // };
    }
  }, []);

  return <div ref={containerRef} />;
};

export default ThreeScene;
