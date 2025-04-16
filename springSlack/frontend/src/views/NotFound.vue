<template>
  <div class="not-found">
    <div class="not-found-content">
      <img src="/404.svg" alt="404 Not Found" class="not-found-image">
      <h1>Page Not Found</h1>
      <p>The page you're looking for doesn't exist or you may not have permission to view it.</p>
      <div class="actions">
        <router-link to="/" class="back-home">
          <i class="fas fa-arrow-left"></i> Back to Home
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f8f8f8;
}

.not-found-content {
  text-align: center;
  max-width: 600px;
  padding: 40px;
}

.not-found-image {
  max-width: 300px;
  margin-bottom: 24px;
}

h1 {
  font-size: 28px;
  color: #1d1c1d;
  margin-bottom: 16px;
}

p {
  font-size: 16px;
  color: #616061;
  margin-bottom: 24px;
}

.actions {
  margin-top: 24px;
}

.back-home {
  display: inline-flex;
  align-items: center;
  padding: 10px 16px;
  background-color: var(--slack-purple);
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.back-home i {
  margin-right: 8px;
}

.back-home:hover {
  background-color: #3f1340;
}
</style> 