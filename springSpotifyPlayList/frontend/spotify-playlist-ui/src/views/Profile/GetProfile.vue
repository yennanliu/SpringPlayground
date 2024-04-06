<template>
  <div>
    <h1>User Profile</h1>
    <pre v-if="userProfile">{{ JSON.stringify(userProfile, null, 2) }}</pre>
    <div v-else>Loading...</div>
  </div>
</template>
  
  <script>
export default {
  data() {
    return {
      userProfile: null,
      authCode: null,
    };
  },
  mounted() {
    this.getProfile();
  },
  methods: {
    async getProfile() {
      try {
        // get auth code
        console.log("auth start");
        this.authorize();
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get("code");
        if (!code) {
          throw new Error("Authorization code not found");
        }
        this.authCode = code;

        const response = await fetch("http://localhost:8888/profile/");
        if (!response.ok) {
          throw new Error("Failed to fetch album");
        }
        const data = await response.json();
        this.userProfile = data;
      } catch (error) {
        console.error(error);
      }
    },

    async authorize() {
      try {
        const response = await fetch("http://localhost:8888/authorize");
        if (!response.ok) {
          throw new Error("Failed to authorize with Spotify");
        }
        const data = await response.json();
        if (data.url) {
          window.location.href = data.url; // Redirect to the Spotify authorization page
        } else {
          throw new Error("Redirect URI not found in response");
        }
      } catch (error) {
        console.error(error);
        // Handle error
      }
    },
  },
};
</script>
  