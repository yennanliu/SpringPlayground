<template>
    <div class="container">
      <div class="row">
        <div class="col-12 text-center">
          <h4 class="pt-3">Add new Ticket</h4>
        </div>
      </div>
  
      <div class="row">
        <div class="col-3"></div>
        <div class="col-md-6 px-5 px-md-0">
          <form>
            <!-- <div class="form-group">
              <label>Category</label>
              <select class="form-control" v-model="departmentId" required>
                <option
                  v-for="department of departments"
                  :key="department.id"
                  :value="department.id"
                >
                  {{ departments.name }}
                </option>
              </select>
            </div> -->
            <div class="form-group">
              <label>Subject</label>
              <input type="text" class="form-control" v-model="subject" required />
            </div>
            <button type="button" class="btn btn-primary" @click="addTicket">
              Submit
            </button>
          </form>
        </div>
        <div class="col-3"></div>
      </div>
    </div>
  </template>
    
    <script>
  import swal from "sweetalert";
  import axios from "axios";
  
  export default {
    data() {
      return {
        id: null,
        ticketId: null,
        name: null,
        tickets: [],
        ticket: null
      };
    },
    props: ["baseURL", "products"],
    methods: {
      async addTicket() {
        const newTicket = {
          id: this.id,
          name: this.name,
        };
  
        await axios({
          method: "post",
          url: "http://localhost:9998/" + "ticket/add",
          data: JSON.stringify(newTicket),
          headers: {
            "Content-Type": "application/json",
          },
        })
          .then((res) => {
            //sending the event to parent to handle
            console.log(res);
            this.$emit("fetchData");
            this.$router.push({ name: "AdminTicket" });
            swal({
              text: "Ticket Added Successfully!",
              icon: "success",
              closeOnClickOutside: false,
            });
          })
          .catch((err) => console.log(err));
      },
    },
  
    mounted() {},
  };
  </script>
    
    <style scoped>
  h4 {
    font-family: "Roboto", sans-serif;
    color: #484848;
    font-weight: 700;
  }
  </style>
    