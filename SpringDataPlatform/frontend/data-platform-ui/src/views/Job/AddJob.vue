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
import axios from "axios";

export default {
  data() {
    return {
      id: null,
      allowNonRestoredState: null,
      entryClass: null,
      jarId: null,
      savedJarId: null,
      parallelism: null,
      programArgs: null,
      savePointPath: null,
      jars: [],
    };
  },
  props: ["baseURL"],
  methods: {
    async getJars() {
      // fetch users
      await axios
        .get("http://localhost:9999/" + "jar/")
        .then((res) => {
          this.jars = res.data;
        })
        .catch((err) => console.log(err));
    },

    async addJob() {
      const newJob = {
        allowNonRestoredState: null,
        entryClass: null,
        jarId: this.savedJarId,
        parallelism: 1,
        programArgs: null,
        savePointPath: null,
      };

      await axios({
        method: "post",
        url: "http://localhost:9999/" + "job/add",
        data: JSON.stringify(newJob),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          //sending the event to parent to handle
          console.log(res);
          this.$emit("fetchData");
          this.$router.push({ name: "ListJob" });
          swal({
            text: "Job Added Successfully!",
            icon: "success",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => console.log(err));
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
