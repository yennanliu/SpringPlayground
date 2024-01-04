<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <div class="col-md-10 col-12">
        <h1 class="font-weight-bold">Jar: {{ jar.id }}</h1>
        <h2 class="font-weight-bold mt-3">Detail:</h2>
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>File Name</th>
              <th>Status</th>
              <th>Upload Time</th>
              <th>Flink Jar Link</th>
            </tr>
          </thead>
          <tbody>
            <td>{{ jar.id }}</td>
            <td>{{ jar.subject }}</td>
            <td>{{ jar.status }}</td>
            <td>{{ jar.uploadTime }}</td>
            <td>
              <a :href="`http://localhost:8081/#/submit`" target="_blank">
                View Job
              </a>
            </td>
          </tbody>
        </table>
      </div>
      <div class="col-md-1"></div>
    </div>
  </div>
</template>
    
    <script>
import axios from "axios";

export default {
  data() {
    return {
      jar: {},
    };
  },
  props: ["baseURL", "jars"],
  methods: {
    async getJar() {
      await axios
        .get(`http://localhost:9999/jar/${this.$route.params.id}`)
        .then((res) => {
          console.log("${this.$route.params.id} = " + this.$route.params.id);
          this.jar = res.data;
          console.log(">>> (getJar) this.jar = " + JSON.stringify());
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.getJar();
  },
};
</script>
    
    <style>
.category {
  font-weight: 400;
}

/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type="number"] {
  -moz-appearance: textfield;
}

#add-to-cart-button {
  background-color: #febd69;
}

#wishlist-button {
  background-color: #b9b9b9;
  border-radius: 0;
}

#show-cart-button {
  background-color: #131921;
  color: white;
  border-radius: 0;
}
</style>
    