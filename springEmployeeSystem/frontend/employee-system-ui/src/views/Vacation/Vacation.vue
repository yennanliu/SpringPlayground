<!-- CalendarView.vue -->

<template>
  <div>
    <FullCalendar
      ref="fullCalendar"
      class="calendar"
      :events="events"
      :header="header"
      :plugins="plugins"
      :editable="editable"
      @eventClick="handleEventClick"
    />
  </div>
</template>

<script>
import FullCalendar from "vue-fullcalendar";
//import "fullcalendar/dist/fullcalendar.css";

export default {
  components: {
    FullCalendar,
  },
  data() {
    return {
      events: [
        {
          title: "Event 1",
          start: "2023-01-01",
        },
        {
          title: "Event 2",
          start: "2023-01-05",
        },
      ],
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
    handleEventClick(event) {
      console.log("Event Clicked:", event);
      // Handle event click logic
    },
  },
};
</script>

<style scoped>
.calendar {
  max-width: 800px;
  margin: 0 auto;
}
</style>
