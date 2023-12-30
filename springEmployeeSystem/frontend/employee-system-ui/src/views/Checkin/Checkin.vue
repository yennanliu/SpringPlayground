<!-- CheckIn.vue -->

<template>
  <div class="container">
    <h2>Check-In Page</h2>

    <button class="btn btn-primary" @click="addCheckin">Check-In</button>
    
    <!-- <div v-if="!isCheckedIn">
      <button class="btn btn-primary" @click="addCheckin">Check-In</button>
    </div> -->

    <!-- <div v-else>
      <p>User {{ userId }} has checked in at {{ checkInTime }}</p>
    </div> -->
  </div>
</template>
  
  <script>
import swal from "sweetalert";
import axios from "axios";

export default {
  data() {
    return {
      userId: null, // Replace with the actual user ID
    };
  },
  methods: {
    // checkIn() {
    //   // Simulate checking in by setting isCheckedIn to true and capturing the check-in time
    //   this.isCheckedIn = true;
    //   this.checkInTime = new Date().toLocaleTimeString();
    // },

    async addCheckin() {
      console.log(">>> addCheckin start ...");
      const newCheckin = {
        userId: this.userId,
      };

      await axios({
        method: "post",
        url: "http://localhost:9998/" + "checkin/add",
        data: JSON.stringify(newCheckin),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          //sending the event to parent to handle
          console.log(res);
          this.$emit("fetchData");
          this.$router.push({ name: "home" });
          swal({
            text: "Checkin Added Successfully!",
            icon: "success",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => console.log(err));
    },
  },
};
</script>
  
  <style scoped>
.container {
  max-width: 600px;
  margin: 50px auto;
}
</style>
  