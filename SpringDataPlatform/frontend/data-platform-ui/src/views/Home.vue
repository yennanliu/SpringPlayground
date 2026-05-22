<template>
  <div class="home">

    <!-- ── Hero ──────────────────────────────────────────────────────────── -->
    <section class="hero">
      <div class="hero-bg" />
      <div class="hero-grid" />
      <div class="container hero-inner">
        <div class="hero-badge">
          <span class="badge-dot-live" />
          Live data platform
        </div>
        <h1 class="hero-title">
          Unified control plane<br />
          for <span class="accent">Apache Flink</span>
        </h1>
        <p class="hero-sub">
          Submit jobs, manage clusters, run SQL queries and explore notebooks — all in one place.
        </p>
        <div class="hero-cta">
          <router-link :to="{ name: 'ListCluster' }" class="cta-primary">
            Open Dashboard
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </router-link>
          <router-link :to="{ name: 'AddJar' }" class="cta-ghost">Upload JAR</router-link>
        </div>
      </div>
    </section>

    <!-- ── Stats bar ─────────────────────────────────────────────────────── -->
    <section class="stats-bar">
      <div class="container">
        <div class="stats-grid">

          <StatsCard label="Clusters registered" :value="stats.clusters" :loading="statsLoading" accent="blue">
            <template #icon>
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="2" y="2" width="20" height="8" rx="2"/>
                <rect x="2" y="14" width="20" height="8" rx="2"/>
                <line x1="6" y1="6" x2="6.01" y2="6"/>
                <line x1="6" y1="18" x2="6.01" y2="18"/>
              </svg>
            </template>
          </StatsCard>

          <StatsCard label="Flink jobs tracked" :value="stats.jobs" :loading="statsLoading" accent="green">
            <template #icon>
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polygon points="5 3 19 12 5 21 5 3"/>
              </svg>
            </template>
          </StatsCard>

          <StatsCard label="JAR files uploaded" :value="stats.jars" :loading="statsLoading" accent="orange">
            <template #icon>
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/>
              </svg>
            </template>
          </StatsCard>

          <StatsCard label="Notebooks created" :value="stats.notebooks" :loading="statsLoading" accent="default">
            <template #icon>
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/>
                <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/>
              </svg>
            </template>
          </StatsCard>

        </div>
      </div>
    </section>

    <!-- ── Features ───────────────────────────────────────────────────────── -->
    <section class="features">
      <div class="container">
        <div class="section-header">
          <h2>Everything your data team needs</h2>
          <p>Manage the full Flink lifecycle from a single, modern interface</p>
        </div>
        <div class="features-grid">

          <div class="feature" v-for="f in features" :key="f.title">
            <div class="feature-icon" :style="`background:${f.bg}; color:${f.color}`" v-html="f.icon" />
            <h3>{{ f.title }}</h3>
            <p>{{ f.desc }}</p>
            <router-link :to="{ name: f.route }" class="feature-link">
              Go to {{ f.title }}
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                <path d="M9 18l6-6-6-6"/>
              </svg>
            </router-link>
          </div>

        </div>
      </div>
    </section>

    <!-- ── How it works ───────────────────────────────────────────────────── -->
    <section class="how-it-works">
      <div class="container">
        <div class="section-header">
          <h2>Up and running in minutes</h2>
          <p>Four steps to your first Flink job</p>
        </div>
        <div class="steps">
          <div class="step" v-for="(step, i) in steps" :key="i">
            <div class="step-num">{{ i + 1 }}</div>
            <div class="step-connector" v-if="i < steps.length - 1" />
            <h4>{{ step.title }}</h4>
            <p>{{ step.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ── External links ────────────────────────────────────────────────── -->
    <section class="links-section">
      <div class="container">
        <div class="section-header">
          <h2>Quick access</h2>
          <p>Jump straight to external tools</p>
        </div>
        <div class="links-grid">
          <a v-for="link in externalLinks" :key="link.title"
             :href="link.href" target="_blank" rel="noopener" class="ext-link">
            <div class="ext-link-body">
              <h4>{{ link.title }}</h4>
              <p>{{ link.desc }}</p>
            </div>
            <svg class="ext-arrow" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M7 17L17 7M17 7H7M17 7v10"/>
            </svg>
          </a>
        </div>
      </div>
    </section>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import StatsCard from '@/components/common/StatsCard.vue'
import { clusterService, jarService, jobService } from '@/services'

const statsLoading = ref(true)
const stats = ref({ clusters: 0, jobs: 0, jars: 0, notebooks: 0 })

const loadStats = async () => {
  try {
    const [clusters, jobs, jars] = await Promise.allSettled([
      clusterService.getAll(),
      jobService.getAll(),
      jarService.getAll(),
    ])
    stats.value.clusters  = clusters.status  === 'fulfilled' ? clusters.value.length  : '—'
    stats.value.jobs      = jobs.status      === 'fulfilled' ? jobs.value.length      : '—'
    stats.value.jars      = jars.status      === 'fulfilled' ? jars.value.length      : '—'
    stats.value.notebooks = '—'
  } catch {
    // fail silently — stats are non-critical
  } finally {
    statsLoading.value = false
  }
}

onMounted(loadStats)

const features = [
  {
    title: 'Cluster Management',
    desc:  'Register Flink clusters, ping connectivity, and track status in real time.',
    route: 'ListCluster',
    icon:  '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="2" width="20" height="8" rx="2"/><rect x="2" y="14" width="20" height="8" rx="2"/><line x1="6" y1="6" x2="6.01" y2="6"/><line x1="6" y1="18" x2="6.01" y2="18"/></svg>',
    bg:    'rgba(59,130,246,.10)', color: '#3b82f6',
  },
  {
    title: 'JAR Jobs',
    desc:  'Upload compiled Flink JARs and submit them directly to your cluster with one click.',
    route: 'ListJar',
    icon:  '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg>',
    bg:    'rgba(240,193,75,.12)', color: '#d4a83a',
  },
  {
    title: 'SQL Jobs',
    desc:  'Write and submit Flink SQL statements via the SQL Gateway without any code compilation.',
    route: 'AddSqlJob',
    icon:  '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><ellipse cx="12" cy="5" rx="9" ry="3"/><path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/><path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/></svg>',
    bg:    'rgba(34,197,94,.10)', color: '#22c55e',
  },
  {
    title: 'Zeppelin Notebooks',
    desc:  'Create and execute interactive Zeppelin paragraphs for exploratory data analysis.',
    route: 'ListNotebook',
    icon:  '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/></svg>',
    bg:    'rgba(168,85,247,.10)', color: '#a855f7',
  },
]

const steps = [
  { title: 'Connect a Cluster',   desc: 'Register your Flink JobManager URL and verify connectivity.' },
  { title: 'Upload a JAR',        desc: 'Upload your compiled Flink application JAR file.' },
  { title: 'Submit a Job',        desc: 'Select a JAR and launch it on the connected cluster.' },
  { title: 'Monitor & Analyse',   desc: 'Track job state and dive into results with Zeppelin.' },
]

const externalLinks = [
  { title: 'Flink UI',     desc: 'Apache Flink JobManager dashboard',   href: 'http://localhost:8081/#/overview' },
  { title: 'Zeppelin UI',  desc: 'Interactive analytics notebooks',      href: 'http://localhost:9080/#/' },
  { title: 'Swagger UI',   desc: 'Explore and test the REST API',        href: 'http://localhost:9997/swagger-ui.html' },
  { title: 'GitHub',       desc: 'View the project source code',         href: 'https://github.com/yennanliu/SpringPlayground/tree/main/SpringDataPlatform' },
]
</script>

<style scoped>
/* ── Hero ─────────────────────────────────────────────────────────────── */
.hero {
  background: var(--color-black);
  color: var(--color-white);
  padding: 120px 0 100px;
  position: relative;
  overflow: hidden;
}
.hero-bg {
  position: absolute; inset: 0;
  background: radial-gradient(ellipse 80% 60% at 60% 40%, rgba(240,193,75,.12) 0%, transparent 70%),
              radial-gradient(ellipse 50% 50% at 0% 100%, rgba(59,130,246,.08) 0%, transparent 60%);
}
.hero-grid {
  position: absolute; inset: 0;
  background-image:
    linear-gradient(rgba(255,255,255,.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,.03) 1px, transparent 1px);
  background-size: 40px 40px;
  mask-image: radial-gradient(ellipse at center, black 30%, transparent 80%);
}
.hero-inner { position: relative; z-index: 1; max-width: 680px; }

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 14px;
  background: rgba(255,255,255,.06);
  border: 1px solid rgba(255,255,255,.12);
  border-radius: var(--radius-full);
  font-size: 0.78rem;
  font-weight: 500;
  color: rgba(255,255,255,.7);
  margin-bottom: 28px;
}
.badge-dot-live {
  width: 7px; height: 7px;
  background: var(--color-success);
  border-radius: 50%;
  box-shadow: 0 0 0 2px rgba(34,197,94,.3);
  animation: live-pulse 2s ease-in-out infinite;
}
@keyframes live-pulse {
  0%,100% { box-shadow: 0 0 0 2px rgba(34,197,94,.3); }
  50%      { box-shadow: 0 0 0 6px rgba(34,197,94,.0); }
}

.hero-title {
  font-size: clamp(2.2rem, 5vw, 3.4rem);
  font-weight: 800;
  line-height: 1.08;
  letter-spacing: -0.03em;
  margin-bottom: 20px;
  color: var(--color-white);
}
.accent { color: var(--color-accent); }

.hero-sub {
  font-size: 1.1rem;
  line-height: 1.65;
  color: rgba(255,255,255,.65);
  margin-bottom: 36px;
  max-width: 520px;
}

.hero-cta { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }

.cta-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 13px 26px;
  background: var(--color-accent);
  color: var(--color-black);
  border-radius: var(--radius-md);
  font-size: 0.95rem;
  font-weight: 700;
  text-decoration: none;
  transition: all var(--transition);
}
.cta-primary:hover {
  background: var(--color-accent-dark);
  transform: translateY(-2px);
  box-shadow: var(--shadow-accent);
}

.cta-ghost {
  display: inline-flex;
  align-items: center;
  padding: 13px 26px;
  border: 1.5px solid rgba(255,255,255,.18);
  color: rgba(255,255,255,.8);
  border-radius: var(--radius-md);
  font-size: 0.95rem;
  font-weight: 500;
  text-decoration: none;
  transition: all var(--transition);
}
.cta-ghost:hover { border-color: rgba(255,255,255,.4); color: var(--color-white); }

/* ── Stats bar ────────────────────────────────────────────────────────── */
.stats-bar {
  padding: 40px 0;
  background: var(--color-white);
  border-bottom: 1px solid var(--color-gray-200);
}
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

/* ── Section shared ───────────────────────────────────────────────────── */
.section-header {
  text-align: center;
  margin-bottom: 52px;
}
.section-header h2 {
  font-size: 1.9rem;
  font-weight: 800;
  margin-bottom: 10px;
  letter-spacing: -0.025em;
}
.section-header p { color: var(--color-gray-500); font-size: 1rem; margin: 0; }

/* ── Features ─────────────────────────────────────────────────────────── */
.features { padding: 80px 0; background: var(--color-gray-50); }
.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
.feature {
  background: var(--color-white);
  border: 1px solid var(--color-gray-200);
  border-radius: var(--radius-lg);
  padding: 28px 24px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  transition: all var(--transition-slow);
}
.feature:hover { transform: translateY(-4px); box-shadow: var(--shadow-lg); border-color: transparent; }
.feature-icon {
  width: 52px; height: 52px;
  border-radius: var(--radius-md);
  display: flex; align-items: center; justify-content: center;
}
.feature h3 { font-size: 1rem; font-weight: 700; margin: 0; }
.feature p  { font-size: 0.875rem; color: var(--color-gray-500); margin: 0; line-height: 1.6; flex: 1; }
.feature-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 0.82rem;
  font-weight: 600;
  color: var(--color-gray-700);
  text-decoration: none;
  margin-top: 4px;
  transition: color var(--transition);
}
.feature-link:hover { color: var(--color-black); }
.feature-link svg { transition: transform var(--transition); }
.feature-link:hover svg { transform: translateX(3px); }

/* ── How it works ─────────────────────────────────────────────────────── */
.how-it-works { padding: 80px 0; background: var(--color-white); }
.steps {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
  position: relative;
}
.step { text-align: center; padding: 8px 16px; position: relative; }
.step-num {
  width: 48px; height: 48px;
  border-radius: 50%;
  background: var(--color-black);
  color: var(--color-white);
  font-size: 1.1rem; font-weight: 800;
  display: flex; align-items: center; justify-content: center;
  margin: 0 auto 18px;
  position: relative; z-index: 1;
  transition: all var(--transition);
}
.step:hover .step-num { background: var(--color-accent); color: var(--color-black); transform: scale(1.1); }
.step-connector {
  position: absolute;
  top: 32px;
  left: calc(50% + 30px);
  width: calc(100% - 44px);
  height: 2px;
  background: var(--color-gray-200);
}
.step h4 { font-size: 0.95rem; font-weight: 700; margin-bottom: 8px; }
.step p  { font-size: 0.82rem; color: var(--color-gray-500); margin: 0; line-height: 1.6; }

/* ── External links ───────────────────────────────────────────────────── */
.links-section { padding: 80px 0; background: var(--color-gray-50); }
.links-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.ext-link {
  background: var(--color-white);
  border: 1px solid var(--color-gray-200);
  border-radius: var(--radius-lg);
  padding: 22px 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  text-decoration: none;
  color: var(--color-gray-900);
  transition: all var(--transition);
}
.ext-link:hover {
  background: var(--color-black);
  color: var(--color-white);
  border-color: var(--color-black);
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
}
.ext-link-body h4 { font-size: 0.95rem; font-weight: 700; margin: 0 0 4px; }
.ext-link-body p  { font-size: 0.8rem; color: var(--color-gray-500); margin: 0; transition: color var(--transition); }
.ext-link:hover .ext-link-body p { color: rgba(255,255,255,.6); }
.ext-arrow {
  opacity: 0;
  transform: translate(-6px, 6px);
  color: var(--color-accent);
  transition: all var(--transition);
  flex-shrink: 0;
}
.ext-link:hover .ext-arrow { opacity: 1; transform: translate(0,0); }

/* ── Responsive ───────────────────────────────────────────────────────── */
@media (max-width: 1024px) {
  .stats-grid    { grid-template-columns: repeat(2, 1fr); }
  .features-grid { grid-template-columns: repeat(2, 1fr); }
  .links-grid    { grid-template-columns: repeat(2, 1fr); }
  .steps         { grid-template-columns: repeat(2, 1fr); }
  .step-connector { display: none; }
}
@media (max-width: 640px) {
  .hero { padding: 80px 0 72px; }
  .stats-grid    { grid-template-columns: 1fr; }
  .features-grid { grid-template-columns: 1fr; }
  .links-grid    { grid-template-columns: 1fr; }
  .steps         { grid-template-columns: 1fr; }
}
</style>
