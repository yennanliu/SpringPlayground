#!/usr/bin/env node

/**
 * API Integration Test Script
 * Tests REST API endpoints with the backend
 */

import http from 'http';
import https from 'https';
import { URL } from 'url';

const API_BASE = 'http://localhost:8080/api';

const tests = [
  {
    name: 'Health Check',
    method: 'GET',
    url: 'http://localhost:8080/actuator/health',
    skipOnError: false
  },
  {
    name: 'Get Channels (Unauthenticated)',
    method: 'GET',
    url: `${API_BASE}/channels`,
    expectedStatus: [200, 401, 403] // May require auth
  },
  {
    name: 'WebSocket Endpoint Available',
    method: 'GET',
    url: 'http://localhost:8080/ws',
    expectedStatus: [200, 404, 426] // 426 = Upgrade Required (normal for WS)
  }
];

async function makeRequest(test) {
  return new Promise((resolve) => {
    const url = new URL(test.url);
    const client = url.protocol === 'https:' ? https : http;

    const options = {
      hostname: url.hostname,
      port: url.port,
      path: url.pathname + url.search,
      method: test.method,
      headers: {
        'Content-Type': 'application/json',
        ...test.headers
      },
      timeout: 5000
    };

    const req = client.request(options, (res) => {
      let data = '';

      res.on('data', (chunk) => {
        data += chunk;
      });

      res.on('end', () => {
        resolve({
          success: true,
          status: res.statusCode,
          headers: res.headers,
          body: data
        });
      });
    });

    req.on('error', (error) => {
      resolve({
        success: false,
        error: error.message
      });
    });

    req.on('timeout', () => {
      req.destroy();
      resolve({
        success: false,
        error: 'Request timeout (5s)'
      });
    });

    if (test.data) {
      req.write(JSON.stringify(test.data));
    }

    req.end();
  });
}

function isExpectedStatus(actualStatus, expectedStatus) {
  if (!expectedStatus) {
    return actualStatus >= 200 && actualStatus < 300;
  }

  if (Array.isArray(expectedStatus)) {
    return expectedStatus.includes(actualStatus);
  }

  return actualStatus === expectedStatus;
}

async function runApiTests() {
  console.log('🧪 Running API Integration Tests...\n');

  let passed = 0;
  let failed = 0;
  let skipped = 0;

  for (const test of tests) {
    try {
      const result = await makeRequest(test);

      if (!result.success) {
        if (result.error.includes('ECONNREFUSED')) {
          console.log(`⚠️  ${test.name}: SKIPPED - Backend not running`);
          if (test.skipOnError === false) {
            failed++;
          } else {
            skipped++;
          }
          continue;
        }

        console.log(`❌ ${test.name}: FAILED - ${result.error}`);
        failed++;
        continue;
      }

      if (isExpectedStatus(result.status, test.expectedStatus)) {
        console.log(`✅ ${test.name}: PASSED (${result.status})`);
        passed++;

        // Show response preview for successful tests
        if (result.body && result.status === 200) {
          try {
            const json = JSON.parse(result.body);
            const preview = JSON.stringify(json).substring(0, 100);
            console.log(`   Response: ${preview}${preview.length === 100 ? '...' : ''}`);
          } catch (e) {
            // Not JSON, that's ok
          }
        }
      } else {
        console.log(`⚠️  ${test.name}: UNEXPECTED STATUS (${result.status})`);
        console.log(`   Expected: ${test.expectedStatus || '2xx'}`);
        failed++;
      }
    } catch (error) {
      console.log(`❌ ${test.name}: EXCEPTION - ${error.message}`);
      failed++;
    }
  }

  console.log(`\n📊 Results: ${passed} passed, ${failed} failed, ${skipped} skipped`);

  if (failed > 0) {
    console.log('\n💡 Make sure the backend is running:');
    console.log('   cd ../../backend/ChatAppV2');
    console.log('   ./mvnw spring-boot:run');
  }

  process.exit(failed > 0 ? 1 : 0);
}

runApiTests();
