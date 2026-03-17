<!--
    https://github.com/webtutsplus/ecommerce-vuejs/blob/master/src/views/Signup.vue
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
        <div id="signup-div" class="flex-item border">
          <h2 class="pt-4 pl-4">Create Account</h2>
          <ValidationObserver ref="form" v-slot="{ handleSubmit, invalid }">
            <form @submit.prevent="handleSubmit(signup)" class="pt-4 pl-4 pr-4">
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
              <div class="form-row">
                <div class="col">
                  <ValidationProvider name="First Name" rules="required" v-slot="{ errors }">
                    <div class="form-group">
                      <label>First Name</label>
                      <input
                        type="text"
                        class="form-control"
                        :class="{ 'is-invalid': errors[0] }"
                        v-model="firstName"
                      />
                      <span class="invalid-feedback">{{ errors[0] }}</span>
                    </div>
                  </ValidationProvider>
                </div>
                <div class="col">
                  <ValidationProvider name="Last Name" rules="required" v-slot="{ errors }">
                    <div class="form-group">
                      <label>Last Name</label>
                      <input
                        type="text"
                        class="form-control"
                        :class="{ 'is-invalid': errors[0] }"
                        v-model="lastName"
                      />
                      <span class="invalid-feedback">{{ errors[0] }}</span>
                    </div>
                  </ValidationProvider>
                </div>
              </div>
              <ValidationProvider name="Password" rules="required|min:6" vid="password" v-slot="{ errors }">
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
              <ValidationProvider name="Confirm Password" rules="required|confirmed:password" v-slot="{ errors }">
                <div class="form-group">
                  <label>Confirm Password</label>
                  <input
                    type="password"
                    class="form-control"
                    :class="{ 'is-invalid': errors[0] }"
                    v-model="passwordConfirm"
                  />
                  <span class="invalid-feedback">{{ errors[0] }}</span>
                </div>
              </ValidationProvider>
              <button type="submit" class="btn btn-primary mt-2 py-0" :disabled="invalid || loading">
                Create Account
                <div v-if="loading" class="spinner-border spinner-border-sm" role="status">
                  <span class="sr-only">Loading...</span>
                </div>
              </button>
            </form>
          </ValidationObserver>
          <hr />
          <small class="form-text text-muted pt-2 pl-4 text-center"
            >Already Have an Account?</small
          >
          <p class="text-center">
            <router-link
              class="btn btn-dark text-center mx-auto px-5 py-1 mb-2"
              :to="{ name: 'Signin' }"
              >Signin Here</router-link
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
  name: "Signup",
  setup() {
    const authStore = useAuthStore();
    return { authStore };
  },
  data() {
    return {
      email: null,
      firstName: null,
      lastName: null,
      password: null,
      passwordConfirm: null,
    };
  },
  computed: {
    loading() {
      return this.authStore.loading;
    },
  },
  methods: {
    async signup() {
      try {
        await this.authStore.signup({
          email: this.email,
          firstName: this.firstName,
          lastName: this.lastName,
          password: this.password,
        });
        this.$router.replace("/");
        swal({
          text: "User signup successful. Please Login",
          icon: "success",
          closeOnClickOutside: false,
        });
      } catch (error) {
        swal({
          text: "Signup failed. Please try again.",
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
  #signup-div {
    width: 40%;
  }
}
</style>