<template>
  <div class="div_class">
    <h3>You will be redirected to payment page</h3>

    <div class="alert alert-primary" role="alert">
      While making payment use card number 4242 4242 4242 4242 and enter random
      cvv(3 digit)
    </div>

    <button
      class="checkout_button"
      id="proceed-to-checkout"
      @click="goToCheckout()"
    >
      Make payment
    </button>
  </div>
</template>
<script>
//const Stripe = require("stripe");
// import { ConvertApi } from 'exports-loader?type=commonjs&exports=ConvertApi!convertapi-js'
// load JS SDK explicitly :
// https://stackoverflow.com/questions/65485241/use-javascript-sdk-in-vue-project
//import {Stripe} from 'https://js.stripe.com/v3/'
import {loadStripe} from '@stripe/stripe-js';
// TODO : read public key from conf
const stripe = await loadStripe('pk_test_51OJSFeLMMLE7XGb55zFRgu8PdipN0S8rpdHUtnVpTSNn7SLsWnrG7rdq2ETBNkzMYGdB7P81hhGzy6GtZJDMI9RV00S6SpI6YB');
// const stripe = await loadStripe('pk_test_TYooMQauvdEDq54NiTphI7jx');

var axios = require("axios");
export default {
  data() {
    return {
      stripeAPIToken: "pk_test_51OJSFeLMMLE7XGb55zFRgu8PdipN0S8rpdHUtnVpTSNn7SLsWnrG7rdq2ETBNkzMYGdB7P81hhGzy6GtZJDMI9RV00S6SpI6YB",//process.env.VUE_APP_STRIPETOKEN,
      stripe: "",
      token: null,
      sessionId: null,
      checkoutBodyArray: [],
    };
  },

  name: "Checkout",
  props: ["baseURL"],
  methods: {
    /*
        Configures Stripe by setting up the elements and
        creating the card element.
      */
    // configureStripe() {
    //   console.log(">>> this.stripeAPIToken = " + this.stripeAPIToken)
    //   this.stripe = Stripe(this.stripeAPIToken);
    // },

    getAllItems() {
      axios.get(`${this.baseURL}cart/?token=${this.token}`).then(
        (response) => {
          if (response.status == 200) {
            let products = response.data;
            let len = Object.keys(products.cartItems).length;
            for (let i = 0; i < len; i++)
              this.checkoutBodyArray.push({
                imageUrl: products.cartItems[i].product.imageURL,
                productName: products.cartItems[i].product.name,
                quantity: products.cartItems[i].quantity,
                price: products.cartItems[i].product.price,
                productId: products.cartItems[i].product.id,
                userId: products.cartItems[i].userId,
              });
          }
        },
        (err) => {
          console.log(err);
        }
      );
    },

    goToCheckout() {
      axios
        .post(
          this.baseURL + "order/create-checkout-session",
          this.checkoutBodyArray
        )
        .then((response) => {
          localStorage.setItem("sessionId", response.data.sessionId);
          return response.data;
        })
        .then((session) => {
          /**
           *  
           * A Stripe Api method (this.stripe.redirectToCheckout)
           * 
           * which we store the session id we received from the response data 
           * in our sessionId which will redirect the user to a Stripe-hosted page 
           * to securely collect payment information. When a user completes their purchase, 
           * they are redirected back to our website.
           * 
           */
          return this.stripe.redirectToCheckout({
            sessionId: session.sessionId,
          });
        });
    },
  },
  mounted() {
    // get the token
    this.token = localStorage.getItem("token");
    // get all the cart items
    // API ref : https://stripe.com/docs/api?lang=node
    //this.stripe = Stripe(this.stripeAPIToken);
    this.stripe = stripe //Stripe(this.stripeAPIToken);
    //this.configureStripe();
    this.getAllItems();
  },
};
</script>

<style>
.alert {
  width: 50%;
}

.div_class {
  margin-top: 5%;
  margin-left: 30%;
}

.checkout_button {
  background-color: #5d3dec;
  border: none;
  color: white;
  margin-left: 15%;
  padding: 15px 30px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 15px;
  font-weight: bold;
  border-radius: 15px;
}

.checkout_button:focus {
  outline: none;
  box-shadow: none;
}

.checkout_button:disabled {
  background-color: #9b86f7;
  border: none;
  color: white;
  margin-left: 15%;
  padding: 15px 30px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 15px;
  font-weight: bold;
  border-radius: 15px;
  cursor: not-allowed;
}
</style>
