#!/usr/bin/env node

/**
 * Health Check Script
 * Verifies that both backend and frontend servers are running
 */

import http from 'http';

const tests = {
  backend: {
    url: 'http://localhost:8080/actuator/health',
    name: 'Backend API',
    fallbackUrl: 'http://localhost:8080'
  },
  frontend: {
    url: 'http://localhost:5173',
    name: 'Frontend Server'
  }
};

async function checkHealth(url) {
  return new Promise((resolve) => {
    http.get(url, (res) => {
      resolve({ success: res.statusCode === 200, status: res.statusCode });
    }).on('error', (err) => {
      resolve({ success: false, error: err.message });
    });
  });
}

async function runHealthChecks() {
  console.log('🏥 Running Health Checks...\n');

  let allHealthy = true;

  for (const [key, test] of Object.entries(tests)) {
    const result = await checkHealth(test.url);
    const icon = result.success ? '✅' : '❌';
    const status = result.success ? 'UP' : 'DOWN';

    console.log(`${icon} ${test.name}: ${status}`);

    if (!result.success) {
      allHealthy = false;
      if (result.error) {
        console.log(`   Error: ${result.error}`);
      }
      if (result.status) {
        console.log(`   HTTP Status: ${result.status}`);
      }

      // Provide help message
      if (key === 'backend') {
        console.log('   💡 Start backend: cd ../../backend/ChatAppV2 && ./mvnw spring-boot:run');
      } else if (key === 'frontend') {
        console.log('   💡 Start frontend: npm run dev');
      }
    }
  }

  console.log('');

  if (allHealthy) {
    console.log('✅ All services are healthy!');
    process.exit(0);
  } else {
    console.log('❌ Some services are down. Please check above for details.');
    process.exit(1);
  }
}

runHealthChecks();
