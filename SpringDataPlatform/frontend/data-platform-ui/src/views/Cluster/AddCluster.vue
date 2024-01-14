<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add new Cluster</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form>
          <div class="form-group">
            <label>Url</label>
            <input type="text" class="form-control" v-model="url" required />
          </div>

          <div class="form-group">
            <label>Port</label>
            <input type="text" class="form-control" v-model="port" required />
          </div>

          <button type="button" class="btn btn-primary" @click="addCluster">
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
      url: null,
      port: null,
      cluster: null,
    };
  },
  props: ["baseURL"],
  methods: {
    async addCluster() {
      const newCluster = {
        // cluster: this.cluster,
        url: this.url,
        port: this.port,
      };

      await axios({
        method: "post",
        url: "http://localhost:9999/" + "cluster/add_cluster",
        data: JSON.stringify(newCluster),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          //sending the event to parent to handle
          console.log(res);
          this.$emit("fetchData");
          this.$router.push({ name: "ListCluster" });
          swal({
            text: "Cluster Added Successfully!",
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
