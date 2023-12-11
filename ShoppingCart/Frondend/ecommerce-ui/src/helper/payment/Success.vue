<template>
  <div class="text-center">
    <div class="spinner-border" role="status">
      <span class="sr-only">Loading...</span>
    </div>
  </div>
</template>

<script>
var axios = require("axios");
export default {
  name: "PaymentSuccess",
  props: ["baseURL"],
  data() {
    return {
      token: null,
      sessionId: null,
    };
  },
  methods: {
    saveOrder() {
      axios
        .post(
          this.baseURL +
            "order/add/?token=" +
            this.token +
            "&sessionId=" +
            this.sessionId
        )
        .then(() => {
          window.location.href = "/order";
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.sessionId = localStorage.getItem("sessionId");
    console.log(">>> (ShowDetails.vue) this.sessionId = " + this.sessionId);
    this.saveOrder();
  },
};
</script>
