<template>
  <div id="department-details">
    <!-- Hero Section -->
    <div class="department-hero">
      <div class="hero-overlay"></div>
      <div class="container">
        <div class="hero-content">
          <div class="department-icon">
            <i class="bi bi-building"></i>
          </div>
          <h1 class="department-title">{{ department.name || 'Department' }}</h1>
          <p class="department-description">
            Managing talent, fostering growth, and driving innovation across our organization
          </p>
          <div class="department-meta">
            <div class="meta-item">
              <i class="bi bi-people-fill"></i>
              <span>{{ departmentStats.totalEmployees }} Employees</span>
            </div>
            <div class="meta-item">
              <i class="bi bi-person-badge"></i>
              <span>{{ departmentStats.managers }} Managers</span>
            </div>
            <div class="meta-item">
              <i class="bi bi-graph-up"></i>
              <span>{{ departmentStats.performance }}% Performance</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <div class="container">
        <!-- Department Statistics -->
        <section class="stats-section">
          <div class="section-header">
            <h2>Department Overview</h2>
            <div class="header-actions">
              <router-link 
                v-if="$route.name === 'AdminDepartment'" 
                :to="`/admin/departments/${department.id}`" 
                class="btn btn-outline-primary"
              >
                <i class="bi bi-pencil-square mr-2"></i>Edit Department
              </router-link>
            </div>
          </div>
          
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon primary">
                <i class="bi bi-people"></i>
              </div>
              <div class="stat-content">
                <h3>{{ departmentStats.totalEmployees }}</h3>
                <p>Total Employees</p>
                <span class="stat-change positive">+3 this month</span>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon success">
                <i class="bi bi-person-check"></i>
              </div>
              <div class="stat-content">
                <h3>{{ departmentStats.activeEmployees }}</h3>
                <p>Active Members</p>
                <span class="stat-change positive">{{ Math.round((departmentStats.activeEmployees / departmentStats.totalEmployees) * 100) }}% active</span>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon warning">
                <i class="bi bi-person-badge"></i>
              </div>
              <div class="stat-content">
                <h3>{{ departmentStats.managers }}</h3>
                <p>Managers</p>
                <span class="stat-change neutral">Leadership team</span>
              </div>
            </div>
            
            <div class="stat-card">
              <div class="stat-icon info">
                <i class="bi bi-graph-up"></i>
              </div>
              <div class="stat-content">
                <h3>{{ departmentStats.performance }}%</h3>
                <p>Performance</p>
                <span class="stat-change positive">+5% vs last quarter</span>
              </div>
            </div>
          </div>
        </section>

        <!-- Team Members Section -->
        <section class="team-section">
          <div class="section-header">
            <h2>Team Members</h2>
            <div class="header-actions">
              <div class="search-filter">
                <input 
                  type="text" 
                  placeholder="Search team members..." 
                  v-model="searchQuery"
                  class="search-input"
                >
                <select v-model="roleFilter" class="role-filter">
                  <option value="">All Roles</option>
                  <option value="ADMIN">Admin</option>
                  <option value="MANAGER">Manager</option>
                  <option value="USER">Employee</option>
                </select>
              </div>
            </div>
          </div>

          <!-- Team Grid -->
          <div v-if="filteredUsers.length > 0" class="team-grid">
            <div 
              v-for="user in filteredUsers" 
              :key="user.id" 
              class="team-member-card"
            >
              <div class="member-header">
                <div class="member-avatar">
                  <img 
                    v-if="!photoErrors[user.id] && getUserPhotoUrl(user.id)" 
                    :src="getUserPhotoUrl(user.id)" 
                    :alt="`${user.firstName} ${user.lastName}`"
                    class="avatar-image"
                    @error="handlePhotoError(user.id)"
                  />
                  <span v-else class="avatar-initials">
                    {{ getInitials(user.firstName, user.lastName) }}
                  </span>
                </div>
                <div class="member-status">
                  <span class="status-indicator online"></span>
                  <span class="status-text">Online</span>
                </div>
              </div>
              
              <div class="member-info">
                <h3 class="member-name">{{ user.firstName }} {{ user.lastName }}</h3>
                <p class="member-email">{{ user.email }}</p>
                
                <div class="member-details">
                  <div class="detail-item">
                    <i class="bi bi-person-badge"></i>
                    <span class="role-badge" :class="getRoleClass(user.role)">
                      {{ user.role || 'Employee' }}
                    </span>
                  </div>
                  <div class="detail-item">
                    <i class="bi bi-hash"></i>
                    <span>ID: {{ user.id }}</span>
                  </div>
                </div>
              </div>
              
              <div class="member-actions">
                <router-link 
                  :to="`/users/show/${user.id}`"
                  class="btn btn-primary btn-sm"
                >
                  <i class="bi bi-eye mr-1"></i>View Profile
                </router-link>
                <router-link 
                  v-if="$route.name === 'AdminDepartment'"
                  :to="`/admin/users/${user.id}`"
                  class="btn btn-outline-secondary btn-sm ml-2"
                >
                  <i class="bi bi-pencil"></i>
                </router-link>
              </div>
            </div>
          </div>

          <!-- Empty State -->
          <div v-else class="empty-state">
            <div class="empty-icon">
              <i class="bi bi-people"></i>
            </div>
            <h3>No team members found</h3>
            <p>{{ searchQuery ? 'Try adjusting your search criteria.' : 'This department has no assigned employees yet.' }}</p>
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
      department: {},
      id: null,
      token: null,
      searchQuery: '',
      roleFilter: '',
      photoErrors: {},
    };
  },
  props: ["baseURL", "departments"],
  computed: {
    departmentStats() {
      const users = this.department.users || [];
      const managers = users.filter(user => user.role === 'MANAGER' || user.role === 'ADMIN').length;
      const activeEmployees = users.filter(user => user.role !== 'INACTIVE').length;
      
      return {
        totalEmployees: users.length,
        activeEmployees: activeEmployees,
        managers: managers,
        performance: 92 // Mock performance data
      };
    },
    filteredUsers() {
      let users = this.department.users || [];
      
      // Filter by search query
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        users = users.filter(user => 
          `${user.firstName} ${user.lastName}`.toLowerCase().includes(query) ||
          user.email.toLowerCase().includes(query)
        );
      }
      
      // Filter by role
      if (this.roleFilter) {
        users = users.filter(user => user.role === this.roleFilter);
      }
      
      return users;
    }
  },
  methods: {
    async getDepartment() {
      try {
        const response = await axios.get(`http://localhost:9998/dep/${this.$route.params.id}`);
        this.department = response.data;
      } catch (err) {
        console.error("Error fetching department:", err);
      }
    },
    
    getInitials(firstName, lastName) {
      return (
        (firstName ? firstName.charAt(0).toUpperCase() : '') + 
        (lastName ? lastName.charAt(0).toUpperCase() : '')
      );
    },
    
    getUserPhotoUrl(userId) {
      if (this.photoErrors[userId]) return null;
      return `http://localhost:9998/users/photo/${userId}`;
    },
    
    handlePhotoError(userId) {
      this.$set(this.photoErrors, userId, true);
    },
    
    getRoleClass(role) {
      const roleClasses = {
        'ADMIN': 'role-admin',
        'MANAGER': 'role-manager',
        'USER': 'role-user',
      };
      return roleClasses[role] || 'role-user';
    }
  },
  mounted() {
    this.getDepartment();
  },
};
</script>

<style scoped>
/* Department Hero Section */
.department-hero {
  height: 60vh;
  min-height: 400px;
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  color: white;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.2);
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
}

.department-icon {
  font-size: 64px;
  margin-bottom: 24px;
  color: rgba(255, 255, 255, 0.9);
}

.department-title {
  font-size: 48px;
  font-weight: 800;
  margin-bottom: 16px;
  color: white;
}

.department-description {
  font-size: 20px;
  margin-bottom: 32px;
  opacity: 0.9;
  line-height: 1.6;
}

.department-meta {
  display: flex;
  justify-content: center;
  gap: 32px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

.meta-item i {
  font-size: 20px;
  margin-right: 8px;
}

/* Main Content */
.main-content {
  background-color: var(--airbnb-bg);
  min-height: 100vh;
  padding: 64px 0;
}

/* Section Header */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  flex-wrap: wrap;
  gap: 16px;
}

.section-header h2 {
  font-size: 32px;
  font-weight: 700;
  margin: 0;
  color: var(--airbnb-dark);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* Stats Section */
.stats-section {
  margin-bottom: 64px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
}

.stat-card {
  background: white;
  padding: 32px;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  transition: var(--transition);
  display: flex;
  align-items: center;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.12);
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.stat-icon.primary { background: linear-gradient(135deg, var(--airbnb-primary), #ff7675); }
.stat-icon.success { background: linear-gradient(135deg, #00b894, #00cec9); }
.stat-icon.warning { background: linear-gradient(135deg, #fdcb6e, #e17055); }
.stat-icon.info { background: linear-gradient(135deg, #74b9ff, #0984e3); }

.stat-content h3 {
  font-size: 32px;
  font-weight: 800;
  margin: 0 0 4px 0;
  color: var(--airbnb-dark);
}

.stat-content p {
  font-size: 16px;
  margin: 0 0 8px 0;
  color: var(--airbnb-light);
}

.stat-change {
  font-size: 14px;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: 12px;
  display: inline-block;
}

.stat-change.positive {
  background: rgba(0, 184, 148, 0.1);
  color: #00b894;
}

.stat-change.neutral {
  background: rgba(116, 185, 255, 0.1);
  color: #74b9ff;
}

/* Search and Filter */
.search-filter {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-input {
  padding: 12px 16px;
  border: 1px solid #DDDDDD;
  border-radius: var(--border-radius);
  font-size: 16px;
  width: 250px;
  transition: var(--transition);
}

.search-input:focus {
  outline: none;
  border-color: var(--airbnb-primary);
  box-shadow: 0 0 0 3px rgba(255,56,92,0.1);
}

.role-filter {
  padding: 12px 16px;
  border: 1px solid #DDDDDD;
  border-radius: var(--border-radius);
  font-size: 16px;
  background: white;
  cursor: pointer;
}

/* Team Grid */
.team-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 28px;
}

.team-member-card {
  background: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  overflow: hidden;
  transition: var(--transition);
  display: flex;
  flex-direction: column;
}

.team-member-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.12);
}

.member-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 24px 24px 0 24px;
  margin-bottom: 20px;
}

.member-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--airbnb-primary) 0%, #FF7E82 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 700;
  box-shadow: 0 4px 12px rgba(255, 56, 92, 0.25);
  overflow: hidden;
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.member-status {
  display: flex;
  align-items: center;
  padding: 6px 12px;
  background-color: rgba(240, 242, 245, 0.8);
  border-radius: 20px;
}

.status-indicator {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-right: 6px;
  background-color: #44CC11;
}

.status-text {
  font-size: 14px;
  color: var(--airbnb-dark);
  font-weight: 500;
}

.member-info {
  padding: 0 24px;
  flex: 1;
}

.member-name {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 8px;
  color: var(--airbnb-dark);
}

.member-email {
  color: var(--airbnb-light);
  font-size: 14px;
  margin-bottom: 16px;
}

.member-details {
  margin-bottom: 16px;
}

.detail-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.detail-item i {
  font-size: 16px;
  color: var(--airbnb-light);
  margin-right: 8px;
  width: 20px;
}

.role-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
}

.role-admin {
  background: rgba(220, 38, 127, 0.1);
  color: #dc267f;
}

.role-manager {
  background: rgba(255, 193, 7, 0.1);
  color: #ff8f00;
}

.role-user {
  background: rgba(116, 185, 255, 0.1);
  color: #74b9ff;
}

.member-actions {
  padding: 16px 24px;
  border-top: 1px solid #EBEBEB;
  display: flex;
  gap: 8px;
}

.btn-sm {
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 8px;
}

.btn-outline-secondary {
  color: var(--airbnb-light);
  border-color: #DDDDDD;
  padding: 8px 12px;
}

.btn-outline-secondary:hover {
  background-color: var(--airbnb-bg);
  color: var(--airbnb-dark);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 80px 20px;
  color: var(--airbnb-light);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 24px;
  opacity: 0.5;
}

.empty-state h3 {
  font-size: 24px;
  margin-bottom: 12px;
  color: var(--airbnb-dark);
}

.empty-state p {
  font-size: 16px;
  max-width: 400px;
  margin: 0 auto;
}

/* Responsive Design */
@media (max-width: 768px) {
  .department-title {
    font-size: 36px;
  }
  
  .department-meta {
    flex-direction: column;
    gap: 16px;
  }
  
  .section-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .search-filter {
    flex-direction: column;
    width: 100%;
  }
  
  .search-input {
    width: 100%;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .team-grid {
    grid-template-columns: 1fr;
  }
}
</style>
