<!-- CalendarView.vue -->

<template>
  <div>
    <h2>User Vacations</h2>
    <ul>
      <li v-for="vacation in vacations" :key="vacation.id">
        User {{ vacation.userId }}, ({{ vacation.startDate }} -
        {{ vacation.endDate }})
      </li>
    </ul>

    <router-link
          id="add-department"
          :to="{ name: 'AddVacation' }"
          v-show="$route.name == 'AddVacation'"
        >
          <button class="btn">Add new Vacation</button>
        </router-link>

    <FullCalendar
      ref="fullCalendar"
      class="calendar"
      :events="calendarEvents"
      :header="header"
      :plugins="plugins"
      :editable="editable"
      @eventClick="handleEventClick"
    />

    <!-- Bootstrap Vue modal -->
    <!-- <b-modal
      v-if="selectedVacation"
      title="Vacation Details"
      @hide="clearSelectedVacation"
    >
      <p>Destination: {{ selectedVacation.destination }}</p>
      <p>Start Date: {{ selectedVacation.startDate }}</p>
      <p>End Date: {{ selectedVacation.endDate }}</p>
    </b-modal> -->

  </div>
</template>

<script>
import FullCalendar from "vue-fullcalendar";
import axios from "axios";
//import "fullcalendar/dist/fullcalendar.css";
//import { BModal } from "bootstrap-vue";

export default {
  components: {
    FullCalendar,
    //BModal,
  },
  data() {
    return {
      vacations: [],
      vacation: null,
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
        vacationDetails: vacation, // Store the full vacation details in the event,
        color: this.getColorForUser(vacation.userId),
      }));
    },
  },

  methods: {
    //this.fetchUserVacations("exampleUser");
    fetchUserVacations() {
      //console.log(">>> (fetchUserVacations) userId = " + userId);
      axios
        .get(`http://localhost:9998/vacation/`)
        .then((response) => {
          this.vacations = response.data;
          console.log(
            ">>> (fetchUserVacations) this.vacations = " +
              JSON.stringify(this.vacations)
          );
        })
        .catch((error) => {
          console.error("Error fetching vacations:", error);
        });
    },
    handleEventClick(event) {
      console.log("Event Clicked:", event);
      this.selectedVacation = event.vacationDetails;
      console.log(
        ">>> this.selectedVacation  = " + JSON.stringify(this.selectedVacation)
      );
      // Handle event click logic
    },
    showVacationDetails({ event }) {
      this.selectedVacation = event.vacationDetails;
    },
    clearSelectedVacation() {
      this.selectedVacation = null;
    },
    getColorForUser(userId) {
      console.log("(getColorForUser) userId = " + userId);
      // Replace this logic with your own color assignment logic based on the userId
      // For example, you can use a switch statement, or generate colors dynamically
      switch (userId) {
        case 1:
          return "red";
        case 2:
          return "blue";
        // Add more cases as needed
        default:
          return "green";
      }
    },
  },
  mounted() {
    this.fetchUserVacations("1"); // Replace with the actual username
  },
};
</script>

<style scoped>
.calendar {
  max-width: 800px;
  margin: 0 auto;
}
</style>
