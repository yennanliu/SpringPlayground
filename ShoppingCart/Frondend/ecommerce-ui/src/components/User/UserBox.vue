<template>
  <div class="card h-100 w-100">
    <div class="card-body">
      <router-link :to="{ name: 'ShowUserDetails', params: { id: user.id } }"
        ><h5 class="card-title">
          Name : {{ user.firstName + " " + user.lastName }}
        </h5></router-link
      >
      <p class="card-text"><sup>Id : </sup>{{ user.id }}</p>
      <p class="card-text"><sup>Email : </sup>{{ user.email }}</p>
      <p class="card-text"><sup>Role : </sup>{{ user.role }}</p>

      <router-link
        id="edit-user"
        :to="{ name: 'EditUser', params: { id: user.id } }"
        v-show="$route.name == 'AdminUser'"
      >
        Edit
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserBox",
  props: ["user"],
  methods: {
    ShowUserDetails() {
      this.$router.push({
        name: "ShowUserDetails",
        params: { id: this.user.id },
      });
    },
  },
};
</script>

<style scoped>
.embed-responsive .card-img-top {
  object-fit: cover;
}

a {
  text-decoration: none;
}

.card-title {
  color: #eb7d08;
  font-size: 1.1rem;
  font-weight: 400;
}

.card-title:hover {
  font-weight: bold;
}

.card-text {
  font-size: 0.9rem;
}

#edit-product {
  float: right;
}
</style>
