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
            Empower your data processing capabilities with Apache Flink, a cutting-edge platform that excels in handling data-intensive tasks. Seamlessly submit SQL jobs, upload JAR files, and efficiently manage resources to unlock the full potential of your data processing workflows. Elevate your data-driven applications to new heights with Flink's powerful features and robust resource management capabilities
          </p>
        </header>
      </div>
    </div>

    <div id="start-shopping" class="container">
      <div class="row">
        <div class="col-12 text-left">
          <h2 class="pt-3">Reference</h2>
          <a href="https://github.com/yennanliu/SpringPlayground/tree/main/SpringDataPlatform" target="_blank">Project github</a><br>
          <a href="https://flink.apache.org/" target="_blank">Apache Flink</a><br>
          <a href="https://spark.apache.org/" target="_blank">pache Spark</a>
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
          <h2 class="pt-3">Links</h2>
          <a href="http://localhost:8081/#/overview" target="_blank">Flink UI</a><br>
          <a href="http://localhost:9080/#/" target="_blank">Zeppelin UI</a><br>
          <a href="http://localhost:9999/swagger-ui.html" target="_blank">Swagger UI</a>
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