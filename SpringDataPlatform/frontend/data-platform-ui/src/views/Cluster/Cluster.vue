<template>
    <div class="container">
      <div class="row">
        <div class="col-12 text-center">
          <h4 class="pt-3">Flink Cluster</h4>
          <router-link
            id="add-department"
            :to="{ name: 'AddCluster' }"
            v-show="$route.name == 'AdminJob'"
          >
            <button class="btn">Add a new Cluster</button>
          </router-link>
        </div>
      </div>
      <div class="row">
        <div
          v-for="cluster of clusters"
          :key="cluster.id"
          class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex"
        >
          <JobBox :cluster="cluster"> </JobBox>
        </div>
      </div>
    </div>
  </template>
    
    <script>
  // https://youtu.be/VZ1NV7EHGJw?si=JPmnA7oQoVdPJwAL&t=1450
  // https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Product/Product.vue
  
  import JobBox from "../../components/Clusters/ClusterBox";
  var axios = require("axios");
  
  export default {
    name: "Cluster",
    components: { JobBox },
    props: ["baseURL"],
  
    data() {
      return {
        clusters: [],
      };
    },
    methods: {
      async getClusters() {
        await axios
          .get("http://localhost:9999/cluster/")
          .then((res) => (this.clusters = res.data))
          .catch((err) => console.log(err));
      },
    },
    mounted() {
      this.getClusters();
    },
  };
  </script>
    
    <style scoped>
  h4 {
    font-family: "Roboto", sans-serif;
    color: #484848;
    font-weight: 700;
  }
  
  #add-user {
    float: right;
    font-weight: 500;
  }
  </style>