<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <div class="col-md-10 col-12">
        <h1 class="font-weight-bold">Ticket: {{ ticket.id }}</h1>
        <h2 class="font-weight-bold">Subject: {{ ticket.subject }}</h2>

        <h2 class="font-weight-bold mt-3">Details:</h2>

        <div class="ticket-details">
          <table class="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>User</th>
                <th>Assigned User</th>
                <th>Status</th>
                <th>Tag</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ ticket.id }}</td>
                <td>{{ ticket.userId }}</td>
                <td>{{ ticket.assignedTo }}</td>
                <td>{{ ticket.status }}</td>
                <td>{{ ticket.tag }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <h2 class="font-weight-bold mt-4">Description:</h2>
        <div class="description-box">
          <p>{{ ticket.description }}</p>
        </div>
      </div>
      <div class="col-md-1"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      id: null,
      token: null,
      ticket: null,
    };
  },
  props: ["baseURL", "tickets"],
  methods: {
    async getTicket() {
      await axios
        .get(`http://localhost:9998/ticket/${this.$route.params.id}`)
        .then((res) => {
          this.ticket = res.data;
          console.log(
            ">>> (getTicket) this.ticket = " + JSON.stringify(this.ticket)
          );
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.getTicket();
  },
};
</script>

<style>
.category {
  font-weight: 400;
}

.ticket-details {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 20px;
  margin-top: 15px;
  background-color: #f9f9f9;
}

.table {
  width: 100%;
}

.table th,
.table td {
  text-align: left;
}

.description-box {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 20px;
  margin-top: 15px;
  background-color: #e9ecef;
  font-size: 1.2em; /* Increased font size for description */
  line-height: 1.5; /* Improved line spacing */
}

/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type="number"] {
  -moz-appearance: textfield;
}

#add-to-cart-button {
  background-color: #febd69;
}

#wishlist-button {
  background-color: #b9b9b9;
  border-radius: 0;
}

#show-cart-button {
  background-color: #131921;
  color: white;
  border-radius: 0;
}
</style>