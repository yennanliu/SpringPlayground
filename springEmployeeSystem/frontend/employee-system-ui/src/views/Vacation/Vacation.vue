<template>
  <div class="vacation-container">
    <h2>User Vacations</h2>

    <FullCalendar
      ref="fullCalendar"
      class="calendar"
      :events="calendarEvents"
      :header="header"
      :plugins="plugins"
      :editable="editable"
      @eventClick="handleEventClick"
    >
      <!-- Use the eventContent slot to customize the content of each event -->
      <template v-slot:eventContent="slotProps">
        <div>
          <span v-if="slotProps.event.extendedProps.vacationDetails">
            {{ slotProps.event.extendedProps.vacationDetails.destination }}
          </span>
          {{ slotProps.timeText }}
        </div>
      </template>
    </FullCalendar>

    <!-- Bootstrap Vue modal -->
    <b-modal
      v-if="selectedVacation"
      title="Vacation Details"
      @hide="clearSelectedVacation"
    >
      <p>Destination: {{ selectedVacation.destination }}</p>
      <p>Start Date: {{ selectedVacation.startDate }}</p>
      <p>End Date: {{ selectedVacation.endDate }}</p>
      <p>Status: {{ selectedVacation.status }}</p>
    </b-modal>

    <h2>Vacation List</h2>
    <div class="vacation-list">
      <div 
        v-for="(vacation, index) in vacations" 
        :key="index"
        class="vacation-bar"
        @click="selectVacation(vacation)"
      >
        <div class="vacation-period">{{ formatDate(vacation.startDate) }} - {{ formatDate(vacation.endDate) }}</div>
        <div class="vacation-type">{{ vacation.type }}</div>
        <div class="vacation-status" :class="vacation.status.toLowerCase()">{{ vacation.status }}</div>
      </div>
    </div>

    <div v-if="selectedVacation" class="vacation-details">
      <h3>Vacation Details</h3>
      <div class="detail-row">
        <span class="label">Period:</span>
        <span class="value">{{ formatDate(selectedVacation.startDate) }} - {{ formatDate(selectedVacation.endDate) }}</span>
      </div>
      <div class="detail-row">
        <span class="label">Duration:</span>
        <span class="value">{{ calculateDays(selectedVacation.startDate, selectedVacation.endDate) }} days</span>
      </div>
      <div class="detail-row">
        <span class="label">Type:</span>
        <span class="value">{{ selectedVacation.type }}</span>
      </div>
      <div class="detail-row">
        <span class="label">Status:</span>
        <span class="value" :class="selectedVacation.status.toLowerCase()">{{ selectedVacation.status }}</span>
      </div>
      <div class="detail-row">
        <span class="label">Comments:</span>
        <span class="value">{{ selectedVacation.comments || 'None' }}</span>
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
      header: {
        left: "prev,next today",
        center: "title",
        right: "month,agendaWeek,agendaDay",
      },
      plugins: ["interaction", "dayGrid", "timeGrid"],
      editable: true,
      loading: false,
      error: null
    };
  },

  computed: {
    calendarEvents() {
      return this.vacations.map((vacation) => ({
        id: vacation.id, // Ensure each event has a unique identifier
        userId: vacation.userId,
        type: vacation.type,
        status: vacation.status,
        start: vacation.startDate,
        end: vacation.endDate,
        vacationDetails: vacation, // Store the full vacation details in the event
        color: this.getStatusColor(vacation.status),
      }));
    },
  },

  methods: {
    fetchUserVacations() {
      axios
        .get(`http://localhost:9998/vacation/`)
        .then((response) => {
          this.vacations = response.data;
        })
        .catch((error) => {
          console.error("Error fetching vacations:", error);
        });
    },
    handleEventClick(event) {
      console.log('>>> Clicked Event:', event);
      this.selectedVacation = event.vacationDetails; //event.extendedProps.vacationDetails;
    },
    clearSelectedVacation() {
      this.selectedVacation = null;
    },
    getStatusColor(status) {
      switch (status) {
        case "PENDING":
          return "#f0ad4e"; // Orange color for Pending status
        case "APPROVED":
          return "#5cb85c"; // Green color for Approved status
        case "REJECTED":
          return "#d9534f"; // Red color for Rejected status
        default:
          return "white"; // Default color
      }
    },
    async fetchVacationData() {
      this.loading = true;
      this.error = null;
      try {
        // Replace with your actual API endpoint
        const response = await this.$http.get('/api/employee/vacations');
        this.vacations = response.data;
      } catch (err) {
        console.error('Failed to fetch vacation data:', err);
        this.error = 'Failed to load vacation data. Please try again later.';
      } finally {
        this.loading = false;
      }
    },
    selectVacation(vacation) {
      this.selectedVacation = vacation;
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString();
    },
    calculateDays(startDate, endDate) {
      if (!startDate || !endDate) return 0;
      const start = new Date(startDate);
      const end = new Date(endDate);
      const diffTime = Math.abs(end - start);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1; // +1 to include both start and end days
      return diffDays;
    }
  },
  mounted() {
    this.fetchUserVacations();
  },
  created() {
    this.fetchVacationData();
  },
};
</script>

<style scoped>
.calendar {
  max-width: 800px;
  margin: 0 auto;
}

.table tbody tr {
  transition: background-color 0.3s;
}

.table tbody tr:hover {
  background-color: #f5f5f5;
}

.vacation-list {
  margin: 20px 0;
}

.vacation-bar {
  display: flex;
  justify-content: space-between;
  padding: 12px 16px;
  margin-bottom: 8px;
  background-color: #f5f5f5;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.vacation-bar:hover {
  background-color: #e0e0e0;
}

.vacation-period {
  font-weight: 500;
}

.vacation-status {
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 0.9em;
}

.vacation-status.approved {
  background-color: #c8e6c9;
  color: #2e7d32;
}

.vacation-status.pending {
  background-color: #fff9c4;
  color: #f57f17;
}

.vacation-status.rejected {
  background-color: #ffcdd2;
  color: #c62828;
}

.vacation-details {
  margin-top: 20px;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 4px;
  border-left: 4px solid #3f51b5;
}

.detail-row {
  margin-bottom: 10px;
  display: flex;
  align-items: flex-start;
}

.detail-row .label {
  width: 100px;
  font-weight: 500;
  color: #555;
}

.detail-row .value {
  flex: 1;
}
</style>