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

          <label>Interpreter</label>
          <select class="form-control" v-model="interpreterGroup" required>
            <option
              v-for="schema of schemas"
              :key="schema.id"
              :value="schema.columnName"
            >
              {{ schema.columnName.toUpperCase() }}
            </option>
          </select>

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
import { zeppelinService } from "@/services";

export default {
  name: "AddNotebook",
  data() {
    return {
      notePath: null,
      schemas: [],
      interpreterGroup: null,
      loading: false,
    };
  },
  methods: {
    async addNoteBook() {
      if (!this.notePath || !this.interpreterGroup) {
        swal({
          text: "Please enter notebook name and select interpreter",
          icon: "warning",
          closeOnClickOutside: false,
        });
        return;
      }

      this.loading = true;
      try {
        await zeppelinService.create({
          notePath: this.notePath,
          interpreterGroup: this.interpreterGroup,
        });
        this.$router.push({ name: "ListNotebook" });
        swal({
          text: "Notebook Added Successfully!",
          icon: "success",
          closeOnClickOutside: false,
        });
      } catch (error) {
        swal({
          text: "Failed to create notebook",
          icon: "error",
          closeOnClickOutside: false,
        });
      } finally {
        this.loading = false;
      }
    },

    async getSchemas() {
      try {
        this.schemas = await zeppelinService.getInterpreters();
      } catch (error) {
        swal({
          text: "Failed to load interpreters",
          icon: "error",
          closeOnClickOutside: false,
        });
      }
    },
  },

  mounted() {
    this.getSchemas();
  },
};
</script>

<style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}
</style>
