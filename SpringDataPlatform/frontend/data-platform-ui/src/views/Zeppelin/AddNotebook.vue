<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add a new Notebook</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form>
          <div class="form-group">
            <label>Notebook Name</label>
            <input
              type="text"
              class="form-control"
              v-model="notePath"
              required
            />
          </div>

          <div class="form-group">
            <label>Interpreter Group</label>
            <input
              type="text"
              class="form-control"
              v-model="interpreterGroup"
              required
            />
          </div>

          <button type="button" class="btn btn-primary" @click="addNoteBook">
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
    async addNoteBook() {
      const newNoteBook = {
        notePath: this.notePath,
        interpreterGroup: this.interpreterGroup,
      };

      await axios({
        method: "post",
        url: "http://localhost:9999/zeppelin/add",
        data: JSON.stringify(newNoteBook),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          console.log(res);
          this.$emit("fetchData");
          this.$router.push({ name: "ListNotebook" });
          swal({
            text: "Notebook Added Successfully!",
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
  