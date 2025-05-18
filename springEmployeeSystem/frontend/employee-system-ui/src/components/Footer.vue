<template>
  <footer>
    <div class="container">
      <hr />
      <div class="footer-content">
        <div class="row">
          <div class="col-md-3 col-sm-6 mb-4">
            <h5>Support</h5>
            <ul>
              <li><a href="#">Help Center</a></li>
              <li><a href="#">Safety Information</a></li>
              <li><a href="#">Cancellation Options</a></li>
              <li><a href="#">Our COVID-19 Response</a></li>
            </ul>
          </div>

          <div class="col-md-3 col-sm-6 mb-4">
            <h5>Community</h5>
            <ul>
              <li><a href="#">Employee Forum</a></li>
              <li><a href="#">Team Building</a></li>
              <li><a href="#">Careers</a></li>
              <li><a href="#">Newsroom</a></li>
            </ul>
          </div>

          <div class="col-md-3 col-sm-6 mb-4">
            <h5>Department</h5>
            <ul>
              <li><a href="#">Engineering</a></li>
              <li><a href="#">Product</a></li>
              <li><a href="#">Marketing</a></li>
              <li><a href="#">Finance</a></li>
            </ul>
          </div>

          <div class="col-md-3 col-sm-6 mb-4">
            <h5>About</h5>
            <ul>
              <li><a href="#">Our Story</a></li>
              <li><a href="#">Leadership</a></li>
              <li><a href="#">Investors</a></li>
              <li><a href="#">Partners</a></li>
            </ul>
          </div>
        </div>
      </div>

      <div class="footer-bottom">
        <div class="d-flex flex-wrap justify-content-between align-items-center py-3">
          <div class="footer-copyright">
            <p>© 2023 Employee System, Inc.</p>
          </div>
          <div class="footer-social d-flex">
            <a href="#" class="me-3"><i class="bi bi-facebook"></i></a>
            <a href="#" class="me-3"><i class="bi bi-twitter"></i></a>
            <a href="#" class="me-3"><i class="bi bi-instagram"></i></a>
            <a href="#" class="me-3"><i class="bi bi-linkedin"></i></a>
          </div>
          <div class="footer-links d-flex">
            <a href="#" class="me-3">Privacy</a>
            <a href="#" class="me-3">Terms</a>
            <a href="#" class="me-3">Company Details</a>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>
  
<script>
export default {
  name: "Footer",
};
</script>
  
<style scoped>
footer {
  color: var(--airbnb-dark);
  font-size: 14px;
  padding: 24px 0;
  background-color: var(--airbnb-white);
}

h5 {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 16px;
}

ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

li {
  margin-bottom: 12px;
}

a {
  color: var(--airbnb-light);
  text-decoration: none;
  transition: var(--transition);
}

a:hover {
  color: var(--airbnb-dark);
  text-decoration: underline;
}

.footer-content {
  padding: 24px 0 48px;
}

.footer-bottom {
  padding-top: 24px;
  border-top: 1px solid #DDDDDD;
}

.footer-copyright p {
  color: var(--airbnb-light);
  margin-bottom: 0;
}

.footer-social a {
  font-size: 18px;
  margin-right: 16px;
}

.footer-links a {
  margin-left: 16px;
}

hr {
  border-color: #DDDDDD;
  margin: 0;
}
</style>