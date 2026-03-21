<template>
  <div class="job-card">
    <div class="job-icon">
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <polygon points="5 3 19 12 5 21 5 3"/>
      </svg>
    </div>
    <div class="job-content">
      <router-link :to="{ name: 'ShowJobDetails', params: { id: job.id } }" class="job-link">
        <h5 class="job-title">{{ job.name }}</h5>
      </router-link>
      <div class="job-meta">
        <span class="job-id">ID: {{ job.id }}</span>
        <span :class="['status-badge', getStatusClass(job.state)]">
          <span class="status-dot"></span>
          {{ job.state || 'Unknown' }}
        </span>
      </div>
    </div>
    <div class="job-arrow">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M9 18l6-6-6-6"/>
      </svg>
    </div>
  </div>
</template>

<script>
export default {
  name: "JobBox",
  props: ["job"],
  data() {
    return {
      jobs: [],
    };
  },
  methods: {
    ShowJobDetails() {
      this.$router.push({
        name: "ShowJobDetails",
        params: { id: this.$route.params.id },
      });
    },
    getStatusClass(state) {
      if (!state) return 'status-neutral';
      const lowerState = state.toLowerCase();
      if (lowerState === 'running' || lowerState === 'finished') return 'status-success';
      if (lowerState === 'failed' || lowerState === 'canceled') return 'status-danger';
      if (lowerState === 'created' || lowerState === 'scheduled') return 'status-info';
      return 'status-neutral';
    }
  },
};
</script>

<style scoped>
.job-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid #f0f0f0;
}

.job-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
  border-color: #e0e0e0;
}

.job-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background-color: #f8f9fa;
  color: #6c757d;
  transition: all 0.25s ease;
  flex-shrink: 0;
}

.job-card:hover .job-icon {
  background-color: #000000;
  color: #f0c14b;
}

.job-content {
  flex: 1;
  min-width: 0;
}

.job-link {
  text-decoration: none;
}

.job-title {
  margin: 0 0 8px 0;
  font-size: 1rem;
  font-weight: 600;
  color: #212529;
  transition: color 0.2s ease;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.job-link:hover .job-title {
  color: #f0c14b;
}

.job-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 0.85rem;
}

.job-id {
  color: #6c757d;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.02em;
}

.status-success {
  background-color: rgba(40, 167, 69, 0.1);
  color: #28a745;
}

.status-danger {
  background-color: rgba(220, 53, 69, 0.1);
  color: #dc3545;
}

.status-info {
  background-color: rgba(23, 162, 184, 0.1);
  color: #17a2b8;
}

.status-neutral {
  background-color: #f8f9fa;
  color: #6c757d;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: currentColor;
}

.status-success .status-dot {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.6;
    transform: scale(1.2);
  }
}

.job-arrow {
  color: #dee2e6;
  transition: all 0.25s ease;
}

.job-card:hover .job-arrow {
  color: #f0c14b;
  transform: translateX(4px);
}
</style>
