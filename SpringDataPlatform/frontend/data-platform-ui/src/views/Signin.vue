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
          <Form @submit="signin" class="pt-4 pl-4 pr-4" v-slot="{ meta }">
            <div class="form-group">
              <label>Email</label>
              <Field
                name="email"
                type="email"
                class="form-control"
                :class="{ 'is-invalid': errors.email }"
                v-model="email"
                rules="required|email"
              />
              <ErrorMessage name="email" class="invalid-feedback" />
            </div>
            <div class="form-group">
              <label>Password</label>
              <Field
                name="password"
                type="password"
                class="form-control"
                :class="{ 'is-invalid': errors.password }"
                v-model="password"
                rules="required|min:6"
              />
              <ErrorMessage name="password" class="invalid-feedback" />
            </div>
            <small class="form-text text-muted"
              >By continuing, you agree to Simplecoding's Conditions of Use and
              Privacy Notice.</small
            >
            <button type="submit" class="btn btn-primary mt-2 py-0" :disabled="!meta.valid || loading">
              Continue
              <div
                v-if="loading"
                class="spinner-border spinner-border-sm"
                role="status"
              >
                <span class="sr-only">Loading...</span>
              </div>
            </button>
          </Form>
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

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Form, Field, ErrorMessage } from 'vee-validate'
import swal from "sweetalert"
import { useAuthStore } from "@/stores"

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const errors = ref({})

const loading = computed(() => authStore.loading)

const signin = async () => {
  try {
    await authStore.signin(email.value, password.value)
    const redirect = route.query.redirect || "/"
    router.push(redirect)
  } catch (error) {
    swal({
      text: "Unable to Log you in!",
      icon: "error",
      closeOnClickOutside: false,
    })
  }
}
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
