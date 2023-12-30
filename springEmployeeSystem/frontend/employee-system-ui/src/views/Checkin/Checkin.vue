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
        <form>
          <!-- <div class="form-group">
              <label>Name</label>
              <input type="text" class="form-control" v-model="name" required />
            </div> -->
          <button type="button" class="btn btn-primary" @click="addCheckin">
            checkin
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
    };
  },
  props: ["baseURL", "products"],
  methods: {
    async addCheckin() {
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
          this.$router.push({ name: "AdminDepartment" });
          swal({
            text: "Checkin Added Successfully!",
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
    