<template>
    <div class="container">
      <div class="row pt-5">
        <div class="col-md-1"></div>
        <div class="col-md-10 col-12">
          <!-- <h6 class="font-weight-bold">Id: {{ department.id }}</h6> -->
          <h1 class="font-weight-bold">Ticket: {{ ticket.subject }}</h1>
  
          <h2 class="font-weight-bold mt-3">Users:</h2>
  
          <table class="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Subject</th>
                <th>User</th>
                <th>Assgined User</th>
                <th>Description</th>
                <th>Status</th>
                <th>Tag</th>
              </tr>
            </thead>
            <tbody>
              <!-- <tr v-for="ticket in ticket" :key="ticket.id"> -->
                <td>{{ ticket.id }}</td>
                <td>{{ ticket.subject }}</td>
                <td>{{ ticket.userId }}</td>
                <td>{{ ticket.assignedTo }}</td>
                <td>{{ ticket.description }}</td>
                <td>{{ ticket.status }}</td>
                <td>{{ ticket.tag }}</td>

                <!-- <td>{{ user.email }}</td>
                <td>{{ user.departementId }}</td> -->
                <!-- <td>
                  <router-link :to="`/users/show/${user.id}`">
                    View Profile
                  </router-link>
                </td> -->
              <!-- </tr> -->
            </tbody>
          </table>
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
        //tickets: {},
        id: null,
        token: null,
        ticket: null
      };
    },
    props: ["baseURL", "tickets"],
    methods: {
      async getTicket() {
        await axios
          .get(`http://localhost:9998/ticket/${this.$route.params.id}`)
          .then((res) => {
            this.ticket = res.data
            console.log(">>> (getTicket) this.ticket = " + JSON.stringify())
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
  