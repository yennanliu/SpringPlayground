<template>
  <div>
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
    <table class="table">
      <thead>
        <tr>
          <th>User ID</th>
          <th>Start Date</th>
          <th>End Date</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="vacation in vacations" :key="vacation.id" :style="{ backgroundColor: getStatusColor(vacation.status) }">
          <td>{{ vacation.userId }}</td>
          <td>{{ vacation.startDate }}</td>
          <td>{{ vacation.endDate }}</td>
          <td>{{ vacation.status }}</td>
        </tr>
      </tbody>
    </table>
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
  },
  mounted() {
    this.fetchUserVacations();
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
</style>