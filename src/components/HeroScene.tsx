"use client";

import { Canvas, useFrame } from "@react-three/fiber";
import { Environment, Float, MeshTransmissionMaterial, OrbitControls, Sparkles } from "@react-three/drei";
import { useRef } from "react";
import type { Group } from "three";

function CoreOrb() {
  const group = useRef<Group>(null);

  useFrame((state) => {
    if (!group.current) return;
    group.current.rotation.y = state.clock.elapsedTime * 0.3;
    group.current.rotation.x = Math.sin(state.clock.elapsedTime * 0.4) * 0.12;
  });

  return (
    <Float speed={1.8} rotationIntensity={1.6} floatIntensity={1.5}>
      <group ref={group}>
        <mesh>
          <torusKnotGeometry args={[0.9, 0.29, 280, 24]} />
          <MeshTransmissionMaterial
            color="#7cc7ff"
            roughness={0.04}
            thickness={0.6}
            distortion={0.2}
            temporalDistortion={0.22}
            chromaticAberration={0.1}
            anisotropicBlur={0.2}
            backside
          />
        </mesh>
        <mesh rotation={[Math.PI / 2, 0, 0]}>
          <torusGeometry args={[1.55, 0.03, 20, 140]} />
          <meshStandardMaterial color="#f97316" emissive="#f97316" emissiveIntensity={0.7} metalness={0.5} />
        </mesh>
      </group>
    </Float>
  );
}

function OrbitingNodes() {
  const ring = useRef<Group>(null);

  useFrame((state) => {
    if (!ring.current) return;
    ring.current.rotation.z = state.clock.elapsedTime * 0.22;
  });

  return (
    <group ref={ring}>
      {new Array(6).fill(0).map((_, index) => {
        const angle = (Math.PI * 2 * index) / 6;
        const radius = 2.05;
        return (
          <Float key={index} speed={1.3 + index * 0.08} floatIntensity={0.8} rotationIntensity={0.5}>
            <mesh position={[Math.cos(angle) * radius, Math.sin(angle) * radius, (index % 2 ? 1 : -1) * 0.2]}>
              <sphereGeometry args={[0.1, 24, 24]} />
              <meshStandardMaterial color="#60a5fa" emissive="#60a5fa" emissiveIntensity={0.85} />
            </mesh>
          </Float>
        );
      })}
    </group>
  );
}

function HeroRig() {
  const sceneGroup = useRef<Group>(null);

  useFrame((state) => {
    if (!sceneGroup.current) return;
    sceneGroup.current.rotation.y = state.pointer.x * 0.2;
    sceneGroup.current.rotation.x = -state.pointer.y * 0.16;
  });

  return (
    <group ref={sceneGroup}>
      <CoreOrb />
      <OrbitingNodes />
    </group>
  );
}

export default function HeroScene() {
  return (
    <Canvas camera={{ position: [0, 0, 5.2], fov: 42 }} dpr={[1, 1.5]}>
      <color attach="background" args={["#080b1f"]} />
      <ambientLight intensity={0.3} />
      <directionalLight position={[3, 2, 4]} intensity={1.2} color="#93c5fd" />
      <pointLight position={[-2, -1, 2]} intensity={0.7} color="#60a5fa" />
      <spotLight position={[0, 4, 2]} angle={0.4} penumbra={0.7} intensity={1.1} color="#fb923c" />
      <HeroRig />
      <Sparkles count={80} scale={[5.8, 4.5, 5.8]} size={1.45} speed={0.38} color="#93c5fd" />
      <Environment preset="city" />
      <OrbitControls enableZoom={false} enablePan={false} autoRotate autoRotateSpeed={0.8} />
    </Canvas>
  );
}
