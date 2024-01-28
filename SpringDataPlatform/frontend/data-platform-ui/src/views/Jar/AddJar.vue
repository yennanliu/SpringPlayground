<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add a new Jar</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form @submit.prevent="addJar">
          <div class="form-group">
            <label>Jar File</label>
            <input type="file" ref="fileInput" class="form-control" required />
          </div>
          <button type="submit" class="btn btn-primary">Submit</button>
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
    return {};
  },
  props: ["baseURL"],
  methods: {
    async addJar() {
      // Get the selected file from the input
      const fileInput = this.$refs.fileInput;
      const file = fileInput.files[0];

      // Create a FormData object and append the file to it
      const formData = new FormData();
      formData.append("jarfile", file);

      // Make the multipart/form-data POST request
      // `http://localhost:9999/jar/add_jar`
      await axios
        .post(`${this.baseURL}/jar/add_jar`, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((res) => {
          // Sending the event to parent to handle
          console.log(res);
          this.$emit("fetchData");
          this.$router.push({ name: "ListJar" });
          swal({
            text: "Jar Added Successfully!",
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
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}
</style>
