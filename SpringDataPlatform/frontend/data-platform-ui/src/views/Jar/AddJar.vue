<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add new Jar</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form>
          <div class="form-group">
            <label>Jar File</label>
            <input
              type="text"
              class="form-control"
              v-model="jarFile"
              required
            />
          </div>
          <button type="button" class="btn btn-primary" @click="addJar">
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
      jarFile: null,
    };
  },
  props: ["baseURL", "products"],
  methods: {
    async addJar() {
      const newJar = {
        jarFile: this.jarFile,
      };

      await axios({
        method: "post",
        url: "http://localhost:9999/" + "jar/add_jar",
        data: JSON.stringify(newJar),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          //sending the event to parent to handle
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
    