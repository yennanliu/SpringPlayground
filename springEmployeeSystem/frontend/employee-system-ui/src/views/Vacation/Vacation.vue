<!-- CalendarView.vue -->

<template>
  <div>
    <h2>User Vacations</h2>
    <ul>
      <li v-for="vacation in vacations" :key="vacation.id">
        {{ vacation.destination }} ({{ vacation.startDate }} -
        {{ vacation.endDate }})
      </li>
    </ul>

    <FullCalendar
      ref="fullCalendar"
      class="calendar"
      :events="vacations"
      :header="header"
      :plugins="plugins"
      :editable="editable"
      @eventClick="handleEventClick"
    />
  </div>
</template>

<script>
import FullCalendar from "vue-fullcalendar";
import axios from "axios";
//import "fullcalendar/dist/fullcalendar.css";

export default {
  components: {
    FullCalendar,
  },
  data() {
    return {
      vacations: [],
      vacation: null,
      header: {
        left: "prev,next today",
        center: "title",
        right: "month,agendaWeek,agendaDay",
      },
      plugins: ["interaction", "dayGrid", "timeGrid"],
      editable: true,
    };
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
      // Handle event click logic
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
