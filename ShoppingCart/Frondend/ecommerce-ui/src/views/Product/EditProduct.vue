<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Edit Product</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form v-if="product">
          <div class="form-group">
            <label>Category</label>
            <select class="form-control" v-model="product.categoryId" required>
              <option
                v-for="category of categories"
                :key="category.id"
                :value="category.id"
              >
                {{ category.categoryName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Name</label>
            <input
              type="text"
              class="form-control"
              v-model="product.name"
              required
            />
          </div>
          <div class="form-group">
            <label>Description</label>
            <input
              type="text"
              class="form-control"
              v-model="product.description"
              required
            />
          </div>
          <div class="form-group">
            <label>ImageURL</label>
            <input
              type="url"
              class="form-control"
              v-model="product.imageURL"
              required
            />
          </div>
          <div class="form-group">
            <label>Price</label>
            <input
              type="number"
              class="form-control"
              v-model="product.price"
              required
            />
          </div>
          <button type="button" class="btn btn-primary" @click="editProduct">
            Submit
          </button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script>
var axios = require("axios");
import swal from "sweetalert";

export default {
  data() {
    return {
      product: null,
      products: [],
    };
  },
  //props: ["baseURL", "products", "categories"],
  props: ["baseURL", "categories"],
  methods: {
    async editProduct() {
      axios
        .post(
          "http://localhost:9999/" + "product/update/" + this.id,
          this.product
        )
        .then((res) => {
          console.log(res);
          //sending the event to parent to handle
          this.$emit("fetchData");
          this.$router.push({ name: "AdminProduct" });
          swal({
            text: "Product Updated Successfully!",
            icon: "success",
            closeOnClickOutside: false,
          });
        })
        .catch((err) => console.log("err", err));
    },

    async getProduct() {
      //fetch categories
      await axios
        .get(this.baseURL + "product/")
        .then((res) => {
          //console.log("res = " + JSON.stringify(res))
          //console.log("res.data = " + JSON.stringify(res.data))
          console.log(
            "this.products.find = " +
              JSON.stringify(res.data.find((product) => product.id == this.$route.params.id))
          );
          // use this approach for now
          this.product = res.data.find(
            (product) => product.id == this.$route.params.id
          );
        })
        .catch((err) => console.log(err));
    },
  },
  mounted() {
    // if (!localStorage.getItem("token")) {
    //   this.$router.push({ name: "Signin" });
    //   return;
    // }

    this.getProduct();
    this.id = this.$route.params.id;
    console.log("this.id  = " + this.id);
    console.log("this.products  = " + JSON.stringify(this.products));
    console.log("this.categories  = " + JSON.stringify(this.categories));

    // TODO : fix why can filter product via id
    //this.product = this.products.find((product) => product.id == this.id);
    //console.log(">>> this.product  = " + JSON.stringify(this.product));
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
