<template>
  <div class="jar-card">
    <div class="jar-icon">
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/>
        <polyline points="3.27 6.96 12 12.01 20.73 6.96"/>
        <line x1="12" y1="22.08" x2="12" y2="12"/>
      </svg>
    </div>
    <div class="jar-content">
      <router-link :to="{ name: 'ShowJarDetails', params: { id: jar.id } }" class="jar-link">
        <h5 class="jar-title">{{ jar.fileName }}</h5>
      </router-link>
      <div class="jar-meta">
        <span class="jar-id">ID: {{ jar.id }}</span>
        <span class="jar-type">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14 2 14 8 20 8"/>
          </svg>
          JAR File
        </span>
      </div>
    </div>
    <div class="jar-arrow">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M9 18l6-6-6-6"/>
      </svg>
    </div>
  </div>
</template>

<script>
export default {
  name: "JarBox",
  props: ["jar"],
  data() {
    return {
      jars: [],
    };
  },
  methods: {
    ShowJarDetails() {
      this.$router.push({
        name: "ShowJarDetails",
        params: { id: this.$route.params.id },
      });
    },
  },
};
</script>

<style scoped>
.jar-card {
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

.jar-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
  border-color: #e0e0e0;
}

.jar-icon {
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

.jar-card:hover .jar-icon {
  background-color: #000000;
  color: #f0c14b;
}

.jar-content {
  flex: 1;
  min-width: 0;
}

.jar-link {
  text-decoration: none;
}

.jar-title {
  margin: 0 0 8px 0;
  font-size: 1rem;
  font-weight: 600;
  color: #212529;
  transition: color 0.2s ease;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.jar-link:hover .jar-title {
  color: #f0c14b;
}

.jar-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 0.85rem;
}

.jar-id {
  color: #6c757d;
}

.jar-type {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #6c757d;
  padding: 4px 10px;
  background-color: #f8f9fa;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 500;
}

.jar-arrow {
  color: #dee2e6;
  transition: all 0.25s ease;
}

.jar-card:hover .jar-arrow {
  color: #f0c14b;
  transform: translateX(4px);
}
</style>
