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
      <h2 class="section-title">Vacation List</h2>
      <div class="table-responsive">
        <table class="table table-hover vacation-table">
          <thead>
            <tr>
              <th>User ID</th>
              <th>Destination</th>
              <th>Start Date</th>
              <th>End Date</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="vacation in vacations" :key="vacation.id" @click="selectVacation(vacation)" class="vacation-row">
              <td>{{ vacation.userId }}</td>
              <td>{{ vacation.destination || 'Not specified' }}</td>
              <td>{{ formatDate(vacation.startDate) }}</td>
              <td>{{ formatDate(vacation.endDate) }}</td>
              <td>
                <span class="status-badge" :class="getStatusClass(vacation.status)">
                  {{ vacation.status }}
                </span>
              </td>
              <td>
                <button class="btn btn-info btn-sm" @click.stop="selectVacation(vacation)">
                  View Details
                </button>
              </td>
            </tr>
          </tbody>
        </table>
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
</style>