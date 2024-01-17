<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Notebook List</h1>
        <h5>{{ msg }}</h5>
      </div>
    </div>

    <div class="row">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Detail</th>
            <!-- Add more columns if needed -->
          </tr>
        </thead>
        <tbody>
          <tr v-for="notebook in notebooks" :key="notebook.id">
            <td>{{ notebooks.id }}</td>
            <td>{{ notebooks.zeppelinNoteId }}</td>
            <td>
              <router-link :to="`/jars/show/${jar.id}`">
                Jar Detail
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
//import JarBox from "../../components/Jar/JarBox";
var axios = require("axios");

export default {
  name: "ListNotebook",
  data() {
    return {
      id: null,
      notebooks: [],
      len: 0,
      msg: null,
    };
  },
  //components: { JarBox },
  props: ["baseURL"],
  methods: {
    async fetchData() {
      try {
        const response = await axios.get("http://localhost:9999/zeppelin/");
        this.notebooks = response.data;
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
  