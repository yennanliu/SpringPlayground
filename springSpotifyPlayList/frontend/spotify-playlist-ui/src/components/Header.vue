<template>
    <header class="app-header">
      <h1>Spotify Playlist App</h1>
      <button class="auth-button" @click="authorize">Authorize with Spotify</button>
    </header>
  </template>
  
  <script>
  export default {
    methods: {
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
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .app-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #1db954;
    padding: 10px 20px;
    color: white;
  }
  
  .auth-button {
    background-color: #fff;
    color: #1db954;
    border: none;
    padding: 10px 20px;
    cursor: pointer;
    font-size: 1rem;
    border-radius: 5px;
    transition: background-color 0.3s ease;
  }
  
  .auth-button:hover {
    background-color: #e6e6e6;
  }
  </style>