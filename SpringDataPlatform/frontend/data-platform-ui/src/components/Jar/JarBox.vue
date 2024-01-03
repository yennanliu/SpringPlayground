<template>
  <div class="card h-100 w-100">
    <div class="card-body">
      <div class="card-header">
        <router-link :to="{ name: 'ShowJarDetails', params: { id: jar.id } }">
          <h5 class="card-title">{{ jar.fileName }}</h5>
        </router-link>
        <!-- <router-link
          id="edit-jar"
          :to="{ name: 'EditJar', params: { id: jar.id } }"
          v-show="$route.name == 'AdminJar'"
          class="edit-link"
        >
          Edit
        </router-link> -->
      </div>
    </div>
  </div>
</template>

<script>
var axios = require("axios");
import swal from "sweetalert";
export default {
  name: "JarBox",
  props: ["jar"],
  data() {
    return {
      jars: [],
    };
  },
  methods: {
    ShowJarDetails() {
      console.log(
        "(ShowJarDetails) this.$route.params.id = " + this.$route.params.id
      );
      this.$router.push({
        name: "ShowJarDetails",
        arams: { id: this.$route.params.id },
      });
    },

    async EditDepartment() {
      axios
        .post("http://localhost:9999/jar/", this.jar)
        .then((res) => {
          console.log(res);
          //sending the event to parent to handle
          this.$emit("fetchData");
          this.$router.push({ name: "AdminJar" });
          swal({
            text: "Jar Updated Successfully!",
            icon: "success",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => console.log("err", err));
    },
  },
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.embed-responsive .card-img-top {
  object-fit: cover;
}

a {
  text-decoration: none;
}

.card-title {
  color: #484848;
  font-size: 1.1rem;
  font-weight: 400;
}

.card-title:hover {
  font-weight: bold;
}

.edit-link {
  background-color: #007bff;
  color: #fff;
  padding: 5px 5px;
  border-radius: 3px;
  cursor: pointer;
}

.edit-link:hover {
  background-color: #0056b3;
}
</style>
