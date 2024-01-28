<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Flink Jars</h4>
        <router-link
          id="add-department"
          :to="{ name: 'AddJar' }"
          v-show="$route.name == 'AdminJar'"
        >
          <button class="btn">Add a new Jar</button>
        </router-link>
      </div>
    </div>
    <div class="row">
      <div
        v-for="jar of jars"
        :key="jar.id"
        class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex"
      >
        <JarBox :jar="jar"> </JarBox>
      </div>
    </div>
  </div>
</template>
  
  <script>
// https://youtu.be/VZ1NV7EHGJw?si=JPmnA7oQoVdPJwAL&t=1450
// https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Product/Product.vue

import JarBox from "../../components/Jar/JarBox";
var axios = require("axios");

export default {
  name: "Jar",
  components: { JarBox },
  props: ["baseURL"],

  data() {
    return {
      jars: [],
    };
  },
  methods: {
    async getDepartments() {
      await axios
        // "http://localhost:9999/jar/"
        .get(`${this.baseURL}/jar/`)
        .then((res) => (this.jars = res.data))
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.getDepartments();
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