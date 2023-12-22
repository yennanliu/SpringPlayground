<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <div class="col-md-6 col-12 pt-3 pt-md-0">
        <h1>Ticket List</h1>
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Subject</th>
              <th>User ID</th>
              <th>Assigned To</th>
              <th>Status</th>
              <th>tag</th>
              <th>Detail</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="ticket in this.tickets" :key="ticket.id">
              <td>{{ ticket.id }}</td>
              <td>{{ ticket.subject }}</td>
              <td>
                <router-link :to="`/users/show/${ticket.userId}`">
                    {{ ticket.userId }}
                </router-link>
              </td>
              <td>
                <router-link :to="`/users/show/${ticket.assignedTo}`">
                    {{ ticket.assignedTo }}
                </router-link>
              </td>
              <td>{{ ticket.status }}</td>
              <td>{{ ticket.tag }}</td>
              <td>
                <router-link :to="`/tickets/show/${ticket.id}`">
                  Ticket Detail
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="col-md-1"></div>
    </div>
  </div>
</template>
  
  <script>
// https://youtu.be/VZ1NV7EHGJw?si=JPmnA7oQoVdPJwAL&t=1450
// https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Product/Product.vue

//import TicketBox from "../../components/Ticket/TicketBox";
var axios = require("axios");

export default {
  name: "Ticket",
  //components: { TicketBox },
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