<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Ticket List</h1>
        <h5>{{ msg }}</h5>
      </div>
    </div>

    <div class="row">
      <!-- <img
            v-show="len == 0"
            class="img-fluid"
            src="../../assets/sorry.jpg"
            alt="Sorry"
          /> -->
      <div
        v-for="ticket of tickets"
        :key="ticket.id"
        class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex"
      >
        <TicketBox :tickets="tickets"> </TicketBox>
      </div>
    </div>
  </div>
</template>
    
    <script>
import TicketBox from "../../components/Ticket/TicketBox";
var axios = require("axios");
export default {
  name: "ListTicket",
  data() {
    return {
      id: null,
      tickets: [],
    };
  },
  components: { TicketBox },
  props: ["baseURL"],
  methods: {
    async fetchData() {
      // fetch users
      await axios
        .get("http://localhost:9998/" + "ticket/")
        .then((res) => {
          this.tickets = res.data;
          console.log(
            ">>> (fetchData) this.tickets = " + JSON.stringify(this.tickets)
          );
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.fetchData();
  },
};
</script>
    
    <style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

h5 {
  font-family: "Roboto", sans-serif;
  color: #686868;
  font-weight: 300;
}
</style>
    