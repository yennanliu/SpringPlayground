<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Add a new Cluster</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <ValidationObserver ref="form" v-slot="{ handleSubmit, invalid }">
          <form @submit.prevent="handleSubmit(addCluster)">
            <ValidationProvider name="URL" rules="required|url" v-slot="{ errors }">
              <div class="form-group">
                <label>Url</label>
                <input
                  type="text"
                  class="form-control"
                  :class="{ 'is-invalid': errors[0] }"
                  v-model="url"
                  placeholder="e.g., http://localhost"
                />
                <span class="invalid-feedback">{{ errors[0] }}</span>
              </div>
            </ValidationProvider>

            <ValidationProvider name="Port" rules="required|port" v-slot="{ errors }">
              <div class="form-group">
                <label>Port</label>
                <input
                  type="text"
                  class="form-control"
                  :class="{ 'is-invalid': errors[0] }"
                  v-model="port"
                  placeholder="e.g., 8081"
                />
                <span class="invalid-feedback">{{ errors[0] }}</span>
              </div>
            </ValidationProvider>

            <button type="submit" class="btn btn-primary" :disabled="invalid || loading">
              Submit
              <div v-if="loading" class="spinner-border spinner-border-sm" role="status">
                <span class="sr-only">Loading...</span>
              </div>
            </button>
          </form>
        </ValidationObserver>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script>
import swal from "sweetalert";
import { clusterService } from "@/services";

export default {
  name: "AddCluster",
  data() {
    return {
      url: null,
      port: null,
      loading: false,
    };
  },
  methods: {
    async addCluster() {
      this.loading = true;
      try {
        await clusterService.create({
          url: this.url,
          port: this.port,
        });
        this.$router.push({ name: "ListCluster" });
        swal({
          text: "Cluster Added Successfully!",
          icon: "success",
          closeOnClickOutside: false,
        });
      } catch (error) {
        swal({
          text: "Failed to add cluster",
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
</style>
