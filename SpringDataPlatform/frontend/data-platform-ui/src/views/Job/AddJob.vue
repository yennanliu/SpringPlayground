<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Submit a new Flink Job</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form>
          <div class="form-group">
            <label>Jar ID</label>
            <select class="form-control" v-model="savedJarId" required>
              <option v-for="jar in jars" :key="jar.id" :value="jar.id">
                Name : {{ jar.id + " " + jar.savedJarName }}
              </option>
            </select>
          </div>

          <button type="button" class="btn btn-primary" @click="addJob">
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
import { jarService, jobService } from "@/services";

export default {
  name: "AddJob",
  data() {
    return {
      savedJarId: null,
      jars: [],
      loading: false,
    };
  },
  methods: {
    async getJars() {
      try {
        this.jars = await jarService.getAll();
      } catch (error) {
        swal({
          text: "Failed to load JAR files",
          icon: "error",
          closeOnClickOutside: false,
        });
      }
    },

    async addJob() {
      if (!this.savedJarId) {
        swal({
          text: "Please select a JAR file",
          icon: "warning",
          closeOnClickOutside: false,
        });
        return;
      }

      this.loading = true;
      try {
        await jobService.create({ jarId: this.savedJarId });
        this.$router.push({ name: "ListJob" });
        swal({
          text: "Job Added Successfully!",
          icon: "success",
          closeOnClickOutside: false,
        });
      } catch (error) {
        swal({
          text: "Failed to submit job",
          icon: "error",
          closeOnClickOutside: false,
        });
      } finally {
        this.loading = false;
      }
    },
  },

  mounted() {
    this.getJars();
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
