<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center">
        <h4 class="pt-3">Edit User</h4>
      </div>
    </div>

    <div class="row">
      <div class="col-3"></div>
      <div class="col-md-6 px-5 px-md-0">
        <form v-if="user">
          <div class="form-group">
            <label>First Name</label>
            <input type="text" class="form-control" v-model="user.firstName" required />
          </div>
          <div class="form-group">
            <label>Last Name</label>
            <input type="text" class="form-control" v-model="user.lastName" required />
          </div>
          <div class="form-group">
            <label>Email</label>
            <input type="text" class="form-control" v-model="user.email" required />
          </div>

          <div class="form-group">
            <label>Department</label>
            <select class="form-control" v-model="user.departmentId" required>
              <option v-for="department of departments" :key="department.id" :value="department.id">
                ID : {{ department.id }} Name : {{ department.name }}
              </option>
            </select>
          </div>

          <!-- File input for user photo -->
          <div class="form-group">
            <label>Upload User Photo</label>
            <input 
              type="file" 
              ref="fileInput" 
              @change="handleFileUpload($event)" 
              accept="image/*" 
              class="form-control-file"
            />
            
            <!-- Photo preview -->
            <div v-if="photoPreview || existingPhotoUrl" class="photo-preview-container mt-3">
              <div class="photo-preview">
                <img 
                  :src="photoPreview || existingPhotoUrl" 
                  alt="Photo Preview" 
                  class="preview-image"
                />
                <div class="photo-info">
                  <small v-if="photoFile">
                    Selected: {{ photoFile.name }} ({{ formatFileSize(photoFile.size) }})
                  </small>
                  <small v-else-if="existingPhotoUrl">
                    Current user photo
                  </small>
                </div>
              </div>
            </div>
          </div>

          <button type="button" class="btn btn-primary" @click="editUser">
            Submit
          </button>
        </form>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import swal from "sweetalert";

export default {
  data() {
    return {
      user: null,
      departments: [],
      photoFile: null, // To store selected photo file
      photoPreview: null, // To store photo preview URL
      photoError: false,
    };
  },
  computed: {
    existingPhotoUrl() {
      if (this.photoError || !this.user || !this.user.id) {
        return null;
      }
      return `http://localhost:9998/users/photo/${this.user.id}`;
    }
  },
  methods: {
    async editUser() {
      // Create form data to send both user data and photo file
      const formData = new FormData();
      formData.append("user", JSON.stringify(this.user)); // Convert user object to JSON string
      
      if (this.photoFile) {
        formData.append("photo", this.photoFile); // Append photo file
        console.log(">>> Photo file attached:", this.photoFile.name, "Size:", this.photoFile.size);
      }

      console.log(">>> (editUser) formData entries:");
      for (let pair of formData.entries()) {
        console.log(pair[0] + ': ' + pair[1]);
      }

      try {
        const response = await axios.post(
          `http://localhost:9998/users/update/${this.user.id}`, 
          formData, 
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );
        
        console.log("Update response:", response);
        this.$emit("fetchData");
        this.$router.push({ name: "AdminUser" });
        swal({
          text: "User Updated Successfully!",
          icon: "success",
          closeOnClickOutside: false,
        });
      } catch (err) {
        console.error("Error updating user:", err);
        swal({
          text: "Failed to update user. Please try again.",
          icon: "error",
          closeOnClickOutside: false,
        });
      }
    },

    async getUser() {
      await axios
        .get(`http://localhost:9998/users/${this.$route.params.id}`)
        .then((res) => {
          this.user = res.data;
        })
        .catch((err) => {
          console.error("Error fetching user:", err);
        });
    },

    async getDepartments() {
      await axios
        .get("http://localhost:9998/dep/")
        .then((res) => {
          this.departments = res.data;
        })
        .catch((err) => {
          console.error("Error fetching departments:", err);
        });
    },

    // Method to handle file selection and update v-model
    handleFileUpload(event) {
      console.log(">>> handleFileUpload, event target:", event.target);
      
      const file = event.target.files[0];
      if (file) {
        // Validate file type
        const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'];
        if (!allowedTypes.includes(file.type)) {
          swal({
            text: "Please select a valid image file (JPEG, PNG, or GIF).",
            icon: "error",
            closeOnClickOutside: false,
          });
          // Reset the file input
          this.$refs.fileInput.value = '';
          this.photoFile = null;
          return;
        }
        
        // Validate file size (limit to 5MB)
        const maxSize = 5 * 1024 * 1024; // 5MB in bytes
        if (file.size > maxSize) {
          swal({
            text: "File size must be less than 5MB.",
            icon: "error",
            closeOnClickOutside: false,
          });
          // Reset the file input
          this.$refs.fileInput.value = '';
          this.photoFile = null;
          return;
        }
        
        this.photoFile = file;
        
        // Create preview URL
        const reader = new FileReader();
        reader.onload = (e) => {
          this.photoPreview = e.target.result;
        };
        reader.readAsDataURL(file);
        
        console.log(">>> Photo file selected:", file.name, "Size:", file.size, "Type:", file.type);
      } else {
        this.photoFile = null;
        this.photoPreview = null;
      }
    },

    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes';
      const k = 1024;
      const sizes = ['Bytes', 'KB', 'MB', 'GB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },
  },

  mounted() {
    this.getDepartments();
    this.getUser();
  },
};
</script>

<style scoped>
h4 {
  font-family: "Roboto", sans-serif;
  color: #484848;
  font-weight: 700;
}

.photo-preview-container {
  display: flex;
  justify-content: center;
}

.photo-preview {
  text-align: center;
  border: 2px dashed #dee2e6;
  border-radius: 8px;
  padding: 15px;
  background-color: #f8f9fa;
}

.preview-image {
  max-width: 200px;
  max-height: 200px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  object-fit: cover;
}

.photo-info {
  margin-top: 10px;
}

.photo-info small {
  color: #6c757d;
  font-style: italic;
}

.form-control-file {
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
  padding: 0.375rem 0.75rem;
  width: 100%;
}
</style>