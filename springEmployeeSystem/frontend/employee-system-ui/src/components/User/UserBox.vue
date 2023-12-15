<template>
  <div class="card h-100 w-100">
    <div class="card-body">
      <router-link :to="{ name: 'ShowDetails', params: { id: user.id } }"
        ><h5 class="card-title">
          Name : {{ user.firstName + " " + user.lastName }}, Email :
          {{ user.email }}
        </h5></router-link
      >
      <!-- <router-link
        id="edit-user"
        :to="{ name: 'EditUser', params: { id: RouterLink.id } }"
        v-show="$route.name == 'AdminUser'"
      >
        Edit
      </router-link> -->
    </div>
  </div>
</template>

<!-- NOTE !!!

  Will only show product edit button when route name is "AdminProduct"

  e.g. 
        <router-link
          id="edit-product"
          :to="{ name: 'EditProduct', params: { id: product.id } }"
          v-show="$route.name == 'AdminProduct'"
        >
          Edit
        </router-link>
  -->

<script>
export default {
  name: "UserBox",
  props: ["user"],
  methods: {
    showDetails() {
      console.log(
        "(showDetails) this.$route.params.id = " + this.$route.params.id
      );
      this.$router.push({
        name: "ShowDetails",
        //params: { id: this.user.id },
        arams: { id: this.$route.params.id },
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
  color: #484848;
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
