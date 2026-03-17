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
          <Form @submit="signup" class="pt-4 pl-4 pr-4" v-slot="{ meta }">
            <div class="form-group">
              <label>Email</label>
              <Field
                name="email"
                type="email"
                class="form-control"
                v-model="email"
                rules="required|email"
              />
              <ErrorMessage name="email" class="invalid-feedback d-block" />
            </div>
            <div class="form-row">
              <div class="col">
                <div class="form-group">
                  <label>First Name</label>
                  <Field
                    name="firstName"
                    type="text"
                    class="form-control"
                    v-model="firstName"
                    rules="required"
                  />
                  <ErrorMessage name="firstName" class="invalid-feedback d-block" />
                </div>
              </div>
              <div class="col">
                <div class="form-group">
                  <label>Last Name</label>
                  <Field
                    name="lastName"
                    type="text"
                    class="form-control"
                    v-model="lastName"
                    rules="required"
                  />
                  <ErrorMessage name="lastName" class="invalid-feedback d-block" />
                </div>
              </div>
            </div>
            <div class="form-group">
              <label>Password</label>
              <Field
                name="password"
                type="password"
                class="form-control"
                v-model="password"
                rules="required|min:6"
              />
              <ErrorMessage name="password" class="invalid-feedback d-block" />
            </div>
            <div class="form-group">
              <label>Confirm Password</label>
              <Field
                name="passwordConfirm"
                type="password"
                class="form-control"
                v-model="passwordConfirm"
                rules="required|confirmed:@password"
              />
              <ErrorMessage name="passwordConfirm" class="invalid-feedback d-block" />
            </div>
            <button type="submit" class="btn btn-primary mt-2 py-0" :disabled="!meta.valid || loading">
              Create Account
              <div v-if="loading" class="spinner-border spinner-border-sm" role="status">
                <span class="sr-only">Loading...</span>
              </div>
            </button>
          </Form>
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

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Form, Field, ErrorMessage } from 'vee-validate'
import swal from "sweetalert"
import { useAuthStore } from "@/stores"

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const firstName = ref('')
const lastName = ref('')
const password = ref('')
const passwordConfirm = ref('')

const loading = computed(() => authStore.loading)

const signup = async () => {
  try {
    await authStore.signup({
      email: email.value,
      firstName: firstName.value,
      lastName: lastName.value,
      password: password.value,
    })
    router.replace("/")
    swal({
      text: "User signup successful. Please Login",
      icon: "success",
      closeOnClickOutside: false,
    })
  } catch (error) {
    swal({
      text: "Signup failed. Please try again.",
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
  #signup-div {
    width: 40%;
  }
}
</style>
