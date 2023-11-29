<template>
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
  </template>
  
  <script>
  // https://youtu.be/VZ1NV7EHGJw?si=JPmnA7oQoVdPJwAL&t=1450
  // https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Product/Product.vue

  import ProductBox from '../../components/Product/ProductBox';
  var axios =  require('axios');

  export default {
    name: 'Product',
    components : {ProductBox},
    //props : [ "baseURL" , "products" ],
    props : [ "baseURL" ],

    data() {
            return {
                //baseURL : "https://limitless-lake-55070.herokuapp.com/",
                //baseURL: "http://localhost:9999/", // NOTE !! we read baseURL from App.vue
                products : [],
            }
        },
        methods: {
            async getProducts() {
                //fetch categories
                await axios.get(this.baseURL + "product/")
                    .then(res => this.products = res.data)
                    .catch(err => console.log(err))
            }
        },
        mounted(){
            this.getProducts();

        }

    // TODO : deal with token, login/logout    
    // mounted(){
    //   if (this.$route.name=='AdminProduct' && !localStorage.getItem('token')) {
    //     this.$router.push({name : 'Signin'});
    //   }
    // }

  }
  </script>
  
  <style scoped>
  h4 {
    font-family: 'Roboto', sans-serif;
    color: #484848;
    font-weight: 700;
  }
  
  #add-product {
    float: right;
    font-weight: 500;
  }
  </style>