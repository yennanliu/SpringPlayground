<template>
  <div id="home">
    <!-- Page Wrapper -->
    <div id="background-div" class="page-holder bg-cover">
      <div class="container py-5">
        <header class="text-left text-white py-5">
          <h3 class="mb-4 rounded">
            <a
              href="#start-shopping"
              class="bg-white px-2 py-2 rounded"
              id="heading"
              >Data Platform Service</a
            >
          </h3>
          <p id="content" class="lead mb-0 bg-dark p-1 rounded">
            Apache Flink Jar, SQL Job submit, Jar upload, resource management.
          </p>
        </header>
      </div>
    </div>

    <div id="start-shopping" class="container">
      <div class="row">
        <div class="col-12 text-left">
          <h2 class="pt-3">Announcement</h2>
          <h6>hackson timeline</h6>
          <h6>2024 RTO plan</h6>
          <h6>blblblbla</h6>
        </div>
      </div>
      <div class="row">
        <!-- <div
          v-for="index in this.category_size"
          :key="index"
          class="col-md-6 col-xl-4 col-12 pt-3 justify-content-around d-flex"
        >
          <CategoryBox :category="categories[index - 1]"> </CategoryBox>
        </div> -->
      </div>
    </div>

    <hr />
    <div class="container">
      <div class="row">
        <div class="col-12 text-left">
          <h2 class="pt-3">News</h2>
          <h6>news 123</h6>
          <h6>blblblbla</h6>
        </div>
      </div>
    </div>
  </div>
</template>
  
  <script>
export default {
  name: "Home",
  //   components : { UserBox, DepartmentBox},
  props: ["baseURL"],
  data() {
    return {};
  },
  mounted() {},
};
</script>
  
  <style>
.page-holder {
  min-height: 100vh;
}

.bg-cover {
  background-size: cover !important;
}

#background-div {
  background: url(../assets/home5.jpeg);
}

#heading {
  text-decoration: none;
  font-family: "Roboto", sans-serif;
  font-weight: 400;
  opacity: 0.8;
  font-family: "Josefin Sans", sans-serif;
}

#content {
  opacity: 0.8;
}

h2 {
  font-family: "Josefin Sans", sans-serif;
}
</style>