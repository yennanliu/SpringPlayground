<template>
    <div class="container">
      <div class="row">
        <div class="col-12 text-center">
          <h1>Jar List</h1>
          <h5>{{ msg }}</h5>
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
  import JarBox from "../../components/Jar/JarBox";
  var axios = require("axios");
  export default {
    name: "ListDepartment",
    data() {
      return {
        id: null,
        jars: [],
        len: 0,
        msg: null,
      };
    },
    components: { JarBox },
    props: ["baseURL"],
    methods: {
      async fetchData() {
        // fetch users
        await axios
          .get("http://localhost:9999/" + "jar/")
          .then((res) => {
            this.jars = res.data;
            console.log(
              ">>> (fetchData) this.jars = " +
                JSON.stringify(this.jars)
            );
          })
          .catch((err) => console.log(err));
      },
    },
    mounted() {
      //this.id = this.$route.params.id;
      this.fetchData();
      //console.log(">>> (ListUsers) this.users = " + JSON.stringify(this.users));
    },
  };
  </script>
    
    <style scoped>
  h4 {
    font-family: "Roboto", sans-serif;
    color: #484848;
    font-weight: 700;
  }
  
  h5 {
    font-family: "Roboto", sans-serif;
    color: #686868;
    font-weight: 300;
  }
  </style>
    