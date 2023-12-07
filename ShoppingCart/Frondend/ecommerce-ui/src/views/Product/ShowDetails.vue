<template>
  <div class="container">
    <div class="row pt-5">
      <div class="col-md-1"></div>
      <div class="col-md-4 col-12">
        <img :src="product.imageURL" :alt="product.name" class="img-fluid" />
      </div>
      <div class="col-md-6 col-12 pt-3 pt-md-0">
        <h4>{{ product.name }}</h4>
        <!-- <h6 class="category font-italic">{{ category.categoryName }}</h6> -->
        <h6 class="font-weight-bold">$ {{ product.price }}</h6>
        <p>
          {{ product.description }}
        </p>

        <div class="d-flex flex-row justify-content-between">
          <div class="input-group col-md-3 col-4 p-0">
            <div class="input-group-prepend">
              <span class="input-group-text" id="basic-addon1">Quantity</span>
            </div>
            <input class="form-control" type="number" v-model="quantity" />
          </div>

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
          @click="addToWishList(this.id)"
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
      product: {},
      category: {},
      id: null,
      token: null,
      isAddedToWishlist: false,
      wishlistString: "Add to wishlist",
      quantity: 1,
    };
  },
  props: ["baseURL", "products", "categories"],
  methods: {
    addToWishList(productId) {
      axios
        .post(`${this.baseURL}wishlist/add?token=${this.token}`, {
          id: productId,
        })
        .then(
          (response) => {
            if (response.status == 201) {
              this.isAddedToWishlist = true;
              this.wishlistString = "Added to WishList";
            }
          },
          (error) => {
            console.log(error);
          }
        );
    },
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
                text: "Product Added to the cart!",
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

    listCartItems() {
      axios.get(`http://localhost:9999/cart/?token=${this.token}`).then(
        (response) => {
          if (response.status === 200) {
            this.$router.push("/cart");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },

    // TODO : fix with filter from product list
    async getProduct() {
      //fetch categories
      await axios
        .get(this.baseURL + "product/")
        .then((res) => {
          console.log(">>> this.$route.params.id = " + this.$route.params.id);
          this.id = this.$route.params.id;
          console.log(">>> this.id =  " + this.id);
          console.log("this.$route.params.id = " + this.$route.params.id);
          console.log(
            "this.products.find = " +
              JSON.stringify(
                res.data.find((product) => product.id == this.$route.params.id)
              )
          );
          // use this approach for now
          this.product = res.data.find(
            (product) => product.id == this.$route.params.id
          );
        })
        .catch((err) => console.log(err));
    },
    // TODO : fix with filter from category list
    async getCategory() {
      //fetch categories
      await axios
        .get(this.baseURL + "category/")
        .then((res) => {
          console.log("this.$route.params.id = " + this.$route.params.id);
          console.log(
            "this.categories.find = " +
              JSON.stringify(
                res.data.find(
                  (category) => category.id == this.$route.params.id
                )
              )
          );
          // use this approach for now
          this.category = res.data.find(
            (category) => category.id == this.$route.params.id
          );
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    // this.id = this.$route.params.id;
    // console.log(">>> this.id = " + this.id);
    this.getProduct();
    this.getCategory();
    //this.product = this.products.find((product) => product.id == this.id);
    // console.log(">>> this.product = " + JSON.stringify(this.product))
    // console.log(">>> this.product.categoryId  = " + this.product.categoryId)
    // this.category = this.categories.find(
    //   (category) => category.id == this.product.categoryId
    // );
    // console.log(">>> this.category = " + JSON.stringify(this.category))
    this.token = localStorage.getItem("token");
    console.log("this.token = " + this.token);
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
