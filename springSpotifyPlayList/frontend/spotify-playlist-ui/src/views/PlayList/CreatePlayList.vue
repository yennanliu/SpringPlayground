<template>
    <div>
      <h1>Spotify Authorization</h1>
      <button @click="authorize">Authorize with Spotify</button>
      <div v-if="authorizationInProgress">Authorization in progress...</div>
      <div v-if="authorizationComplete">Authorization complete! Redirecting...</div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        authorizationInProgress: false,
        authorizationComplete: false,
      };
    },
    methods: {
      async authorize() {
        try {
          this.authorizationInProgress = true;
          const response = await fetch('http://localhost:8888/authorize');
          console.log(">>> response = {}", JSON.stringify(response))
          if (!response.ok) {
            throw new Error('Failed to authorize with Spotify');
          }
          this.authorizationComplete = true;
          window.location.href = 'http://localhost:8080/callback'; // Redirect to your frontend callback page
        } catch (error) {
          console.error(error);
          this.authorizationInProgress = false;
          this.authorizationComplete = false;
        }
      },
    },
  };
  </script>
  