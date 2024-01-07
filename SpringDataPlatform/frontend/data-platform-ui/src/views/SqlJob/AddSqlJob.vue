<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Submit new SQL Job</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form>
          <div class="form-group">
            <label>SQL command</label>
            <input
              type="text"
              class="form-control"
              v-model="statement"
              required
            />
          </div>
          <button type="button" class="btn btn-primary" @click="addSQLJob">
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
      statement: null,
    };
  },
  props: ["baseURL"],
  methods: {
    async addSQLJob() {
      const newSQLJob = {
        statement: this.statement,
      };

      await axios({
        method: "post",
        url: "http://localhost:9999" + "/sql_job/add",
        data: JSON.stringify(newSQLJob),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          //sending the event to parent to handle
          console.log(res);
          this.$emit("fetchData");
          this.$router.push({ name: "ListJob" });
          swal({
            text: "SQL Job Added Successfully!",
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
        