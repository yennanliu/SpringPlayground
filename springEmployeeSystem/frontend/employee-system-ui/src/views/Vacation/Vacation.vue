<!-- CalendarView.vue -->

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
    </b-modal>

    <h2>Vacation List</h2>
    <table class="table">
      <thead>
        <tr>
          <th>User ID</th>
          <th>Duration</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="vacation in vacations" :key="vacation.id">
          <td>{{ vacation.userId }}</td>
          <td>{{ vacation.startDate }} {{ vacation.endDate }}</td>
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
        userId: vacation.userId,
        type: vacation.type,
        status: vacation.status,
        start: vacation.startDate,
        end: vacation.endDate,
        vacationDetails: vacation, // Store the full vacation details in the event
        color: this.getColorForUser(vacation.userId),
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
      this.selectedVacation = event.vacationDetails;
    },
    clearSelectedVacation() {
      this.selectedVacation = null;
    },
    getColorForUser(userId) {
      switch (userId) {
        case 1:
          return "red";
        case 2:
          return "blue";
        default:
          return "green";
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
</style>
