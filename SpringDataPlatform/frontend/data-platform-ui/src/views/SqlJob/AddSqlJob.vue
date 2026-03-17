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
            <textarea
              class="form-control"
              v-model="statement"
              rows="5"
              required
            ></textarea>
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
import { jobService } from "@/services";

export default {
  name: "AddSqlJob",
  data() {
    return {
      statement: null,
      loading: false,
    };
  },
  methods: {
    async addSQLJob() {
      if (!this.statement || !this.statement.trim()) {
        swal({
          text: "Please enter a SQL statement",
          icon: "warning",
          closeOnClickOutside: false,
        });
        return;
      }

      this.loading = true;
      try {
        await jobService.createSqlJob(this.statement);
        this.$router.push({ name: "ListJob" });
        swal({
          text: "SQL Job Added Successfully!",
          icon: "success",
          closeOnClickOutside: false,
        });
      } catch (error) {
        swal({
          text: "Failed to submit SQL job",
          icon: "error",
          closeOnClickOutside: false,
        });
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

/* Additional styling for the textarea */
textarea {
  font-family: "Courier New", monospace;
}
</style>
