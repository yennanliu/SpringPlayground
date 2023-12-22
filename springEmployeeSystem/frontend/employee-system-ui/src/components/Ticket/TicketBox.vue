<template>
    <div class="card h-100 w-100">
      <div class="card-body">
        <div class="card-header">
          <router-link
            :to="{ name: 'ShowTicketDetails', params: { id: ticket.id } }"
          >
            <h5 class="card-title">{{ ticket.subject }}</h5>
          </router-link>
          <router-link
            id="edit-department"
            :to="{ name: 'EditTicket', params: { id: ticket.id } }"
            v-show="$route.name == 'AdminTicket'"
            class="edit-link"
          >
            Edit
          </router-link>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  var axios = require("axios");
  import swal from "sweetalert";
  export default {
    name: "TicketBox",
    props: ["ticket"],
    data() {
      return {
        tickets: [],
      };
    },
    methods: {
      ShowTicketDetails() {
        console.log(
          "(ShowTicketDetails) this.$route.params.id = " +
            this.$route.params.id
        );
        this.$router.push({
          name: "ShowTicketDetails",
          arams: { id: this.$route.params.id },
        });
      },
  
      async EditTicket() {
        axios
          .post("http://localhost:9998/ticket/", this.ticket)
          .then((res) => {
            console.log(res);
            //sending the event to parent to handle
            this.$emit("fetchData");
            this.$router.push({ name: "AdminTicket" });
            swal({
              text: "Ticket Updated Successfully!",
              icon: "success",
              closeOnClickOutside: false,
            });
          })
          .catch((err) => console.log("err", err));
      },
    },
  };
  </script>
  
  <style scoped>
  .card-header {
    display: flex;
    justify-content: space-around;
    align-items: center;
  }
  
  .embed-responsive .card-img-top {
    object-fit: cover;
  }
  
  a {
    text-decoration: none;
  }
  
  .card-title {
    color: #484848;
    font-size: 1.1rem;
    font-weight: 400;
  }
  
  .card-title:hover {
    font-weight: bold;
  }
  
  .edit-link {
    background-color: #007bff;
    color: #fff;
    padding: 5px 5px;
    border-radius: 3px;
    cursor: pointer;
  }
  
  .edit-link:hover {
    background-color: #0056b3;
  }
  </style>
  