import { ContactForm } from "@/components/ContactForm";
import BackgroundScene from "@/components/BackgroundScene";
import HeroScene from "@/components/HeroScene";
import InteractiveLayer from "@/components/InteractiveLayer";
import Reveal from "@/components/Reveal";
import SectionHashSync from "@/components/SectionHashSync";
import TiltCard from "@/components/TiltCard";
import Image from "next/image";

const gmailComposeLink =
  "https://mail.google.com/mail/?view=cm&fs=1&to=shivam.sp1109%40gmail.com&su=Regarding&body=Dear%20Shivam%2C%0AI%20came%20across%20your%20portfolio%20and%20resume.%0A%0A%0A%0ARegards%2C";

const experience = [
  {
    period: "Jun 2025 - Present",
    title: "Software Developer Engineer",
    company: "Rayole Software (Remote)",
    details:
      "Published 7 Android apps used by 400k+ users, integrated native plugins, analyzed user postbacks, and built in-app AI models trained from user interactions.",
  },
  {
    period: "Jan 2025 - May 2025",
    title: "Software Developer Intern",
    company: "InnogenX (Bangalore)",
    details:
      "Worked on backend development, integrated language translation APIs, and implemented AWS Polly and S3-based asset workflows.",
  },
  {
    period: "Sep 2024 - Jan 2025",
    title: "Data Analytics Intern",
    company: "Bunny Bucks (Remote)",
    details:
      "Built predictive analytics pipelines for loan approvals using Python and Scikit-learn, then visualized trends with MySQL-backed dashboards.",
  },
  {
    period: "May 2024 - Aug 2024",
    title: "Research Intern",
    company: "Creative Interfaces Lab (Delhi)",
    details:
      "Developed frontend prototypes for AR/VR Sanskrit learning and implemented interactive auditory learning experiences.",
  },
];

const projects = [
  {
    name: "Explainable Visual Risk Analysis for Construction Sites",
    description:
      "Built an explainable computer-vision system using YOLO object detection with geometry-based reasoning and rule-driven safety risk assessment for Construction site workers.",
    tech: "Python, OpenCV, YOLOv8s, Node.js, Express, React",
    link: "https://github.com/Shivamsp1109/Explainable-Visual-Risk-Analysis-System-EVRAS-for-Construction-Safety",
  },
  {
    name: "AR Chat",
    description:
      "Created immersive 3D chat interactions with emotion-aware avatars, combining computer vision with real-time AR communication.",
    tech: "React.js, Unity, C#, Photon Networking, OpenCV",
    link: "https://github.com/sid142003/ARchat/tree/main",
  },
];

const skills = [
  "Python",
  "Java",
  "C#.NET",
  "AI/ML",
  "Computer Vision",
  "LLMs",
  "CNN",
  "OpenCV",
  "MediaPipe",
  "TensorFlow",
  "YOLOv8",
  "Pandas",
  "React.js",
  "Node.js",
  "Express.js",
  "MySQL",
  "PostgreSQL",
  "MongoDB",
  "Firebase",
  "AWS EC2",
  "AWS S3",
  "Amazon SageMaker",
  "Git & GitHub",
  "Data Analytics",
  "DSA",
];

export default function Home() {
  return (
    <div className="relative min-h-screen overflow-hidden">
      <BackgroundScene />
      <InteractiveLayer />
      <SectionHashSync />
      <div className="spotlight spotlight-left" />
      <div className="spotlight spotlight-right" />
      <div className="noise-overlay" />
      <header className="content-shell mx-auto flex w-full max-w-6xl items-center justify-between px-6 py-6 md:px-10">
        <a className="flex items-center gap-3 font-heading text-2xl tracking-wide text-[var(--text-main)]" href="#home">
          <Image
            src="/profile.jpg"
            alt="Shivam profile"
            width={32}
            height={32}
            className="h-8 w-8 rounded-full object-cover"
            priority
          />
          <span>Shivam</span>
        </a>
        <nav className="hidden gap-6 text-sm md:flex">
          {["about", "experience", "projects", "skills", "contact"].map((item) => (
            <a key={item} href={`#${item}`} className="nav-link">
              {item}
            </a>
          ))}
        </nav>
        <a className="btn btn-secondary resume-cta text-sm" href="/resume">
          Resume
        </a>
      </header>

      <main className="content-shell mx-auto flex w-full max-w-6xl flex-col gap-20 px-6 pb-24 md:px-10">
        <section id="home" className="hero-grid grid items-center gap-8 py-8 md:grid-cols-2 md:py-16">
          <div className="space-y-6">
            <p className="eyebrow">Software Developer | AI/ML Enthusiast</p>
            <h1 className="hero-title font-heading text-5xl leading-[0.95] md:text-7xl">
              Building intelligent products with code, data, and creativity.
            </h1>
            <p className="max-w-xl text-base text-[var(--text-muted)] md:text-lg">
              Hi, I am Shivam Kumar Pandey - a software developer passionate about AI/ML, computer vision, and
              full-stack engineering. I design practical, motion-rich products that scale from prototypes to production
              systems.
            </p>
            <div className="flex flex-wrap gap-4">
              <a className="btn btn-primary" href="#projects">
                View Works
              </a>
              <a className="btn btn-secondary" href="#contact">
                Hire Me
              </a>
            </div>
          </div>
          <div className="hero-scene-wrap">
            <HeroScene />
          </div>
        </section>

        <Reveal>
          <section id="about" className="panel">
            <p className="eyebrow">About Me</p>
            <h2 className="section-title">I combine software engineering with applied AI to solve real problems.</h2>
            <p className="section-copy">
              I am a B.Tech CSE graduate from IIIT Vadodara with hands-on experience in app development, backend
              systems, machine learning, and analytics. I enjoy building data-informed products, from AI-enhanced
              mobile apps to explainable computer vision systems and modern web interfaces.
            </p>
          </section>
        </Reveal>

        <Reveal delay={0.05}>
          <section id="experience" className="panel">
            <p className="eyebrow">Experience</p>
            <h2 className="section-title">Professional Experience</h2>
            <div className="mt-4 grid gap-4">
              {experience.map((job) => (
                <TiltCard key={`${job.company}-${job.title}`} className="card card-3d">
                  <p className="text-sm text-[var(--text-muted)]">{job.period}</p>
                  <h3 className="mt-2 text-xl font-semibold text-[var(--text-main)]">{job.title}</h3>
                  <p className="text-sm text-[var(--accent)]">{job.company}</p>
                  <p className="mt-3 text-[var(--text-muted)]">{job.details}</p>
                </TiltCard>
              ))}
            </div>
          </section>
        </Reveal>

        <Reveal delay={0.08}>
          <section id="projects" className="panel">
            <p className="eyebrow">Projects</p>
            <h2 className="section-title">Featured Projects</h2>
            <div className="mt-4 grid gap-4 md:grid-cols-2">
              {projects.map((project) => (
                <TiltCard key={project.name} className="card card-3d">
                  <h3 className="text-2xl font-semibold text-[var(--text-main)]">{project.name}</h3>
                  <p className="mt-3 text-[var(--text-muted)]">{project.description}</p>
                  <p className="mt-4 text-sm text-[var(--accent)]">{project.tech}</p>
                  <a
                    className="mt-6 inline-flex text-sm text-[var(--text-main)] underline underline-offset-4"
                    href={project.link}
                    target="_blank"
                    rel="noreferrer"
                  >
                    View project
                  </a>
                </TiltCard>
              ))}
            </div>
          </section>
        </Reveal>

        <Reveal delay={0.1}>
          <section id="skills" className="panel">
            <p className="eyebrow">Skills</p>
            <h2 className="section-title">Tech Stack & Skills</h2>
            <div className="mt-3 flex flex-wrap gap-3">
              {skills.map((skill) => (
                <span key={skill} className="chip lift">
                  {skill}
                </span>
              ))}
            </div>
          </section>
        </Reveal>

        <Reveal delay={0.12}>
          <section id="contact" className="panel">
            <p className="eyebrow">Contact</p>
            <h2 className="section-title">Let us build something impactful.</h2>
            <div className="mt-3 grid gap-8 md:grid-cols-2">
              <div className="space-y-4 text-[var(--text-muted)]">
                <a
                  className="contact-link"
                  href="https://www.linkedin.com/in/shivam-kpandey-/"
                  target="_blank"
                  rel="noreferrer"
                >
                  LinkedIn
                </a>
                <a className="contact-link" href="https://github.com/Shivamsp1109" target="_blank" rel="noreferrer">
                  GitHub
                </a>
                <a className="contact-link" href={gmailComposeLink} target="_blank" rel="noreferrer">
                  shivam.sp1109@gmail.com
                </a>
              </div>
              <ContactForm />
            </div>
          </section>
        </Reveal>
      </main>
    </div>
  );
}
