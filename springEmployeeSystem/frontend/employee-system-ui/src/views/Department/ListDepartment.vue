<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h1 class="page-title">Department List</h1>
        <h5 class="page-subtitle">{{ msg }}</h5>
      </div>
    </div>

    <div class="row mt-4">
      <div class="col-12">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>ID</th>
                <th>Department Name</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="department in departments" :key="department.id">
                <td>{{ department.id }}</td>
                <td>
                  <router-link
                    :to="{ name: 'ShowDepartmentDetails', params: { id: department.id } }"
                    class="department-link"
                  >
                    {{ department.name }}
                  </router-link>
                </td>
                <td>
                  <router-link
                    :to="{ name: 'EditDepartment', params: { id: department.id } }"
                    v-show="$route.name == 'AdminDepartment'"
                    class="btn btn-primary btn-sm"
                  >
                    Edit
                  </router-link>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
var axios = require("axios");
export default {
  name: "ListDepartment",
  data() {
    return {
      id: null,
      departments: [],
      len: 0,
      msg: null,
    };
  },
  props: ["baseURL"],
  methods: {
    async fetchData() {
      await axios
        .get("http://localhost:9998/" + "dep/")
        .then((res) => {
          this.departments = res.data;
          console.log(
            ">>> (fetchData) this.departments = " +
              JSON.stringify(this.departments)
          );
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.fetchData();
  },
};
</script>

<style scoped>
.page-title {
  font-family: "Roboto", sans-serif;
  color: #2c3e50;
  font-weight: 700;
  margin-bottom: 1rem;
}

.page-subtitle {
  font-family: "Roboto", sans-serif;
  color: #7f8c8d;
  font-weight: 300;
  margin-bottom: 2rem;
}

.table {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table thead th {
  background-color: #f8f9fa;
  border-bottom: 2px solid #dee2e6;
  color: #2c3e50;
  font-weight: 600;
  padding: 1rem;
}

.table tbody td {
  padding: 1rem;
  vertical-align: middle;
}

.department-link {
  color: #3498db;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s ease;
}

.department-link:hover {
  color: #2980b9;
  text-decoration: none;
}

.btn-primary {
  background-color: #3498db;
  border-color: #3498db;
  transition: all 0.2s ease;
}

.btn-primary:hover {
  background-color: #2980b9;
  border-color: #2980b9;
}

.table-responsive {
  border-radius: 8px;
  overflow: hidden;
}
</style>
  