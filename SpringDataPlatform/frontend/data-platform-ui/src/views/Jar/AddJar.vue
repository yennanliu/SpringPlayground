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
import { jarService } from "@/services";

export default {
  name: "AddJar",
  data() {
    return {
      loading: false,
    };
  },
  methods: {
    async addJar() {
      const fileInput = this.$refs.fileInput;
      const file = fileInput.files[0];

      if (!file) {
        swal({
          text: "Please select a JAR file",
          icon: "warning",
          closeOnClickOutside: false,
        });
        return;
      }

      this.loading = true;
      try {
        await jarService.create(file);
        this.$router.push({ name: "ListJar" });
        swal({
          text: "Jar Added Successfully!",
          icon: "success",
          closeOnClickOutside: false,
        });
      } catch (error) {
        swal({
          text: "Failed to upload JAR file",
          icon: "error",
          closeOnClickOutside: false,
        });
      } finally {
        this.loading = false;
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
