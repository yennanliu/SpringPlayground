<template>
  <div class="vacation-container">
    <div class="header-section">
      <h2 class="page-title">User Vacations</h2>
    </div>

    <div class="calendar-section">
      <FullCalendar
        ref="fullCalendar"
        class="calendar"
        :events="calendarEvents"
        :header="header"
        :plugins="plugins"
        :editable="editable"
        @eventClick="handleEventClick"
        :event-render="eventRender"
      >
        <!-- Use the eventContent slot to customize the content of each event -->
        <template v-slot:eventContent="slotProps">
          <div class="event-content">
            <i class="fas fa-plane event-icon"></i>
            <span class="event-text">
              {{ slotProps.event.extendedProps.destination || 'Vacation' }}
            </span>
          </div>
        </template>
      </FullCalendar>
    </div>

    <!-- Improved Bootstrap Vue modal -->
    <b-modal
      v-model="showModal"
      title="Vacation Details"
      @hide="clearSelectedVacation"
      size="md"
      centered
    >
      <div v-if="selectedVacation" class="vacation-details">
        <div class="detail-row">
          <strong>User ID:</strong>
          <span>{{ selectedVacation.userId }}</span>
        </div>
        <div class="detail-row">
          <strong>Destination:</strong>
          <span>{{ selectedVacation.destination || 'Not specified' }}</span>
        </div>
        <div class="detail-row">
          <strong>Start Date:</strong>
          <span>{{ formatDate(selectedVacation.startDate) }}</span>
        </div>
        <div class="detail-row">
          <strong>End Date:</strong>
          <span>{{ formatDate(selectedVacation.endDate) }}</span>
        </div>
        <div class="detail-row">
          <strong>Status:</strong>
          <span class="status-badge" :class="getStatusClass(selectedVacation.status)">
            {{ selectedVacation.status }}
          </span>
        </div>
        <div class="detail-row" v-if="selectedVacation.type">
          <strong>Type:</strong>
          <span>{{ selectedVacation.type }}</span>
        </div>
      </div>
      
      <template #modal-footer="{ hide }">
        <b-button variant="secondary" @click="hide()">Close</b-button>
      </template>
    </b-modal>

    <div class="table-section">
      <div class="table-header">
        <h2 class="section-title">Vacation List</h2>
        <div class="table-controls">
          <div class="search-box">
            <i class="fas fa-search search-icon"></i>
            <input 
              type="text" 
              v-model="searchQuery" 
              placeholder="Search vacations..." 
              class="search-input"
            />
          </div>
          <div class="items-per-page">
            <label>Show:</label>
            <select v-model="itemsPerPage" @change="currentPage = 1" class="page-select">
              <option value="5">5</option>
              <option value="10">10</option>
              <option value="20">20</option>
              <option value="50">50</option>
            </select>
          </div>
        </div>
      </div>
      
      <div class="table-responsive">
        <table class="table table-hover vacation-table">
          <thead>
            <tr>
              <th @click="sortBy('userId')" class="sortable">
                User ID
                <i class="fas fa-sort sort-icon" :class="getSortIcon('userId')"></i>
              </th>
              <th @click="sortBy('destination')" class="sortable">
                Destination
                <i class="fas fa-sort sort-icon" :class="getSortIcon('destination')"></i>
              </th>
              <th @click="sortBy('startDate')" class="sortable">
                Start Date
                <i class="fas fa-sort sort-icon" :class="getSortIcon('startDate')"></i>
              </th>
              <th @click="sortBy('endDate')" class="sortable">
                End Date
                <i class="fas fa-sort sort-icon" :class="getSortIcon('endDate')"></i>
              </th>
              <th @click="sortBy('status')" class="sortable">
                Status
                <i class="fas fa-sort sort-icon" :class="getSortIcon('status')"></i>
              </th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="vacation in paginatedVacations" :key="vacation.id" @click="selectVacation(vacation)" class="vacation-row">
              <td>
                <div class="user-cell">
                  <div class="user-avatar">
                    <i class="fas fa-user"></i>
                  </div>
                  <span class="user-id">{{ vacation.userId }}</span>
                </div>
              </td>
              <td>
                <div class="destination-cell">
                  <i class="fas fa-map-marker-alt destination-icon"></i>
                  <span>{{ vacation.destination || 'Not specified' }}</span>
                </div>
              </td>
              <td>
                <div class="date-cell">
                  <i class="fas fa-calendar-alt date-icon"></i>
                  <span>{{ formatDate(vacation.startDate) }}</span>
                </div>
              </td>
              <td>
                <div class="date-cell">
                  <i class="fas fa-calendar-check date-icon"></i>
                  <span>{{ formatDate(vacation.endDate) }}</span>
                </div>
              </td>
              <td>
                <span class="status-badge" :class="getStatusClass(vacation.status)">
                  <i class="fas" :class="getStatusIcon(vacation.status)"></i>
                  {{ vacation.status }}
                </span>
              </td>
              <td>
                <button class="btn btn-info btn-sm action-btn" @click.stop="selectVacation(vacation)">
                  <i class="fas fa-eye"></i>
                  View Details
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- Pagination Controls -->
      <div class="pagination-section" v-if="totalPages > 1">
        <div class="pagination-info">
          Showing {{ startItem }} to {{ endItem }} of {{ filteredVacations.length }} entries
        </div>
        <nav class="pagination-nav">
          <ul class="pagination">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <button class="page-link" @click="goToPage(1)" :disabled="currentPage === 1">
                <i class="fas fa-angle-double-left"></i>
              </button>
            </li>
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <button class="page-link" @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">
                <i class="fas fa-angle-left"></i>
              </button>
            </li>
            
            <li 
              v-for="page in visiblePages" 
              :key="page" 
              class="page-item" 
              :class="{ active: page === currentPage }"
            >
              <button class="page-link" @click="goToPage(page)">
                {{ page }}
              </button>
            </li>
            
            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
              <button class="page-link" @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages">
                <i class="fas fa-angle-right"></i>
              </button>
            </li>
            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
              <button class="page-link" @click="goToPage(totalPages)" :disabled="currentPage === totalPages">
                <i class="fas fa-angle-double-right"></i>
              </button>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>
import FullCalendar from "vue-fullcalendar";
import axios from "axios";

export default {
  components: {
    FullCalendar,
  },
  data() {
    return {
      vacations: [],
      selectedVacation: null,
      showModal: false,
      searchQuery: '',
      currentPage: 1,
      itemsPerPage: 10,
      sortField: '',
      sortDirection: 'asc',
      header: {
        left: "prev,next today",
        center: "title",
        right: "month,agendaWeek,agendaDay",
      },
      plugins: ["interaction", "dayGrid", "timeGrid"],
      editable: false,
    };
  },

  computed: {
    calendarEvents() {
      return this.vacations.map((vacation) => ({
        id: vacation.id,
        title: vacation.destination || `Vacation - ${vacation.userId}`,
        start: vacation.startDate,
        end: vacation.endDate,
        backgroundColor: this.getStatusColor(vacation.status),
        borderColor: this.getStatusColor(vacation.status),
        textColor: '#ffffff',
        extendedProps: {
          userId: vacation.userId,
          destination: vacation.destination,
          type: vacation.type,
          status: vacation.status,
          vacationDetails: vacation,
        },
      }));
    },
    
    filteredVacations() {
      let filtered = [...this.vacations];
      
      // Apply search filter
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        filtered = filtered.filter(vacation => 
          vacation.userId.toString().toLowerCase().includes(query) ||
          (vacation.destination && vacation.destination.toLowerCase().includes(query)) ||
          vacation.status.toLowerCase().includes(query) ||
          this.formatDate(vacation.startDate).toLowerCase().includes(query) ||
          this.formatDate(vacation.endDate).toLowerCase().includes(query)
        );
      }
      
      // Apply sorting
      if (this.sortField) {
        filtered.sort((a, b) => {
          let aVal = a[this.sortField];
          let bVal = b[this.sortField];
          
          // Handle null/undefined values
          if (!aVal) aVal = '';
          if (!bVal) bVal = '';
          
          // Convert to string for comparison
          aVal = aVal.toString().toLowerCase();
          bVal = bVal.toString().toLowerCase();
          
          if (this.sortDirection === 'asc') {
            return aVal.localeCompare(bVal);
          } else {
            return bVal.localeCompare(aVal);
          }
        });
      }
      
      return filtered;
    },
    
    paginatedVacations() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredVacations.slice(start, end);
    },
    
    totalPages() {
      return Math.ceil(this.filteredVacations.length / this.itemsPerPage);
    },
    
    startItem() {
      return (this.currentPage - 1) * this.itemsPerPage + 1;
    },
    
    endItem() {
      const end = this.currentPage * this.itemsPerPage;
      return Math.min(end, this.filteredVacations.length);
    },
    
    visiblePages() {
      const pages = [];
      const total = this.totalPages;
      const current = this.currentPage;
      
      if (total <= 7) {
        for (let i = 1; i <= total; i++) {
          pages.push(i);
        }
      } else {
        if (current <= 4) {
          for (let i = 1; i <= 5; i++) {
            pages.push(i);
          }
          pages.push('...');
          pages.push(total);
        } else if (current >= total - 3) {
          pages.push(1);
          pages.push('...');
          for (let i = total - 4; i <= total; i++) {
            pages.push(i);
          }
        } else {
          pages.push(1);
          pages.push('...');
          for (let i = current - 1; i <= current + 1; i++) {
            pages.push(i);
          }
          pages.push('...');
          pages.push(total);
        }
      }
      
      return pages;
    },
  },

  methods: {
    fetchUserVacations() {
      axios
        .get(`http://localhost:9998/vacation/`)
        .then((response) => {
          this.vacations = response.data;
          console.log('Fetched vacations:', this.vacations);
        })
        .catch((error) => {
          console.error("Error fetching vacations:", error);
        });
    },
    
    handleEventClick(info) {
      console.log('Event clicked:', info);
      this.selectedVacation = info.event.extendedProps.vacationDetails;
      this.showModal = true;
    },
    
    selectVacation(vacation) {
      this.selectedVacation = vacation;
      this.showModal = true;
    },
    
    clearSelectedVacation() {
      this.selectedVacation = null;
      this.showModal = false;
    },
    
    getStatusColor(status) {
      switch (status) {
        case "PENDING":
          return "#f39c12"; // Orange color for Pending status
        case "APPROVED":
          return "#27ae60"; // Green color for Approved status
        case "REJECTED":
          return "#e74c3c"; // Red color for Rejected status
        default:
          return "#95a5a6"; // Gray color for default
      }
    },
    
    getStatusClass(status) {
      switch (status) {
        case "PENDING":
          return "status-pending";
        case "APPROVED":
          return "status-approved";
        case "REJECTED":
          return "status-rejected";
        default:
          return "status-default";
      }
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
    
    eventRender(info) {
      // Additional event rendering customization if needed
      info.el.style.cursor = 'pointer';
    },
    
    sortBy(field) {
      if (this.sortField === field) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortField = field;
        this.sortDirection = 'asc';
      }
      this.currentPage = 1;
    },
    
    getSortIcon(field) {
      if (this.sortField !== field) {
        return '';
      }
      return this.sortDirection === 'asc' ? 'fa-sort-up' : 'fa-sort-down';
    },
    
    getStatusIcon(status) {
      switch (status) {
        case "PENDING":
          return "fa-clock";
        case "APPROVED":
          return "fa-check-circle";
        case "REJECTED":
          return "fa-times-circle";
        default:
          return "fa-question-circle";
      }
    },
    
    goToPage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },
  },
  
  watch: {
    searchQuery() {
      this.currentPage = 1;
    },
  },
  
  mounted() {
    this.fetchUserVacations();
  },
};
</script>

<style scoped>
.vacation-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header-section {
  text-align: center;
  margin-bottom: 30px;
}

.page-title {
  font-family: "Roboto", sans-serif;
  color: #2c3e50;
  font-weight: 700;
  margin-bottom: 1rem;
}

.section-title {
  font-family: "Roboto", sans-serif;
  color: #2c3e50;
  font-weight: 600;
  margin-bottom: 20px;
  font-size: 1.5rem;
}

.calendar-section {
  margin-bottom: 40px;
}

.calendar {
  max-width: 100%;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.event-content {
  display: flex;
  align-items: center;
  padding: 2px 4px;
}

.event-icon {
  margin-right: 4px;
  font-size: 12px;
}

.event-text {
  font-size: 12px;
  font-weight: 500;
}

.table-section {
  margin-top: 40px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 15px;
}

.table-controls {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 12px;
  color: #7f8c8d;
  z-index: 1;
}

.search-input {
  padding: 8px 12px 8px 35px;
  border: 2px solid #e9ecef;
  border-radius: 25px;
  font-size: 14px;
  width: 250px;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.items-per-page {
  display: flex;
  align-items: center;
  gap: 8px;
}

.items-per-page label {
  font-size: 14px;
  color: #2c3e50;
  font-weight: 500;
}

.page-select {
  padding: 6px 12px;
  border: 2px solid #e9ecef;
  border-radius: 6px;
  font-size: 14px;
  background-color: white;
  cursor: pointer;
  transition: border-color 0.3s ease;
}

.page-select:focus {
  outline: none;
  border-color: #3498db;
}

.vacation-table {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.vacation-table thead th {
  background-color: #f8f9fa;
  border-bottom: 2px solid #dee2e6;
  color: #2c3e50;
  font-weight: 600;
  padding: 1rem;
  position: relative;
}

.sortable {
  cursor: pointer;
  user-select: none;
  transition: background-color 0.2s ease;
}

.sortable:hover {
  background-color: #e9ecef;
}

.sort-icon {
  margin-left: 8px;
  font-size: 12px;
  opacity: 0.6;
  transition: opacity 0.2s ease;
}

.sortable:hover .sort-icon {
  opacity: 1;
}

.fa-sort-up,
.fa-sort-down {
  opacity: 1;
  color: #3498db;
}

.vacation-table tbody td {
  padding: 1rem;
  vertical-align: middle;
}

.vacation-row {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.vacation-row:hover {
  background-color: #f8f9fa;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
}

.status-pending {
  background-color: #f39c12;
  color: white;
}

.status-approved {
  background-color: #27ae60;
  color: white;
}

.status-rejected {
  background-color: #e74c3c;
  color: white;
}

.status-default {
  background-color: #95a5a6;
  color: white;
}

.user-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #3498db;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 14px;
}

.user-id {
  font-weight: 500;
  color: #2c3e50;
}

.destination-cell,
.date-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.destination-icon,
.date-icon {
  color: #7f8c8d;
  font-size: 14px;
  width: 16px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  padding: 6px 12px;
}

.vacation-details {
  padding: 10px 0;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-row strong {
  color: #2c3e50;
  min-width: 120px;
}

.btn-info {
  background-color: #3498db;
  border-color: #3498db;
  transition: all 0.2s ease;
}

.btn-info:hover {
  background-color: #2980b9;
  border-color: #2980b9;
}

.table-responsive {
  border-radius: 8px;
  overflow: hidden;
}

/* FullCalendar custom styles */
.fc-event {
  cursor: pointer !important;
}

.fc-event:hover {
  opacity: 0.8;
}

/* Pagination Styles */
.pagination-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding: 20px 0;
  border-top: 1px solid #e9ecef;
  flex-wrap: wrap;
  gap: 15px;
}

.pagination-info {
  color: #7f8c8d;
  font-size: 14px;
}

.pagination-nav .pagination {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 4px;
}

.page-item {
  display: flex;
}

.page-link {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 12px;
  border: 1px solid #dee2e6;
  background-color: white;
  color: #3498db;
  text-decoration: none;
  border-radius: 6px;
  font-size: 14px;
  min-width: 40px;
  height: 40px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.page-link:hover:not(:disabled) {
  background-color: #3498db;
  color: white;
  border-color: #3498db;
}

.page-item.active .page-link {
  background-color: #3498db;
  color: white;
  border-color: #3498db;
  font-weight: 600;
}

.page-item.disabled .page-link {
  color: #6c757d;
  background-color: #f8f9fa;
  border-color: #dee2e6;
  cursor: not-allowed;
  opacity: 0.6;
}

.page-link:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

/* Responsive Design */
@media (max-width: 768px) {
  .table-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .table-controls {
    justify-content: space-between;
  }
  
  .search-input {
    width: 200px;
  }
  
  .pagination-section {
    flex-direction: column;
    text-align: center;
  }
  
  .pagination-nav .pagination {
    justify-content: center;
    flex-wrap: wrap;
  }
}
</style>