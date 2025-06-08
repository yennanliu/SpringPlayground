<template>
  <div id="user-details">
    <!-- Hero Section -->
    <div class="user-hero">
      <div class="hero-overlay"></div>
      <div class="container">
        <div class="hero-content">
          <div class="user-photo-section">
            <div class="user-photo-container">
              <img
                v-if="!photoError && user.id"
                :src="userPhotoUrl"
                :alt="`${user.firstName} ${user.lastName}`"
                class="user-photo"
                @error="handlePhotoError"
              />
              <div v-else class="user-photo-placeholder">
                <span class="photo-initials">
                  {{ getInitials(user.firstName, user.lastName) }}
                </span>
              </div>
            </div>
          </div>
          
          <div class="user-info-section">
            <h1 class="user-title">{{ user.firstName }} {{ user.lastName }}</h1>
            <p class="user-role">{{ user.role || 'Employee' }}</p>
            <p class="user-description">
              Dedicated professional contributing to organizational excellence and team success
            </p>
            
            <div class="user-meta">
              <div class="meta-item">
                <i class="bi bi-envelope"></i>
                <span>{{ user.email }}</span>
              </div>
              <div class="meta-item">
                <i class="bi bi-hash"></i>
                <span>ID: {{ user.id }}</span>
              </div>
              <div class="meta-item" v-if="user.departmentId">
                <i class="bi bi-building"></i>
                <router-link :to="`/departments/show/${user.departmentId}`" class="meta-link">
                  Department {{ user.departmentId }}
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <div class="container">
        <!-- User Statistics -->
        <section class="stats-section">
          <div class="section-header">
            <h2>Profile Overview</h2>
            <div class="header-actions">
              <router-link 
                :to="`/users/edit/${user.id}`" 
                class="btn btn-outline-primary"
              >
                <i class="bi bi-pencil-square mr-2"></i>Edit Profile
              </router-link>
            </div>
          </div>
          
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon primary">
                <i class="bi bi-calendar-event"></i>
              </div>
              <div class="stat-content">
                <h3>{{ userStats.totalVacations }}</h3>
                <p>Total Vacations</p>
                <span class="stat-change neutral">{{ userStats.pendingVacations }} pending</span>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon success">
                <i class="bi bi-check-circle"></i>
              </div>
              <div class="stat-content">
                <h3>{{ userStats.approvedVacations }}</h3>
                <p>Approved Requests</p>
                <span class="stat-change positive">{{ getApprovalRate() }}% approval rate</span>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon info">
                <i class="bi bi-people"></i>
              </div>
              <div class="stat-content">
                <h3>{{ userStats.totalSubordinates }}</h3>
                <p>Team Members</p>
                <span class="stat-change neutral">Direct reports</span>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon warning">
              <i class="bi bi-person-up"></i>
              </div>
              <div class="stat-content">
                <h3>{{ user.managerId ? 'Yes' : 'No' }}</h3>
                <p>Has Manager</p>
                <span class="stat-change neutral" v-if="user.managerId">
                  <router-link :to="`/users/show/${user.managerId}`" class="manager-link">
                    View Manager
                  </router-link>
                </span>
                <span v-else class="stat-change neutral">Independent role</span>
              </div>
            </div>
          </div>
        </section>

        <!-- Vacations Section -->
        <section class="vacations-section">
          <div class="section-header">
            <h2>Vacation History</h2>
            <div class="header-actions">
              <div class="search-filter">
                <select v-model="vacationStatusFilter" class="status-filter">
                  <option value="">All Status</option>
                  <option value="PENDING">Pending</option>
                  <option value="APPROVED">Approved</option>
                  <option value="REJECTED">Rejected</option>
                </select>
              </div>
            </div>
          </div>

          <div v-if="filteredVacations.length > 0" class="vacations-grid">
            <div 
              v-for="vacation in filteredVacations" 
              :key="vacation.id" 
              class="vacation-card"
            >
              <div class="vacation-header">
                <div class="vacation-dates">
                  <div class="date-item">
                    <i class="bi bi-calendar-plus"></i>
                    <span>{{ formatDate(vacation.startDate) }}</span>
                  </div>
                  <div class="date-separator">→</div>
                  <div class="date-item">
                    <i class="bi bi-calendar-minus"></i>
                    <span>{{ formatDate(vacation.endDate) }}</span>
                  </div>
                </div>
                <div class="vacation-status">
                  <span class="status-badge" :class="getStatusClass(vacation.status)">
                    {{ vacation.status || 'Pending' }}
                  </span>
                </div>
              </div>
              
              <div class="vacation-info">
                <h4 class="vacation-type">{{ vacation.type || 'Vacation' }}</h4>
                <p class="vacation-duration">{{ getVacationDuration(vacation.startDate, vacation.endDate) }} days</p>
                
                <div class="vacation-details">
                  <div class="detail-item">
                    <i class="bi bi-hash"></i>
                    <span>ID: {{ vacation.id }}</span>
                  </div>
                </div>
              </div>
              
              <div class="vacation-actions">
                <router-link 
                  :to="`/vacation/${vacation.id}`"
                  class="btn btn-primary btn-sm"
                >
                  <i class="bi bi-eye mr-1"></i>View Details
                </router-link>
              </div>
            </div>
          </div>

          <!-- Empty State for Vacations -->
          <div v-else class="empty-state">
            <div class="empty-icon">
              <i class="bi bi-calendar-x"></i>
            </div>
            <h3>No vacations found</h3>
            <p>{{ vacationStatusFilter ? 'No vacations match the selected status.' : 'No vacation requests have been made yet.' }}</p>
          </div>
        </section>

        <!-- Subordinates Section -->
        <section class="subordinates-section" v-if="subordinates.length > 0">
          <div class="section-header">
            <h2>Team Members</h2>
            <div class="header-actions">
              <div class="search-filter">
                <input 
                  type="text" 
                  placeholder="Search team members..." 
                  v-model="subordinateSearchQuery"
                  class="search-input"
                >
              </div>
            </div>
          </div>

          <div class="team-grid">
            <div 
              v-for="subordinate in filteredSubordinates" 
              :key="subordinate.id" 
              class="team-member-card"
            >
              <div class="member-header">
                <div class="member-avatar">
                  <img 
                    v-if="!subordinatePhotoErrors[subordinate.id]" 
                    :src="getSubordinatePhotoUrl(subordinate.id)" 
                    :alt="`${subordinate.firstName} ${subordinate.lastName}`"
                    class="avatar-image"
                    @error="handleSubordinatePhotoError(subordinate.id)"
                  />
                  <span v-else class="avatar-initials">
                    {{ getInitials(subordinate.firstName, subordinate.lastName) }}
                  </span>
                </div>
                <div class="member-status">
                  <span class="status-indicator online"></span>
                  <span class="status-text">Active</span>
                </div>
              </div>
              
              <div class="member-info">
                <h3 class="member-name">{{ subordinate.firstName }} {{ subordinate.lastName }}</h3>
                <p class="member-email">{{ subordinate.email }}</p>
                
                <div class="member-details">
                  <div class="detail-item">
                    <i class="bi bi-person-badge"></i>
                    <span class="role-badge" :class="getRoleClass(subordinate.role)">
                      {{ subordinate.role || 'Employee' }}
                    </span>
                  </div>
                  <div class="detail-item">
                    <i class="bi bi-building"></i>
                    <span>Dept: {{ subordinate.departmentId }}</span>
                  </div>
                </div>
              </div>
              
              <div class="member-actions">
                <router-link 
                  :to="`/users/show/${subordinate.id}`"
                  class="btn btn-primary btn-sm"
                >
                  <i class="bi bi-eye mr-1"></i>View Profile
                </router-link>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      user: {},
      userVacations: [],
      subordinates: [],
      photoError: false,
      subordinatePhotoErrors: {},
      vacationStatusFilter: '',
      subordinateSearchQuery: '',
    };
  },
  computed: {
    userPhotoUrl() {
      if (this.photoError || !this.user.id) {
        return null;
      }
      return `http://localhost:9998/users/photo/${this.user.id}`;
    },
    userStats() {
      return {
        totalVacations: this.userVacations.length,
        approvedVacations: this.userVacations.filter(v => v.status === 'APPROVED').length,
        pendingVacations: this.userVacations.filter(v => v.status === 'PENDING').length,
        totalSubordinates: this.subordinates.length,
      };
    },
    filteredVacations() {
      let filtered = this.userVacations;
      if (this.vacationStatusFilter) {
        filtered = filtered.filter(vacation => 
          vacation.status === this.vacationStatusFilter
        );
      }
      return filtered;
    },
    filteredSubordinates() {
      if (!this.subordinateSearchQuery) return this.subordinates;
      
      const query = this.subordinateSearchQuery.toLowerCase();
      return this.subordinates.filter(subordinate => 
        `${subordinate.firstName} ${subordinate.lastName}`.toLowerCase().includes(query) ||
        subordinate.email.toLowerCase().includes(query)
      );
    }
  },
  methods: {
    getInitials(firstName, lastName) {
      if (!firstName && !lastName) return 'U';
      const first = firstName ? firstName.charAt(0).toUpperCase() : '';
      const last = lastName ? lastName.charAt(0).toUpperCase() : '';
      return first + last;
    },
    handlePhotoError() {
      console.log('Error loading user photo, using default');
      this.photoError = true;
    },
    handleSubordinatePhotoError(userId) {
      this.$set(this.subordinatePhotoErrors, userId, true);
    },
    getSubordinatePhotoUrl(userId) {
      return `http://localhost:9998/users/photo/${userId}`;
    },
    getRoleClass(role) {
      const roleMap = {
        'ADMIN': 'role-admin',
        'MANAGER': 'role-manager',
        'USER': 'role-user',
      };
      return roleMap[role] || 'role-default';
    },
    getStatusClass(status) {
      const statusMap = {
        'APPROVED': 'status-approved',
        'PENDING': 'status-pending',
        'REJECTED': 'status-rejected',
      };
      return statusMap[status] || 'status-default';
    },
    getApprovalRate() {
      if (this.userStats.totalVacations === 0) return 0;
      return Math.round((this.userStats.approvedVacations / this.userStats.totalVacations) * 100);
    },
    formatDate(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', { 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric' 
      });
    },
    getVacationDuration(startDate, endDate) {
      if (!startDate || !endDate) return 0;
      const start = new Date(startDate);
      const end = new Date(endDate);
      const diffTime = Math.abs(end - start);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      return diffDays;
    },
    async getUser() {
      try {
        const response = await axios.get(`http://localhost:9998/users/${this.$route.params.id}`);
        this.user = response.data;
      } catch (error) {
        console.log('Error fetching user:', error);
      }
    },
    async getSubordinates() {
      try {
        const response = await axios.get(`http://localhost:9998/users/subordinates/${this.$route.params.id}`);
        this.subordinates = response.data;
      } catch (error) {
        console.log('Error fetching subordinates:', error);
        this.subordinates = [];
      }
    },
    async getUserVacations() {
      try {
        const response = await axios.get("http://localhost:9998/vacation/");
        this.userVacations = response.data.filter(
          (vacation) => vacation.userId == this.$route.params.id
        );
      } catch (error) {
        console.log('Error fetching vacations:', error);
        this.userVacations = [];
      }
    },
  },
  mounted() {
    this.getUser();
    this.getSubordinates();
    this.getUserVacations();
  },
};
</script>

<style scoped>
/* CSS Variables */
:root {
  --primary-color: #667eea;
  --secondary-color: #764ba2;
  --success-color: #10b981;
  --warning-color: #f59e0b;
  --danger-color: #ef4444;
  --info-color: #3b82f6;
  --text-primary: #1f2937;
  --text-secondary: #6b7280;
  --text-muted: #9ca3af;
  --bg-white: #ffffff;
  --bg-gray-50: #f9fafb;
  --bg-gray-100: #f3f4f6;
  --border-color: #e5e7eb;
  --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  --border-radius: 8px;
  --transition: all 0.3s ease;
}

#user-details {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-gray-50) 0%, var(--bg-white) 100%);
}

/* Hero Section */
.user-hero {
  position: relative;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
  color: white;
  padding: 4rem 0;
  overflow: hidden;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.1);
  pointer-events: none;
}

.hero-content {
  display: flex;
  align-items: center;
  gap: 3rem;
  position: relative;
  z-index: 2;
}

.user-photo-section {
  flex-shrink: 0;
}

.user-photo-container {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  overflow: hidden;
  border: 4px solid rgba(255, 255, 255, 0.3);
  box-shadow: var(--shadow-lg);
  background: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-photo {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-photo-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
}

.photo-initials {
  font-size: 3rem;
  font-weight: bold;
  color: white;
}

.user-info-section {
  flex-grow: 1;
}

.user-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-role {
  font-size: 1.5rem;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 1rem;
}

.user-description {
  font-size: 1.1rem;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 2rem;
  line-height: 1.6;
}

.user-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
}

.meta-item i {
  font-size: 1.1rem;
  opacity: 0.9;
}

.meta-link {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  transition: var(--transition);
}

.meta-link:hover {
  color: white;
  border-bottom-color: white;
  text-decoration: none;
}

/* Main Content */
.main-content {
  padding: 4rem 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

/* Section Headers */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid var(--border-color);
}

.section-header h2 {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
}

/* Statistics Section */
.stats-section {
  margin-bottom: 4rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: var(--bg-white);
  border-radius: var(--border-radius);
  padding: 1.5rem;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-color);
  transition: var(--transition);
  position: relative;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--primary-color), var(--secondary-color));
}

.stat-card .stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
  font-size: 1.5rem;
  color: white;
}

.stat-icon.primary { background: var(--primary-color); }
.stat-icon.success { background: var(--success-color); }
.stat-icon.warning { background: var(--warning-color); }
.stat-icon.info { background: var(--info-color); }

.stat-content h3 {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 0.25rem;
}

.stat-content p {
  color: var(--text-secondary);
  font-weight: 500;
  margin-bottom: 0.5rem;
}

.stat-change {
  font-size: 0.875rem;
  font-weight: 500;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  display: inline-block;
}

.stat-change.positive {
  background: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.stat-change.neutral {
  background: var(--bg-gray-100);
  color: var(--text-secondary);
}

.manager-link {
  color: var(--info-color);
  text-decoration: none;
  font-weight: 500;
}

.manager-link:hover {
  text-decoration: underline;
}

/* Vacations Section */
.vacations-section, .subordinates-section {
  margin-bottom: 4rem;
}

.vacations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
}

.vacation-card {
  background: var(--bg-white);
  border-radius: var(--border-radius);
  padding: 1.5rem;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-color);
  transition: var(--transition);
}

.vacation-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.vacation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--border-color);
}

.vacation-dates {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.date-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--text-secondary);
  font-size: 0.875rem;
}

.date-separator {
  color: var(--text-muted);
  font-weight: bold;
}

.vacation-status .status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.status-badge.status-approved {
  background: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.status-badge.status-pending {
  background: rgba(245, 158, 11, 0.1);
  color: var(--warning-color);
}

.status-badge.status-rejected {
  background: rgba(239, 68, 68, 0.1);
  color: var(--danger-color);
}

.status-badge.status-default {
  background: var(--bg-gray-100);
  color: var(--text-secondary);
}

.vacation-info h4 {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.vacation-duration {
  color: var(--text-secondary);
  font-weight: 500;
  margin-bottom: 1rem;
}

.vacation-details, .member-details {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 1rem;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--text-secondary);
  font-size: 0.875rem;
}

.vacation-actions, .member-actions {
  display: flex;
  gap: 0.5rem;
}

/* Team Grid */
.team-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.team-member-card {
  background: var(--bg-white);
  border-radius: var(--border-radius);
  padding: 1.5rem;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-color);
  transition: var(--transition);
}

.team-member-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.member-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.member-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-gray-100);
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-initials {
  font-weight: 600;
  color: var(--text-secondary);
}

.member-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.75rem;
  color: var(--text-secondary);
}

.status-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.status-indicator.online {
  background: var(--success-color);
}

.member-info h3 {
  font-size: 1.125rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 0.25rem;
}

.member-email {
  color: var(--text-secondary);
  margin-bottom: 1rem;
}

.role-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.role-badge.role-admin {
  background: rgba(239, 68, 68, 0.1);
  color: var(--danger-color);
}

.role-badge.role-manager {
  background: rgba(245, 158, 11, 0.1);
  color: var(--warning-color);
}

.role-badge.role-user {
  background: rgba(59, 130, 246, 0.1);
  color: var(--info-color);
}

.role-badge.role-default {
  background: var(--bg-gray-100);
  color: var(--text-secondary);
}

/* Form Controls */
.search-filter {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-input, .status-filter {
  padding: 0.5rem 1rem;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  font-size: 0.875rem;
  transition: var(--transition);
}

.search-input:focus, .status-filter:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

/* Buttons */
.btn {
  padding: 0.5rem 1rem;
  border-radius: var(--border-radius);
  font-weight: 500;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  transition: var(--transition);
  border: 1px solid transparent;
  cursor: pointer;
}

.btn-primary {
  background: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background: #5a67df;
  color: white;
  text-decoration: none;
}

.btn-outline-primary {
  background: transparent;
  color: var(--primary-color);
  border-color: var(--primary-color);
}

.btn-outline-primary:hover {
  background: var(--primary-color);
  color: white;
  text-decoration: none;
}

.btn-outline-secondary {
  background: transparent;
  color: var(--text-secondary);
  border-color: var(--border-color);
}

.btn-outline-secondary:hover {
  background: var(--bg-gray-50);
  text-decoration: none;
}

.btn-sm {
  padding: 0.375rem 0.75rem;
  font-size: 0.875rem;
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: var(--text-secondary);
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-state h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: var(--text-primary);
}

/* Responsive Design */
@media (max-width: 768px) {
  .hero-content {
    flex-direction: column;
    text-align: center;
    gap: 2rem;
  }
  
  .user-photo-container {
    width: 150px;
    height: 150px;
  }
  
  .user-title {
    font-size: 2rem;
  }
  
  .user-meta {
    justify-content: center;
  }
  
  .section-header {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }
  
  .header-actions {
    justify-content: center;
  }
  
  .search-filter {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .search-input, .status-filter {
    width: 100%;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .vacations-grid, .team-grid {
    grid-template-columns: 1fr;
  }
  
  .vacation-header {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }
  
  .vacation-dates {
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .user-hero {
    padding: 2rem 0;
  }
  
  .main-content {
    padding: 2rem 0;
  }
  
  .container {
    padding: 0 0.5rem;
  }
  
  .vacation-card, .team-member-card, .stat-card {
    padding: 1rem;
  }
  
  .section-header h2 {
    font-size: 1.5rem;
  }
}
</style>