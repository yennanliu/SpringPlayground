# Frontend Refactor & Improvement Plan

**Project:** data-platform-ui (Vue.js Frontend)
**Date:** 2026-03-17
**Status:** Planning Phase

---

## Table of Contents

1. [Current State Assessment](#current-state-assessment)
2. [Architecture Overview](#architecture-overview)
3. [High Priority Improvements](#high-priority-improvements)
4. [Medium Priority Improvements](#medium-priority-improvements)
5. [Lower Priority Improvements](#lower-priority-improvements)
6. [Implementation Phases](#implementation-phases)
7. [Progress Tracking](#progress-tracking)

---

## Current State Assessment

### Summary Metrics

| Area | Current State | Assessment | Action Required |
|------|---------------|------------|-----------------|
| Framework | Vue 2.6.14 | Outdated (EOL Dec 2023) | Plan migration |
| Build Tool | Vue CLI 5.0 | Current | OK for now |
| State Management | Props + LocalStorage | No centralized store | Critical |
| API Layer | Direct Axios in components | Anti-pattern | Critical |
| Testing | 0% coverage | Critical gap | High priority |
| Route Guards | None | Security concern | High priority |
| Form Validation | Manual/None | User experience issue | Medium priority |
| Styling | Bootstrap CDN + scoped CSS | Fragmented | Medium priority |
| TypeScript | None | Missing type safety | Future |

### Current Project Structure

```
data-platform-ui/
├── src/
│   ├── assets/              # Images and static files
│   ├── components/          # Reusable Vue components
│   │   ├── Cluster/
│   │   ├── Jar/
│   │   ├── Job/
│   │   ├── Navbar.vue
│   │   └── HelloWorld.vue
│   ├── views/               # Page-level components
│   │   ├── Admin/
│   │   ├── Cluster/
│   │   ├── Jar/
│   │   ├── Job/
│   │   ├── SqlJob/
│   │   ├── Zeppelin/
│   │   ├── Home.vue
│   │   ├── Signin.vue
│   │   ├── Signup.vue
│   │   └── AboutView.vue
│   ├── router/              # Vue Router configuration
│   ├── App.vue              # Root component
│   └── main.js              # Entry point
├── public/                  # Static files
├── Dockerfile               # Docker configuration
├── package.json             # Dependencies
└── vue.config.js            # Vue CLI configuration
```

### Key Issues Identified

1. **No API Service Layer:** Axios calls scattered across 20+ components
2. **Prop Drilling:** `baseURL` passed through entire component tree
3. **No Auth State Management:** Token handling via localStorage only
4. **Unprotected Routes:** Admin routes accessible without authentication
5. **No Tests:** Zero test coverage
6. **Inconsistent Code Style:** Mix of `require()` and ES6 imports
7. **Unused Dependencies:** `bootstrap-vue`, `highlight.js` installed but not used

---

## Architecture Overview

### Current Architecture

```
┌─────────────────────────────────────────────────────┐
│                   App.vue (Root)                    │
│              (manages baseURL prop)                 │
└──────────────────────┬──────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
   Navbar.vue    RouterView      (Footer)
        │              │
        │    ┌─────────┴─────────┬────────────┐
        │    │                   │            │
        │  Feature Views      Auth Views   Zeppelin
        │  (Jar/Job/Cluster)  (Sign in/up)  Views
        │    │                   │            │
        │  [Direct Axios]    [Direct Axios]  [iframe]
        │         ↓                ↓
        └────────────────→ Backend API ←──────────────
```

### Target Architecture

```
┌─────────────────────────────────────────────────────┐
│                   App.vue (Root)                    │
└──────────────────────┬──────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
   Navbar.vue    RouterView      (Footer)
        │         (with guards)
        │              │
        │    ┌─────────┴─────────┬────────────┐
        │    │                   │            │
        │  Feature Views      Auth Views   Zeppelin
        │         │                │
        │         ↓                ↓
        │    ┌─────────────────────────┐
        │    │   Pinia Store           │
        │    │  (auth, jars, jobs...)  │
        │    └───────────┬─────────────┘
        │                ↓
        │    ┌─────────────────────────┐
        │    │   Services Layer        │
        │    │  (API abstraction)      │
        │    └───────────┬─────────────┘
        │                ↓
        │    ┌─────────────────────────┐
        │    │   Axios Instance        │
        │    │  (interceptors, config) │
        │    └───────────┬─────────────┘
        │                ↓
        └────────────→ Backend API
```

---

## High Priority Improvements

### 1. Create API Service Layer

**Problem:** Axios calls are scattered across 20+ components with duplicated error handling and hardcoded URLs.

**Solution:** Create centralized service layer.

**Target Structure:**
```
src/
├── services/
│   ├── api.js              # Axios instance with interceptors
│   ├── authService.js      # Authentication operations
│   ├── jarService.js       # Jar CRUD operations
│   ├── jobService.js       # Job CRUD operations
│   ├── clusterService.js   # Cluster CRUD operations
│   ├── sqlJobService.js    # SQL Job operations
│   └── notebookService.js  # Zeppelin notebook operations
```

**api.js Example:**
```javascript
import axios from 'axios';

const apiClient = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8081',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request interceptor - add auth token
apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor - handle errors
apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // Handle unauthorized
      localStorage.removeItem('token');
      window.location.href = '/signin';
    }
    return Promise.reject(error);
  }
);

export default apiClient;
```

**Service Example (jarService.js):**
```javascript
import apiClient from './api';

export default {
  getAll() {
    return apiClient.get('/jar/');
  },
  getById(id) {
    return apiClient.get(`/jar/${id}`);
  },
  upload(formData) {
    return apiClient.post('/jar/add_jar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  },
  delete(id) {
    return apiClient.delete(`/jar/${id}`);
  }
};
```

**Tasks:**
- [ ] Create `src/services/api.js` with Axios instance
- [ ] Create `src/services/authService.js`
- [ ] Create `src/services/jarService.js`
- [ ] Create `src/services/jobService.js`
- [ ] Create `src/services/clusterService.js`
- [ ] Create `src/services/sqlJobService.js`
- [ ] Create `src/services/notebookService.js`
- [ ] Refactor all components to use services
- [ ] Remove direct axios imports from components

---

### 2. Implement State Management (Pinia)

**Problem:** `baseURL` prop drilled through entire component tree; auth state scattered across components using localStorage directly.

**Solution:** Add Pinia for centralized state management.

**Target Structure:**
```
src/
├── stores/
│   ├── index.js           # Pinia setup
│   ├── auth.js            # Authentication state
│   ├── jars.js            # Jars state (optional)
│   ├── jobs.js            # Jobs state (optional)
│   └── clusters.js        # Clusters state (optional)
```

**auth.js Example:**
```javascript
import { defineStore } from 'pinia';
import authService from '@/services/authService';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null,
    loading: false,
    error: null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    currentUser: (state) => state.user
  },

  actions: {
    async login(credentials) {
      this.loading = true;
      this.error = null;
      try {
        const response = await authService.login(credentials);
        this.token = response.data.token;
        localStorage.setItem('token', this.token);
        return response;
      } catch (error) {
        this.error = error.response?.data?.message || 'Login failed';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    logout() {
      this.user = null;
      this.token = null;
      localStorage.removeItem('token');
    }
  }
});
```

**Tasks:**
- [ ] Install Pinia: `npm install pinia@2`
- [ ] Create `src/stores/index.js`
- [ ] Create `src/stores/auth.js`
- [ ] Register Pinia in `main.js`
- [ ] Refactor `Signin.vue` and `Signup.vue` to use auth store
- [ ] Refactor `Navbar.vue` to use auth store
- [ ] Remove `baseURL` prop drilling from App.vue

---

### 3. Add Router Guards

**Problem:** Admin routes are unprotected; any user can access `/admin/*` routes without authentication.

**Solution:** Implement navigation guards in Vue Router.

**Implementation:**
```javascript
// router/index.js
import { useAuthStore } from '@/stores/auth';

const routes = [
  {
    path: '/admin/jars/add',
    name: 'AddJar',
    component: AddJar,
    meta: { requiresAuth: true }
  },
  // ... other routes with meta
];

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'Signin', query: { redirect: to.fullPath } });
  } else {
    next();
  }
});
```

**Tasks:**
- [ ] Add `meta: { requiresAuth: true }` to protected routes
- [ ] Implement `beforeEach` navigation guard
- [ ] Add redirect after successful login
- [ ] Add 404 catch-all route

---

### 4. Add Form Validation

**Problem:** No client-side validation; forms can submit invalid/empty data.

**Solution:** Add VeeValidate for form validation.

**Tasks:**
- [ ] Install VeeValidate: `npm install vee-validate@4 @vee-validate/rules`
- [ ] Create validation rules configuration
- [ ] Add validation to `Signin.vue`
- [ ] Add validation to `Signup.vue`
- [ ] Add validation to `AddJar.vue`
- [ ] Add validation to `AddJob.vue`
- [ ] Add validation to `AddCluster.vue`
- [ ] Add validation to `AddSqlJob.vue`

---

## Medium Priority Improvements

### 5. Extract Reusable Components

**Problem:** Duplicated UI patterns across List views and forms.

**Target Components:**
```
src/components/
├── common/
│   ├── BaseTable.vue       # Standardized data table
│   ├── FormInput.vue       # Text input with validation
│   ├── FormSelect.vue      # Dropdown with validation
│   ├── FormTextarea.vue    # Textarea with validation
│   ├── FileUpload.vue      # File upload component
│   ├── ConfirmDialog.vue   # Confirmation modal
│   ├── LoadingSpinner.vue  # Loading indicator
│   └── ErrorMessage.vue    # Error display
```

**Tasks:**
- [ ] Create `BaseTable.vue` component
- [ ] Create `FormInput.vue` component
- [ ] Create `FormSelect.vue` component
- [ ] Create `ConfirmDialog.vue` component
- [ ] Create `LoadingSpinner.vue` component
- [ ] Refactor List views to use `BaseTable`
- [ ] Refactor forms to use form components

---

### 6. Styling Consolidation

**Problem:** Bootstrap loaded via CDN; unused dependencies; duplicate styles.

**Tasks:**
- [ ] Remove `bootstrap-vue` from package.json (unused)
- [ ] Remove `highlight.js` from package.json (unused)
- [ ] Install Bootstrap via npm: `npm install bootstrap`
- [ ] Create `src/styles/` directory structure
- [ ] Create `_variables.scss` for theme customization
- [ ] Create `_tables.scss` for shared table styles
- [ ] Create `_forms.scss` for shared form styles
- [ ] Remove CDN links from `public/index.html`
- [ ] Import Bootstrap in `main.js`

---

### 7. Add Testing Infrastructure

**Problem:** Zero test coverage; no testing framework installed.

**Target Structure:**
```
tests/
├── unit/
│   ├── services/
│   │   ├── authService.spec.js
│   │   ├── jarService.spec.js
│   │   └── ...
│   ├── stores/
│   │   └── auth.spec.js
│   └── components/
│       ├── Navbar.spec.js
│       └── ...
└── e2e/
    ├── auth.spec.js
    └── ...
```

**Tasks:**
- [ ] Install testing dependencies: `npm install -D @vue/test-utils jest vue-jest`
- [ ] Create `jest.config.js`
- [ ] Create test for `authService.js`
- [ ] Create test for `auth` store
- [ ] Create test for `Navbar.vue`
- [ ] Create test for login flow
- [ ] Add test script to `package.json`
- [ ] Target: 80% coverage on services layer

---

### 8. Fix Code Inconsistencies

**Problem:** Mix of `require()` and ES6 imports; console.log statements; loose ESLint.

**Tasks:**
- [ ] Convert all `require()` to ES6 `import`
- [ ] Remove all `console.log` statements (or use proper logger)
- [ ] Update `.eslintrc.js` with stricter rules
- [ ] Add `no-console` rule (warn in dev, error in prod)
- [ ] Run `npm run lint --fix`

---

## Lower Priority Improvements

### 9. Vue 3 Migration

**Problem:** Vue 2 reached End of Life in December 2023.

**Migration Steps:**
1. Review Vue 3 migration guide
2. Update dependencies
3. Migrate to Composition API
4. Update router syntax
5. Update Pinia usage (already Vue 3 compatible)
6. Consider migrating to Vite for faster builds

**Tasks:**
- [ ] Audit components for Vue 3 compatibility
- [ ] Create migration plan document
- [ ] Update Vue and related packages
- [ ] Migrate components incrementally
- [ ] Update build configuration

---

### 10. Environment Configuration

**Problem:** Hardcoded `localhost:8081` URL; no environment-specific configs.

**Solution:** Add environment files.

**Files to Create:**
```
.env                    # Default/shared variables
.env.development        # Development overrides
.env.production         # Production overrides
.env.local              # Local overrides (gitignored)
```

**Example .env:**
```
VUE_APP_API_URL=http://localhost:8081
VUE_APP_TITLE=Data Platform
```

**Tasks:**
- [ ] Create `.env` file
- [ ] Create `.env.development` file
- [ ] Create `.env.production` file
- [ ] Add `.env.local` to `.gitignore`
- [ ] Update `api.js` to use `process.env.VUE_APP_API_URL`
- [ ] Document environment variables in README

---

### 11. TypeScript Adoption

**Problem:** No type safety; IDE support limited; refactoring risky.

**Tasks:**
- [ ] Install TypeScript and Vue TypeScript support
- [ ] Create `tsconfig.json`
- [ ] Convert services to TypeScript first
- [ ] Create API response types/interfaces
- [ ] Gradually convert components

---

## Implementation Phases

### Phase 1: Foundation (Week 1-2)
**Goal:** Establish API layer and environment configuration

| Task | Priority | Estimate | Status |
|------|----------|----------|--------|
| Create API service layer | High | 2 days | [ ] Not Started |
| Create environment config | Medium | 0.5 day | [ ] Not Started |
| Remove unused dependencies | Low | 0.5 day | [ ] Not Started |

**Deliverables:**
- [ ] `src/services/` directory with all service files
- [ ] `.env` files configured
- [ ] All components refactored to use services

---

### Phase 2: Security & State (Week 2-3)
**Goal:** Implement proper authentication and state management

| Task | Priority | Estimate | Status |
|------|----------|----------|--------|
| Implement Pinia stores | High | 1.5 days | [ ] Not Started |
| Add router guards | High | 1 day | [ ] Not Started |
| Add form validation | High | 1.5 days | [ ] Not Started |

**Deliverables:**
- [ ] `src/stores/` directory with auth store
- [ ] Protected routes with proper redirects
- [ ] Validated forms with error messages

---

### Phase 3: DRY & Quality (Week 3-4)
**Goal:** Extract reusable components and improve code quality

| Task | Priority | Estimate | Status |
|------|----------|----------|--------|
| Extract reusable components | Medium | 2 days | [ ] Not Started |
| Consolidate styling | Medium | 1 day | [ ] Not Started |
| Fix code inconsistencies | Medium | 0.5 day | [ ] Not Started |

**Deliverables:**
- [ ] `src/components/common/` with shared components
- [ ] `src/styles/` with SCSS organization
- [ ] Clean, consistent codebase

---

### Phase 4: Testing (Week 4-5)
**Goal:** Establish testing infrastructure and coverage

| Task | Priority | Estimate | Status |
|------|----------|----------|--------|
| Setup Jest | Medium | 0.5 day | [ ] Not Started |
| Write service tests | Medium | 1.5 days | [ ] Not Started |
| Write store tests | Medium | 1 day | [ ] Not Started |
| Write component tests | Medium | 1.5 days | [ ] Not Started |

**Deliverables:**
- [ ] Jest configured and running
- [ ] 80%+ coverage on services
- [ ] Critical path tests for auth flow

---

### Phase 5: Modernization (Future)
**Goal:** Upgrade to Vue 3 and modern tooling

| Task | Priority | Estimate | Status |
|------|----------|----------|--------|
| Vue 3 migration | Low | 3-5 days | [ ] Not Started |
| TypeScript adoption | Low | 3-5 days | [ ] Not Started |
| Vite migration | Low | 1 day | [ ] Not Started |

**Deliverables:**
- [ ] Vue 3 with Composition API
- [ ] TypeScript throughout
- [ ] Vite for fast development

---

## Progress Tracking

### Overall Progress

| Phase | Status | Completion |
|-------|--------|------------|
| Phase 1: Foundation | Not Started | 0% |
| Phase 2: Security & State | Not Started | 0% |
| Phase 3: DRY & Quality | Not Started | 0% |
| Phase 4: Testing | Not Started | 0% |
| Phase 5: Modernization | Not Started | 0% |

### Recent Updates

| Date | Update |
|------|--------|
| 2026-03-17 | Initial plan created |

---

## Appendix

### API Endpoints Reference

| Method | Endpoint | Service | Description |
|--------|----------|---------|-------------|
| GET | `/jar/` | jarService | List all jars |
| GET | `/jar/{id}` | jarService | Get jar by ID |
| POST | `/jar/add_jar` | jarService | Upload new jar |
| GET | `/job/` | jobService | List all jobs |
| GET | `/job/{id}` | jobService | Get job by ID |
| POST | `/job/add` | jobService | Create new job |
| GET | `/cluster/` | clusterService | List all clusters |
| GET | `/cluster/{id}` | clusterService | Get cluster by ID |
| POST | `/cluster/add_cluster` | clusterService | Add new cluster |
| POST | `/sql_job/add` | sqlJobService | Create SQL job |
| POST | `/users/signIn` | authService | User login |
| POST | `/users/signup` | authService | User registration |

### Dependencies to Add

```json
{
  "pinia": "^2.1.0",
  "vee-validate": "^4.11.0",
  "@vee-validate/rules": "^4.11.0"
}
```

### Dependencies to Remove

```json
{
  "bootstrap-vue": "^2.23.1",
  "highlight.js": "^11.9.0"
}
```

### Dev Dependencies to Add

```json
{
  "@vue/test-utils": "^1.3.0",
  "jest": "^29.0.0",
  "vue-jest": "^3.0.0"
}
```
