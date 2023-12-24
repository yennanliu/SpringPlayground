<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add New Ticket</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form>
          <div class="form-group">
            <label>Subject</label>
            <input
              type="text"
              class="form-control"
              v-model="subject"
              required
            />
          </div>

          <label>User</label>
          <select class="form-control" v-model="userId" required>
            <option v-for="user of users" :key="user.id" :value="user.id">
              ID : {{ user.id }}  Name : {{ user.firstName }} {{ user.lastName }}
            </option>
          </select>

          <label>Assigned User</label>
          <select class="form-control" v-model="assignedTo" required>
            <option v-for="user of users" :key="user.id" :value="user.id">
              ID : {{ user.id }}  Name : {{ user.firstName }} {{ user.lastName }}
            </option>
          </select>

          <div class="form-group">
            <label>Description</label>
            <input
              type="text"
              class="form-control"
              v-model="description"
              required
            />
          </div>

          <div class="form-group">
            <label>Tag</label>
            <input type="text" class="form-control" v-model="tag" required />
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
      ticket: null,
      users: []
    };
  },
  props: ["baseURL"],
  methods: {

    async getUsers() {
      // fetch users
      await axios
        .get("http://localhost:9998/" + "users/")
        .then((res) => {
          this.users = res.data;
          console.log(
            ">>> (getUsers) this.users = " + JSON.stringify(this.users)
          );
        })
        .catch((err) => console.log(err));
    },
    async addTicket() {
      const newTicket = {
        id: this.id,
        subject: this.subject,
        assignedTo: this.assignedTo,
        description: this.description,
        status: this.status,
        tag: this.tag,
        userId: this.userId,
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

  mounted() {
    this.getUsers();
  },
};
</script>
    
    <style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}
</style>
    