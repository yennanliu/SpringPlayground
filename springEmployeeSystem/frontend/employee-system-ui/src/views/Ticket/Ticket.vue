<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Tickets</h4>
        <router-link
          id="add-department"
          :to="{ name: 'AddTicket' }"
          v-show="$route.name == 'AdminTicket'"
        >
          <button class="btn">Add a new Ticket</button>
        </router-link>
      </div>
    </div>
    <div class="row">
      <div
        v-for="ticket of tickets"
        :key="ticket.id"
        class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex"
      >
        <TicketBox :ticket="ticket"> </TicketBox>
      </div>
    </div>
  </div>
</template>
  
  <script>
// https://youtu.be/VZ1NV7EHGJw?si=JPmnA7oQoVdPJwAL&t=1450
// https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Product/Product.vue

import TicketBox from "../../components/Ticket/TicketBox";
var axios = require("axios");

export default {
  name: "Ticket",
  components: { TicketBox },
  props: ["baseURL"],

  data() {
    return {
      //baseURL: "http://localhost:9999/", // NOTE !! we read baseURL from App.vue
      tickets: [],
    };
  },
  methods: {
    async getTickets() {
      await axios
        .get("http://localhost:9998/ticket/")
        .then((res) => {
            this.tickets = res.data;
            console.log(">>> this.tickets = " + JSON.stringify(this.tickets));
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.getTickets();
  },
};
</script>
  
  <style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

#add-user {
  float: right;
  font-weight: 500;
}
</style>