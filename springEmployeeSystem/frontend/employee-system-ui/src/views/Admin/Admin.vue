<template>
  <div class="adminPanel container">
    <router-link :to="{ name: 'AdminUser' }">
      <button type="button" class="btn btn-outline-primary">Admin Users</button>
    </router-link>
    <router-link :to="{ name: 'AdminDepartment' }">
      <button type="button" class="btn btn-outline-primary">
        Admin Departments
      </button>
    </router-link>
  </div>
</template>

<script>
export default {
  mounted() {
    if (!localStorage.getItem("token")) {
      this.$router.push({ name: "Signin" });
    }
  },
};
</script>

<style>
.adminPanel {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.adminPanel button {
  font-size: 30px;
  width: fit-content;
  margin: 55px 0;
}
</style>
