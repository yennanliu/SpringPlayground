<template>
    <div class="container">
      <div class="row pt-5">
        <div class="col-md-1"></div>
        <div class="col-md-10 col-12">
          <!-- <h6 class="font-weight-bold">Id: {{ department.id }}</h6> -->
          <h1 class="font-weight-bold">Ticket: {{ ticket.name }}</h1>
  
          <h2 class="font-weight-bold mt-3">Users:</h2>
  
          <table class="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Subject</th>
                <!-- <th>Email</th>
                <th>Department</th> -->
                <!-- <th>Profile</th> -->
              </tr>
            </thead>
            <tbody>
              <tr v-for="ticket in tickets" :key="ticket.id">
                <td>{{ ticket.id }}</td>
                <td>{{ ticket.subject }}</td>
                <!-- <td>{{ user.email }}</td>
                <td>{{ user.departementId }}</td> -->
                <!-- <td>
                  <router-link :to="`/users/show/${user.id}`">
                    View Profile
                  </router-link>
                </td> -->
              </tr>
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
      };
    },
    props: ["baseURL", "tickets"],
    methods: {
      async getTicket() {
        await axios
          .get(`http://localhost:9998/ticket/${this.$route.params.id}`)
          .then((res) => (this.ticket = res.data))
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
  