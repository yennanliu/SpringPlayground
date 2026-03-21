<template>
  <div class="cluster-card">
    <div class="cluster-icon">
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <rect x="2" y="2" width="20" height="8" rx="2" ry="2"/>
        <rect x="2" y="14" width="20" height="8" rx="2" ry="2"/>
        <line x1="6" y1="6" x2="6.01" y2="6"/>
        <line x1="6" y1="18" x2="6.01" y2="18"/>
      </svg>
    </div>
    <div class="cluster-content">
      <router-link
        :to="{ name: 'ShowClusterDetails', params: { id: cluster.id } }"
        class="cluster-link"
      >
        <h5 class="cluster-title">{{ cluster.url }}:{{ cluster.port }}</h5>
      </router-link>
      <div class="cluster-meta">
        <span class="cluster-id">ID: {{ cluster.id }}</span>
        <span :class="['status-indicator', cluster.status === 'connected' ? 'status-connected' : 'status-disconnected']">
          <span class="status-dot"></span>
          {{ cluster.status || 'Unknown' }}
        </span>
      </div>
    </div>
    <div class="cluster-arrow">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M9 18l6-6-6-6"/>
      </svg>
    </div>
  </div>
</template>

<script>
export default {
  name: "ClusterBox",
  props: ["cluster"],
  data() {
    return {
      clusters: [],
    };
  },
  methods: {
    ShowClusterDetails() {
      this.$router.push({
        name: "ShowClusterDetails",
        params: { id: this.$route.params.id },
      });
    },
  },
};
</script>

<style scoped>
.cluster-card {
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

.cluster-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
  border-color: #e0e0e0;
}

.cluster-icon {
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

.cluster-card:hover .cluster-icon {
  background-color: #000000;
  color: #f0c14b;
}

.cluster-content {
  flex: 1;
  min-width: 0;
}

.cluster-link {
  text-decoration: none;
}

.cluster-title {
  margin: 0 0 8px 0;
  font-size: 1rem;
  font-weight: 600;
  color: #212529;
  transition: color 0.2s ease;
}

.cluster-link:hover .cluster-title {
  color: #f0c14b;
}

.cluster-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 0.85rem;
}

.cluster-id {
  color: #6c757d;
}

.status-indicator {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 500;
  text-transform: capitalize;
}

.status-connected {
  background-color: rgba(40, 167, 69, 0.1);
  color: #28a745;
}

.status-disconnected {
  background-color: rgba(220, 53, 69, 0.1);
  color: #dc3545;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: currentColor;
}

.status-connected .status-dot {
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

.cluster-arrow {
  color: #dee2e6;
  transition: all 0.25s ease;
}

.cluster-card:hover .cluster-arrow {
  color: #f0c14b;
  transform: translateX(4px);
}
</style>
