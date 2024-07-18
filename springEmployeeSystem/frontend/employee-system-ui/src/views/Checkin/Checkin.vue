<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Check In</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form @submit.prevent="addCheckin">
          <div class="form-group">
            <label>User Id</label>
            <input
              type="text"
              class="form-control"
              v-model="userId"
              required
              @input="validateUserId"
            />
          </div>
          <button type="submit" class="btn btn-primary" :disabled="!isUserIdValid">
            Check In
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
      userId: null,
      isUserIdValid: false,
    };
  },
  methods: {
    validateUserId() {
      // Check if userId is a valid integer
      this.isUserIdValid = Number.isInteger(Number(this.userId));
      if (!this.isUserIdValid) {
        swal({
          text: "User ID must be a valid integer.",
          icon: "error",
          closeOnClickOutside: false,
        });
      }
    },
    async addCheckin() {
      // Proceed only if userId is valid
      if (!this.isUserIdValid) {
        return;
      }

      const newCheckin = {
        id: null,
        userId: this.userId,
        createTime: null,
      };

      try {
        const res = await axios.post("http://localhost:9998/checkin/add", newCheckin, {
          headers: {
            "Content-Type": "application/json",
          },
        });

        console.log(res);
        this.$emit("fetchData");
        this.$router.push({ name: "Home" });
        swal({
          text: "User ID = " + this.userId + " Check-in OK!",
          icon: "success",
          closeOnClickOutside: false,
        });
      } catch (err) {
        // Handle error when user ID does not exist or other errors
        swal({
          text: "Error: " + err.response.data.message || "Check-in failed!",
          icon: "error",
          closeOnClickOutside: false,
        });
        console.error(err);
      }
    },
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