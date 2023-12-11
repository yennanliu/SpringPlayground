<!-- <template>
    <div class="container">
      <div class="row">
        <div class="col-12 text-center">
          <h4 class="pt-3">Our Products</h4>
          <router-link id="add-product" :to="{name : 'AddProduct'}" v-show="$route.name=='AdminProduct'">
            <button class="btn">Add a new Product</button>
          </router-link>
        </div>
      </div>
      <div class="row">
          <div v-for="product of products" :key="product.id" class="col-md-6 col-xl-4 col-12 pt-3  justify-content-around d-flex">
            <ProductBox :product="product">
            </ProductBox>
          </div>
      </div>
    </div>
  </template> -->
  
  <script>
import UserBox from "../../components/User/UserBox";
var axios = require("axios");

export default {
  name: "User",
  components: { UserBox },
  props: ["baseURL"],

  data() {
    return {
      //baseURL: "http://localhost:9999/", // NOTE !! we read baseURL from App.vue
      user: null,
    };
  },
  methods: {
    async getUser() {
      if (this.token) {
        await axios
          .get(this.baseURL + "userProfile/")
          .then((res) => (this.user = res.data))
          .catch((err) => console.log(err));
      }
    },
  },
  mounted() {
    this.getUser();
  },
};
</script>
  
  <style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

#add-product {
  float: right;
  font-weight: 500;
}
</style>