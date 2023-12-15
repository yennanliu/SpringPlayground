<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <!-- <div class="col-md-4 col-12">
        <img :src="product.imageURL" :alt="product.name" class="img-fluid" />
      </div> -->
      <div class="col-md-6 col-12 pt-3 pt-md-0">
        <h4>{{ user.id }}</h4>
        <!-- <h6 class="category font-italic">{{ category.categoryName }}</h6> -->
        <h6 class="font-weight-bold">$ {{ user.email }}</h6>
        <p>
          {{ user.role }}
        </p>

        <div class="d-flex flex-row justify-content-between">
          <!-- <div class="input-group col-md-3 col-4 p-0">
            <div class="input-group-prepend">
              <span class="input-group-text" id="basic-addon1">Quantity</span>
            </div>
            <input class="form-control" type="number" v-model="quantity" />
          </div> -->

          <div class="input-group col-md-3 col-4 p-0">
            <button
              type="button"
              id="add-to-cart-button"
              class="btn"
              @click="addToCart()"
            >
              Add to Cart
              <ion-icon name="cart-outline" v-pre></ion-icon>
            </button>
          </div>
        </div>

        <div class="features pt-3">
          <h5><strong>Features</strong></h5>
          <ul>
            <li>this is a product description.</li>
            <li>blablalalalalalalalalal</li>
            <li>more words are still ongoing</li>
            <li>make good works</li>
            <li>last words</li>
          </ul>
        </div>

        <button
          id="wishlist-button"
          class="btn mr-3 p-1 py-0"
          :class="{ product_added_wishlist: isAddedToWishlist }"
          @click="addToWishList()"
        >
          {{ wishlistString }}
        </button>
        <button
          id="show-cart-button"
          type="button"
          class="btn mr-3 p-1 py-0"
          @click="listCartItems()"
        >
          Show Cart

          <ion-icon name="cart-outline" v-pre></ion-icon>
        </button>
      </div>
      <div class="col-md-1"></div>
    </div>
  </div>
</template>
  
  <script>
import swal from "sweetalert";
import axios from "axios";

export default {
  data() {
    return {
      user: {},
      id: null,
      token: null,
      isAddedToWishlist: false,
      wishlistString: "Add to wishlist",
      quantity: 1,
    };
  },
  props: ["baseURL", "users"],
  methods: {
    addToCart() {
      if (!this.token) {
        swal({
          text: "Please log in first!",
          icon: "error",
        });
        return;
      }
      axios
        .post(`${this.baseURL}cart/add?token=${this.token}`, {
          //productId: this.id, //productId, // TODO : fix get id from input param (use this.id approach for now)
          productId: this.$route.params.id,
          quantity: this.quantity,
        })
        .then(
          (response) => {
            if (response.status == 201) {
              swal({
                text: "User Added to the cart!",
                icon: "success",
                closeOnClickOutside: false,
              });
              // refresh nav bar
              this.$emit("fetchData");
            }
          },
          (error) => {
            console.log(error);
          }
        );
    },
    // TODO : fix with filter from product list
    async getUser() {
      //fetch categories
      await axios
        .get(this.baseURL + `user/userProfile/?token=${this.token}`)
        .then((res) => {
          console.log(">>> this.$route.params.id = " + this.$route.params.id);
          this.id = this.$route.params.id;
          console.log(">>> this.id =  " + this.id);
          console.log("this.$route.params.id = " + this.$route.params.id);
          console.log(
            "this.users.find = " +
              JSON.stringify(
                res.data.find((user) => user.id == this.$route.params.id)
              )
          );
          // use this approach for now
          this.user = res.data.find((user) => user.id == this.$route.params.id);
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
    console.log("this.token = " + this.token);
    this.getUser();
  },
};
</script>
  
  <style>
.category {
  font-weight: 400;
}

/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type="number"] {
  -moz-appearance: textfield;
}

#add-to-cart-button {
  background-color: #febd69;
}

#wishlist-button {
  background-color: #b9b9b9;
  border-radius: 0;
}

#show-cart-button {
  background-color: #131921;
  color: white;
  border-radius: 0;
}
</style>
  