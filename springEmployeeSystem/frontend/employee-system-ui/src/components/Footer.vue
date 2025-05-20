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
            <p>© 2023 Employee System, Inc. All rights reserved.</p>
          </div>
          <div class="footer-social">
            <a href="#" class="social-icon">
              <i class="bi bi-facebook"></i>
            </a>
            <a href="#" class="social-icon">
              <i class="bi bi-twitter"></i>
            </a>
            <a href="#" class="social-icon">
              <i class="bi bi-instagram"></i>
            </a>
            <a href="#" class="social-icon">
              <i class="bi bi-linkedin"></i>
            </a>
          </div>
          <div class="footer-links">
            <a href="#">Privacy</a>
            <a href="#">Terms</a>
            <a href="#">Company Details</a>
          </div>
        </div>
        <div class="footer-app">
          <p>Download our app</p>
          <div class="app-buttons">
            <a href="#" class="app-btn">
              <i class="bi bi-apple"></i>
              <span>App Store</span>
            </a>
            <a href="#" class="app-btn">
              <i class="bi bi-google-play"></i>
              <span>Google Play</span>
            </a>
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
  font-size: 15px;
  padding: 40px 0;
  background-color: var(--airbnb-white);
}

h5 {
  font-size: 16px;
  font-weight: 700;
  margin-bottom: 20px;
  color: var(--airbnb-dark);
}

ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

li {
  margin-bottom: 16px;
}

a {
  color: var(--airbnb-light);
  text-decoration: none;
  transition: var(--transition);
  font-size: 15px;
  font-weight: 500;
}

a:hover {
  color: var(--airbnb-primary);
  text-decoration: none;
}

.footer-content {
  padding: 32px 0 48px;
}

.footer-bottom {
  padding-top: 32px;
  border-top: 1px solid #DDDDDD;
}

.footer-copyright p {
  color: var(--airbnb-light);
  margin-bottom: 0;
  font-size: 15px;
}

.footer-social {
  display: flex;
  align-items: center;
  gap: 16px;
}

.social-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--airbnb-bg);
  color: var(--airbnb-dark);
  transition: var(--transition);
}

.social-icon i {
  font-size: 20px;
}

.social-icon:hover {
  transform: translateY(-3px);
  background-color: var(--airbnb-primary);
  color: white;
}

.footer-links {
  display: flex;
  gap: 24px;
}

.footer-links a {
  font-weight: 600;
}

.footer-app {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 32px;
}

.footer-app p {
  color: var(--airbnb-dark);
  font-weight: 600;
  margin-bottom: 16px;
}

.app-buttons {
  display: flex;
  gap: 16px;
}

.app-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--airbnb-dark);
  color: white;
  border-radius: 12px;
  padding: 10px 16px;
  font-weight: 600;
  transition: var(--transition);
}

.app-btn:hover {
  background-color: var(--airbnb-primary);
  color: white;
  transform: translateY(-3px);
}

.app-btn i {
  font-size: 20px;
  margin-right: 8px;
}

hr {
  border-color: #DDDDDD;
  margin: 0;
}

@media (max-width: 768px) {
  .footer-bottom .d-flex {
    flex-direction: column;
    gap: 20px;
    align-items: center;
    text-align: center;
  }
  
  .footer-links {
    margin-top: 8px;
  }
}
</style>