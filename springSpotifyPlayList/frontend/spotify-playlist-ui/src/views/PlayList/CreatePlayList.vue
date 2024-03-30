<template>
    <div>
      <h1>Spotify Authorization</h1>
      <button @click="authorize">Authorize with Spotify</button>
    </div>
  </template>
  
  <script>
  export default {
    methods: {
      async authorize() {
        try {
          const response = await fetch('http://localhost:8888/authorize');
          console.log(">>> response = {}", JSON.stringify(response))
          console.log(">>> response = {}" + response)
          if (!response.ok) {
            throw new Error('Failed to authorize with Spotify');
          }
          const data = await response.json();
          if (data.url) {
            console.log(">>> data.url = {}" + data.url)
            window.location.href = data.url; // Redirect to the Spotify authorization page
          } else {
            throw new Error('Redirect URI not found in response');
          }
        } catch (error) {
          console.error(error);
          // Handle error
        }
      },
    },
  };
  </script>
  