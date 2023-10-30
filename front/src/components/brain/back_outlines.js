import * as THREE from "three";
import * as React from "react";
import { shaderMaterial } from "@react-three/drei";
import { extend, applyProps } from "@react-three/fiber";

const OutlinesMaterial = shaderMaterial();

// export function Outlines({
//   color = "black",
//   opacity = 1,
//   transparent = false,
//   thickness = 0.05,
//   angle = Math.PI,
//   ...props
// }) {

export function Outlines() {
  const ref = React.useRef(null);
  const [material] = React.useState(() => new OutlinesMaterial({ side: THREE.BackSide }));

  React.useMemo(() => extend({ OutlinesMaterial }), []);
  React.useLayoutEffect(() => {
    const group = ref.current;
    const parent = group.parent;
    if (parent && parent.geometry) {
      let mesh;
      if (parent.skeleton) {
        mesh = new THREE.SkinnedMesh();
        mesh.material = material;
        mesh.bind(parent.skeleton, parent.bindMatrix);
        group.add(mesh);
      } else if (parent.isInstancedMesh) {
        mesh = new THREE.InstancedMesh(parent.geometry, material, parent.count);
        mesh.instanceMatrix = parent.instanceMatrix;
        group.add(mesh);
      } else {
        mesh = new THREE.Mesh();
        mesh.material = material;
        group.add(mesh);
      }
      mesh.geometry = angle ? toCreasedNormals(parent.geometry, angle) : parent.geometry;
      return () => {
        if (angle) mesh.geometry.dispose();
        group.remove(mesh);
      };
    }
    // }, [angle, ref.current?.parent?.geometry]);
  }, []);

  React.useLayoutEffect(() => {
    const group = ref.current;
    console.log(group.children.length);
    const mesh = group.children[0];
    if (mesh) {
      applyProps(mesh.material, { transparent, thickness, color, opacity });
    }
  }, [angle, transparent, thickness, color, opacity]);

  return <group ref={ref} {...props} />;
}

export default Outlines;
