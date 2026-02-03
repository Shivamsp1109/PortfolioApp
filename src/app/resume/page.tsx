export default function ResumePage() {
  return (
    <main className="mx-auto min-h-screen w-full max-w-4xl px-6 py-14 md:px-10">
      <article className="panel">
        <p className="eyebrow">Resume</p>
        <h1 className="section-title">Shivam Kumar Pandey - Software Developer</h1>
        <p className="mt-6 text-[var(--text-muted)]">View my resume below or download it directly.</p>
        <div className="mt-5 flex flex-wrap gap-3">
          <a className="btn btn-primary text-sm" href="/ShivamKumarPandey.pdf" target="_blank" rel="noreferrer">
            Open PDF
          </a>
          <a className="btn btn-secondary min-w-[190px] px-6 text-sm" href="/ShivamKumarPandey.pdf" download>
            Download Resume
          </a>
        </div>

        <div className="mt-8 overflow-hidden rounded-xl border border-[var(--line)] bg-white">
          <iframe
            title="Shivam Resume"
            src="/ShivamKumarPandey.pdf#view=FitH"
            className="h-[72vh] w-full"
          />
        </div>

        <section className="mt-8">
          <h2 className="text-xl font-semibold">Experience</h2>
          <ul className="mt-3 list-disc space-y-2 pl-5 text-[var(--text-muted)]">
            <li>Software Developer Engineer - Rayole Software (Jun 2025 - Present)</li>
            <li>Software Developer Intern - InnogenX, Bangalore (Jan 2025 - May 2025)</li>
            <li>Data Analytics Intern - Bunny Bucks, Remote (Sep 2024 - Jan 2025)</li>
            <li>Research Intern - Creative Interfaces Lab, Delhi (May 2024 - Aug 2024)</li>
          </ul>
        </section>

        <section className="mt-8">
          <h2 className="text-xl font-semibold">Projects</h2>
          <ul className="mt-3 list-disc space-y-2 pl-5 text-[var(--text-muted)]">
            <li>Explainable Visual Risk Analysis System for Construction Safety</li>
            <li>AR Chat</li>
          </ul>
        </section>

        <section className="mt-8">
          <h2 className="text-xl font-semibold">Education</h2>
          <ul className="mt-3 list-disc space-y-2 pl-5 text-[var(--text-muted)]">
            <li>B.Tech in Computer Science and Engineering - IIIT Vadodara (Dec 2021 - May 2025)</li>
            <li>Patna, Bihar, India</li>
          </ul>
        </section>

        <section className="mt-8">
          <h2 className="text-xl font-semibold">Skills</h2>
          <p className="mt-3 text-[var(--text-muted)]">
            AI/ML, Computer Vision, LLM, CNN, Java, Python, C#.NET, OpenCV, Mediapipe, Pandas, TensorFlow, YOLOv8,
            React.js, Node.js, Express.js, MySQL, PostgreSQL, MongoDB, Firebase, AWS (S3, EC2, SageMaker), Git,
            GitHub
          </p>
        </section>

        <section className="mt-8">
          <h2 className="text-xl font-semibold">Contact</h2>
          <p className="mt-3 text-[var(--text-muted)]">
            Email: shivam.sp1109@gmail.com | Phone: +91 9142752148
          </p>
        </section>
      </article>
    </main>
  );
}
