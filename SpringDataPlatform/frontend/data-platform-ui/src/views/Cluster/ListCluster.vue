<template>
    <div class="container">
      <div class="row">
        <div class="col-12 text-center">
          <h1>Cluster List</h1>
          <h5>{{ msg }}</h5>
        </div>
      </div>
  
      <div class="row">
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Port</th>
              <th>Status</th>
              <th>Url</th>
              <th>Detail</th>
              <!-- Add more columns if needed -->
            </tr>
          </thead>
          <tbody>
            <tr v-for="cluster in clusters" :key="cluster.id">
              <td>{{ cluster.id }}</td>
              <td>{{ cluster.port }}</td>
              <td>{{ cluster.status }}</td>
              <td>{{ cluster.url }}</td>
              <td>
                <router-link :to="`/clusters/show/${cluster.id}`">
                  Cluster Detail
                </router-link>
              </td>
              <!-- Add more columns if needed -->
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script>
  //import JobBox from "../../components/Job/JobBox";
  var axios = require("axios");
  
  export default {
    name: "ListCluster",
    data() {
      return {
        clusters: [],
        id: null,
        len: 0,
        msg: null,
      };
    },
    //components: { JobBox },
    props: ["baseURL"],
    methods: {
      async fetchData() {
        try {
          const response = await axios.get("http://localhost:9999/cluster/");
          this.clusters = response.data;
        } catch (error) {
          console.error(error);
        }
      },
    },
    mounted() {
      this.fetchData();
    },
  };
  </script>
  
  <style scoped>
  h1,
  h5 {
    font-family: "Roboto", sans-serif;
    color: #484848;
  }
  
  .table {
    width: 100%;
    margin-bottom: 1rem;
    color: #212529;
  }
  
  .table th,
  .table td {
    padding: 0.75rem;
    vertical-align: top;
    border-top: 1px solid #dee2e6;
  }
  </style>
  