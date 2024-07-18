<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add new Vacation</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form>
          <label>User</label>
          <select class="form-control" v-model="userId" required>
            <option v-for="user of users" :key="user.id" :value="user.id">
              ID : {{ user.id }} Name : {{ user.firstName }} {{ user.lastName }}
            </option>
          </select>

          <div class="form-group">
            <label>Start date</label>
            <date-picker v-model="startDate" :format="datePickerFormat" required></date-picker>
          </div>
          <div class="form-group">
            <label>End date</label>
            <date-picker v-model="endDate" :format="datePickerFormat" required></date-picker>
          </div>

          <div class="form-group">
            <label>Type</label>
            <select class="form-control" v-model="type" required>
              <option v-for="schema in schemas" :key="schema.id" :value="schema.columnName">
                {{ schema.columnName }}
              </option>
            </select>
          </div>

          <button type="button" class="btn btn-primary" @click="addVacation">Submit</button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script>
import DatePicker from "vue2-datepicker";
import axios from "axios";
import swal from "sweetalert";

export default {
  components: {
    DatePicker,
  },
  data() {
    return {
      userId: null,
      startDate: null,
      endDate: null,
      type: null,
      token: null,
      datePickerFormat: "YYYY-MM-DD",
      users: [],
      schemas: [],
    };
  },
  methods: {
    async addVacation() {
      const newVacation = {
        userId: this.userId,
        startDate: this.startDate,
        endDate: this.endDate,
        type: this.type,
      };

      const baseURL = "http://localhost:9998/";

      try {
        await axios.post(baseURL + "vacation/add", newVacation);
        this.$router.push({ name: "Vacation" });
        swal({
          text: "Vacation Added and email sent Successfully!",
          icon: "success",
          closeOnClickOutside: false,
        });
      } catch (error) {
        console.error("Error adding vacation:", error);
      }
    },

    async getUsers() {
      try {
        const res = await axios.get("http://localhost:9998/users/");
        this.users = res.data;
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    },

    async getSchemas() {
      try {
        const res = await axios.get("http://localhost:9998/schema/active/");
        this.schemas = res.data.filter((x) => x.schemaName === "vacation");
      } catch (error) {
        console.error("Error fetching schemas:", error);
      }
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.getUsers();
    this.getSchemas();
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