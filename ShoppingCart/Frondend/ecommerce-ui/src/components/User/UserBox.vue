<template>
  <div class="card h-100 w-100">
    <!-- <div class="embed-responsive embed-responsive-16by9">
        <img
          class="card-img-top embed-responsive-item"
          :src="product.imageURL"
          alt="Product Image"
        />
      </div> -->
    <div class="card-body">
      <router-link :to="{ name: 'ShowDetails', params: { id: user.id } }"
        ><h5 class="card-title">
          {{ user.firstName + user.lastName }}
        </h5></router-link
      >
      <p class="card-text"><sup>$</sup>{{ user.email }}</p>
      <p class="card-text"><sup>$</sup>{{ user.role }}</p>
      <!-- <router-link
          id="edit-product"
          :to="{ name: 'EditProduct', params: { id: product.id } }"
          v-show="$route.name == 'AdminProduct'"
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
      this.$router.push({
        name: "ShowDetails",
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