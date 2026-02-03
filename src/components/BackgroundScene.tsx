"use client";

import { Canvas, useFrame } from "@react-three/fiber";
import { Sparkles } from "@react-three/drei";
import { useEffect, useMemo, useRef } from "react";
import type { MutableRefObject } from "react";
import type { Group } from "three";
import { Vector3 } from "three";

function pseudoRandom(seed: number) {
  const value = Math.sin(seed * 999.91) * 43758.5453123;
  return value - Math.floor(value);
}

function DriftingOrbs() {
  const group = useRef<Group>(null);
  const orbs = useMemo(
    () =>
      new Array(18).fill(0).map((_, index) => ({
        id: index,
        x: (pseudoRandom(index + 1) - 0.5) * 24,
        y: (pseudoRandom(index + 17) - 0.5) * 14,
        z: (pseudoRandom(index + 33) - 0.5) * 18,
        size: 0.15 + pseudoRandom(index + 49) * 0.32,
      })),
    [],
  );

  useFrame((state) => {
    if (!group.current) return;
    group.current.rotation.y = state.clock.elapsedTime * 0.03;
    group.current.rotation.x = Math.sin(state.clock.elapsedTime * 0.1) * 0.04;
  });

  return (
    <group ref={group}>
      {orbs.map((orb) => (
        <mesh key={orb.id} position={[orb.x, orb.y, orb.z]}>
          <sphereGeometry args={[orb.size, 20, 20]} />
          <meshStandardMaterial color="#79a6ff" transparent opacity={0.22} roughness={0.35} metalness={0.08} />
        </mesh>
      ))}
    </group>
  );
}

type CameraDirectorProps = {
  scrollProgress: MutableRefObject<number>;
};

const cameraStops: [number, number, number][] = [
  [0, 0, 12],
  [1.3, 0.35, 11.2],
  [-1.5, 0.8, 10.5],
  [1.2, -0.45, 9.8],
  [-1, -0.95, 9.2],
  [0, -0.3, 8.6],
];

function CameraDirector({ scrollProgress }: CameraDirectorProps) {
  const world = useRef<Group>(null);
  const smoothed = useRef(0);
  const targetVector = useRef(new Vector3(...cameraStops[0]));

  useFrame((state) => {
    smoothed.current += (scrollProgress.current - smoothed.current) * 0.06;

    const scaled = smoothed.current * (cameraStops.length - 1);
    const index = Math.min(Math.floor(scaled), cameraStops.length - 2);
    const t = scaled - index;
    const from = cameraStops[index];
    const to = cameraStops[index + 1];

    targetVector.current.set(
      from[0] + (to[0] - from[0]) * t,
      from[1] + (to[1] - from[1]) * t,
      from[2] + (to[2] - from[2]) * t,
    );

    state.camera.position.lerp(targetVector.current, 0.08);
    state.camera.lookAt(0, 0, 0);

    if (world.current) {
      world.current.rotation.y = smoothed.current * 0.55 + state.pointer.x * 0.1;
      world.current.rotation.x = state.pointer.y * 0.06;
      world.current.position.y = -smoothed.current * 1.1;
    }
  });

  return (
    <group ref={world}>
      <DriftingOrbs />
      <Sparkles count={160} scale={[24, 13, 20]} speed={0.24} size={1.4} color="#93c5fd" />
    </group>
  );
}

export default function BackgroundScene() {
  const scrollProgress = useRef(0);

  useEffect(() => {
    const readProgress = () => {
      const doc = document.documentElement;
      const max = doc.scrollHeight - window.innerHeight;
      if (max <= 0) {
        scrollProgress.current = 0;
        return;
      }
      scrollProgress.current = Math.min(1, Math.max(0, window.scrollY / max));
    };

    readProgress();
    window.addEventListener("scroll", readProgress, { passive: true });
    window.addEventListener("resize", readProgress);

    return () => {
      window.removeEventListener("scroll", readProgress);
      window.removeEventListener("resize", readProgress);
    };
  }, []);

  return (
    <div className="background-scene" aria-hidden="true">
      <Canvas camera={{ position: [0, 0, 12], fov: 52 }} dpr={[1, 1.5]}>
        <color attach="background" args={["#050816"]} />
        <fog attach="fog" args={["#050816", 8, 28]} />
        <ambientLight intensity={0.28} />
        <pointLight position={[6, 4, 5]} intensity={0.9} color="#60a5fa" />
        <pointLight position={[-6, -3, 1]} intensity={0.85} color="#f97316" />
        <CameraDirector scrollProgress={scrollProgress} />
      </Canvas>
    </div>
  );
}
