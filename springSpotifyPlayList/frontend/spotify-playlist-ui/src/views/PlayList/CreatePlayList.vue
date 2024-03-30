<template>
  <div>
    <h1>Spotify Authorization</h1>
    <button @click="authorize">Authorize with Spotify</button>
    <button v-if="authorized" @click="createPlaylist">Create Playlist</button>
    <div v-if="playlistCreated">Playlist created successfully!</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      authorized: false,
      playlistCreated: false,
      accessToken: null,
    };
  },
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

          // create a playlist
          this.createPlaylist();
        } else {
          throw new Error("Redirect URI not found in response");
        }
      } catch (error) {
        console.error(error);
        // Handle error
      }
    },

    async createPlaylist() {
      try {
        console.log("createPlaylist start");
        console.log(">>> this.accessToken = " + this.accessToken);
        //await sleep(3000);

        const response = await fetch("http://localhost:8888/playlist/create", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + this.accessToken, // Use the accessToken obtained after authorization
          },
          body: JSON.stringify({
            userId: "someId",
            name: "someName",
          }),
        });
        if (!response.ok) {
          throw new Error("Failed to create playlist");
        }
        this.playlistCreated = true;
      } catch (error) {
        console.error(error);
        // Handle error
      }
    },

  },
  mounted() {
    // Check if the URL contains an access token (after the Spotify authorization redirect)
    const urlParams = new URLSearchParams(window.location.search);
    /** 
     *  Auth code can be received after redirect successfully 
     * 
     *  e.g. :
     *  http://localhost:8888/authorized-url?code=AQCh8TjtviCMIV_jMym59siQGiC_FcuoAjvPQmzgu2jrfv542QS9ftfwZEnYi3cacXfQVoq-QEk4nwKmdt3d3EjXEu3Dw7i6poRcM3_uj-CFhMbV80qwqjv4LZyvufzMk1VcyuBZi2B6qgUsaQD8EGcuzSlB2vAhT2qjGskaEEJsCE1IZMMXI58wK2UDL0B1pFsjqhDV861B0HWN-PGUIA
     * 
     */
    const accessToken = urlParams.get("access_token");
    if (accessToken) {
      this.accessToken = accessToken;
      console.log(">>> accessToken = " + accessToken)
      this.authorized = true;
    }
  },
};
</script>
