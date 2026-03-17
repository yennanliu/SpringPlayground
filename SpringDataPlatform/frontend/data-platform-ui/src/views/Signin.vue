<!-- 
    https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Signin.vue
-->

<template>
  <div class="container">
    <!--    Logo Div-->
    <div class="row">
      <div class="col-12 text-center pt-3">
        <router-link :to="{ name: 'Home' }">
          <img id="logo" src="../assets/icon3.png" />
        </router-link>
      </div>
    </div>

    <div class="row">
      <div class="col-12 justify-content-center d-flex flex-row pt-5">
        <div id="signin-div" class="flex-item border">
          <h2 class="pt-4 pl-4">Sign-In</h2>
          <ValidationObserver ref="form" v-slot="{ handleSubmit, invalid }">
            <form @submit.prevent="handleSubmit(signin)" class="pt-4 pl-4 pr-4">
              <ValidationProvider name="Email" rules="required|email" v-slot="{ errors }">
                <div class="form-group">
                  <label>Email</label>
                  <input
                    type="email"
                    class="form-control"
                    :class="{ 'is-invalid': errors[0] }"
                    v-model="email"
                  />
                  <span class="invalid-feedback">{{ errors[0] }}</span>
                </div>
              </ValidationProvider>
              <ValidationProvider name="Password" rules="required|min:6" v-slot="{ errors }">
                <div class="form-group">
                  <label>Password</label>
                  <input
                    type="password"
                    class="form-control"
                    :class="{ 'is-invalid': errors[0] }"
                    v-model="password"
                  />
                  <span class="invalid-feedback">{{ errors[0] }}</span>
                </div>
              </ValidationProvider>
              <small class="form-text text-muted"
                >By continuing, you agree to Simplecoding's Conditions of Use and
                Privacy Notice.</small
              >
              <button type="submit" class="btn btn-primary mt-2 py-0" :disabled="invalid || loading">
                Continue
                <div
                  v-if="loading"
                  class="spinner-border spinner-border-sm"
                  role="status"
                >
                  <span class="sr-only">Loading...</span>
                </div>
              </button>
            </form>
          </ValidationObserver>
          <hr />
          <small class="form-text text-muted pt-2 pl-4 text-center"
            >New to Simplecoding?</small
          >
          <p class="text-center">
            <router-link
              :to="{ name: 'Signup' }"
              class="btn btn-dark text-center mx-auto px-5 py-1 mb-2"
              >Create Your Simplecoding Account</router-link
            >
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
import swal from "sweetalert";
import { useAuthStore } from "@/stores";

export default {
  name: "Signin",
  setup() {
    const authStore = useAuthStore();
    return { authStore };
  },
  data() {
    return {
      email: null,
      password: null,
    };
  },
  computed: {
    loading() {
      return this.authStore.loading;
    },
  },
  methods: {
    async signin(e) {
      e.preventDefault();

      try {
        await this.authStore.signin(this.email, this.password);
        // Redirect to intended destination or home
        const redirect = this.$route.query.redirect || "/";
        this.$router.push(redirect);
      } catch (error) {
        swal({
          text: "Unable to Log you in!",
          icon: "error",
          closeOnClickOutside: false,
        });
      }
    },
  },
};
</script>
  
  <style scoped>
.btn-dark {
  background-color: #e7e9ec;
  color: #000;
  font-size: smaller;
  border-radius: 0;
  border-color: #adb1b8 #a2a6ac #a2a6ac;
}

.btn-primary {
  background-color: #f0c14b;
  color: black;
  border-color: #a88734 #9c7e31 #846a29;
  border-radius: 0;
}

#logo {
  width: 150px;
}

@media only screen and (min-width: 992px) {
  #signin-div {
    width: 40%;
  }
}
</style>