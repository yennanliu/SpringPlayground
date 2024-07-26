<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Edit User</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form v-if="user">
          <div class="form-group">
            <label>First Name</label>
            <input type="text" class="form-control" v-model="user.firstName" required />
          </div>
          <div class="form-group">
            <label>Last Name</label>
            <input type="text" class="form-control" v-model="user.lastName" required />
          </div>
          <div class="form-group">
            <label>Email</label>
            <input type="text" class="form-control" v-model="user.email" required />
          </div>

          <div class="form-group">
            <label>Department</label>
            <select class="form-control" v-model="user.departmentId" required>
              <option v-for="department of departments" :key="department.id" :value="department.id">
                ID : {{ department.id }} Name : {{ department.name }}
              </option>
            </select>
          </div>

          <!-- File input for user photo -->
          <div class="form-group">
            <label>Upload User Photo</label>
            <input type="file" ref="fileInput" @change="handleFileUpload($event)" accept="image/*" />
            <!-- <input type="text" class="form-control" v-model="user.firstName" required />-->
          </div>

          <button type="button" class="btn btn-primary" @click="editUser">
            Submit
          </button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import swal from "sweetalert";

export default {
  data() {
    return {
      user: null,
      departments: [],
      photoFile: null, // To store selected photo file
    };
  },
  methods: {
    async editUser() {
      // Create form data to send both user data and photo file
      const formData = new FormData();
      formData.append("user", JSON.stringify(this.user)); // Convert user object to JSON string
      this.photoFile = "r34evgergre";
      if (this.photoFile) {
        formData.append("photo", this.photoFile); // Append photo file
      }

      console.log(">>> (editUser) formData = " + JSON.stringify(formData))

      axios
        // .post(`http://localhost:9998/users/update/${this.user.id}`, formData, {
        //   headers: {
        //     "Content-Type": "multipart/form-data",
        //   },
        // })
        .post("http://localhost:9998/users/update/", this.user, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          }
        })
        .then((res) => {
          console.log(res);
          this.$emit("fetchData");
          this.$router.push({ name: "AdminUser" });
          swal({
            text: "User Updated Successfully!",
            icon: "success",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => {
          console.error("Error updating user:", err);
        });
    },

    async getUser() {
      await axios
        .get(`http://localhost:9998/users/${this.$route.params.id}`)
        .then((res) => {
          this.user = res.data;
        })
        .catch((err) => {
          console.error("Error fetching user:", err);
        });
    },

    async getDepartments() {
      await axios
        .get("http://localhost:9998/dep/")
        .then((res) => {
          this.departments = res.data;
        })
        .catch((err) => {
          console.error("Error fetching departments:", err);
        });
    },

    // Method to handle file selection and update v-model
    handleFileUpload(event) {
      console.log(">>> handleFileUpload, event = {}", JSON.stringify(event));
      console.log(">>> handleFileUpload, event.target.files[0] = {}", event.target.files[0]);
      this.photoFile = event.target.files[0];
    },
  },

  mounted() {
    this.getDepartments();
    this.getUser();
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