<template>
  <div class="add-vacation-container">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-lg-8 col-xl-6">
          <div class="vacation-card">
            <!-- Header Section -->
            <div class="card-header">
              <div class="header-content">
                <div class="icon-wrapper">
                  <i class="fas fa-plane"></i>
                </div>
                <div class="header-text">
                  <h1 class="card-title">Request New Vacation</h1>
                  <p class="card-subtitle">Submit your time off request for approval</p>
                </div>
              </div>
            </div>

            <!-- Form Section -->
            <div class="card-body">
              <form @submit.prevent="addVacation" class="vacation-form">
                <!-- User Selection -->
                <div class="form-group">
                  <label for="userId" class="form-label">
                    <i class="fas fa-user label-icon"></i>
                    Select Employee
                  </label>
                  <div class="select-wrapper">
                    <select 
                      id="userId" 
                      class="form-control modern-select" 
                      v-model="userId" 
                      required
                    >
                      <option value="" disabled>Choose an employee...</option>
                      <option 
                        v-for="user of users" 
                        :key="user.id" 
                        :value="user.id"
                      >
                        {{ user.firstName }} {{ user.lastName }} (ID: {{ user.id }})
                      </option>
                    </select>
                    <i class="fas fa-chevron-down select-arrow"></i>
                  </div>
                </div>

                <!-- Date Range Section -->
                <div class="date-range-section">
                  <h3 class="section-title">
                    <i class="fas fa-calendar-alt"></i>
                    Vacation Period
                  </h3>
                  
                  <div class="date-inputs">
                    <div class="form-group">
                      <label for="startDate" class="form-label">
                        <i class="fas fa-calendar-plus label-icon"></i>
                        Start Date
                      </label>
                      <div class="date-input-wrapper">
                        <date-picker 
                          v-model="startDate" 
                          :format="datePickerFormat" 
                          placeholder="Select start date"
                          class="modern-datepicker"
                          required
                        ></date-picker>
                      </div>
                    </div>

                    <div class="date-separator">
                      <i class="fas fa-arrow-right"></i>
                    </div>

                    <div class="form-group">
                      <label for="endDate" class="form-label">
                        <i class="fas fa-calendar-check label-icon"></i>
                        End Date
                      </label>
                      <div class="date-input-wrapper">
                        <date-picker 
                          v-model="endDate" 
                          :format="datePickerFormat" 
                          placeholder="Select end date"
                          class="modern-datepicker"
                          required
                        ></date-picker>
                      </div>
                    </div>
                  </div>

                  <!-- Duration Display -->
                  <div class="duration-display" v-if="startDate && endDate">
                    <div class="duration-card">
                      <i class="fas fa-clock"></i>
                      <span class="duration-text">
                        Duration: {{ calculateDuration() }} days
                      </span>
                    </div>
                  </div>
                </div>

                <!-- Vacation Type -->
                <div class="form-group">
                  <label for="type" class="form-label">
                    <i class="fas fa-tags label-icon"></i>
                    Vacation Type
                  </label>
                  <div class="select-wrapper">
                    <select 
                      id="type" 
                      class="form-control modern-select" 
                      v-model="type" 
                      required
                    >
                      <option value="" disabled>Select vacation type...</option>
                      <option 
                        v-for="schema in schemas" 
                        :key="schema.id" 
                        :value="schema.columnName"
                      >
                        {{ schema.columnName }}
                      </option>
                    </select>
                    <i class="fas fa-chevron-down select-arrow"></i>
                  </div>
                </div>

                <!-- Additional Notes (Optional) -->
                <div class="form-group">
                  <label for="notes" class="form-label">
                    <i class="fas fa-sticky-note label-icon"></i>
                    Additional Notes
                    <span class="optional-text">(Optional)</span>
                  </label>
                  <textarea 
                    id="notes"
                    class="form-control modern-textarea"
                    v-model="notes"
                    placeholder="Add any additional information about your vacation request..."
                    rows="4"
                  ></textarea>
                </div>

                <!-- Action Buttons -->
                <div class="form-actions">
                  <button 
                    type="button" 
                    class="btn btn-secondary btn-cancel"
                    @click="goBack"
                  >
                    <i class="fas fa-times"></i>
                    Cancel
                  </button>
                  
                  <button 
                    type="submit" 
                    class="btn btn-primary btn-submit"
                    :disabled="loading || !isFormValid"
                  >
                    <div v-if="loading" class="spinner-border spinner-border-sm mr-2"></div>
                    <i v-else class="fas fa-paper-plane"></i>
                    {{ loading ? 'Submitting...' : 'Submit Request' }}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DatePicker from "vue2-datepicker";
import axios from "axios";
import swal from "sweetalert";

export default {
  components: {
    DatePicker,
  },
  data() {
    return {
      userId: null,
      startDate: null,
      endDate: null,
      type: null,
      notes: "",
      loading: false,
      token: null,
      datePickerFormat: "YYYY-MM-DD",
      users: [],
      schemas: [],
    };
  },
  computed: {
    isFormValid() {
      return this.userId && this.startDate && this.endDate && this.type;
    }
  },
  methods: {
    async addVacation() {
      if (!this.isFormValid) {
        swal({
          text: "Please fill in all required fields.",
          icon: "warning",
          closeOnClickOutside: false,
        });
        return;
      }

      if (new Date(this.endDate) < new Date(this.startDate)) {
        swal({
          text: "End date cannot be before start date.",
          icon: "warning",
          closeOnClickOutside: false,
        });
        return;
      }

      this.loading = true;

      const newVacation = {
        userId: this.userId,
        startDate: this.startDate,
        endDate: this.endDate,
        type: this.type,
        notes: this.notes || null,
      };

      const baseURL = "http://localhost:9998/";

      try {
        await axios.post(baseURL + "vacation/add", newVacation);
        
        swal({
          text: "Vacation request submitted successfully! An email notification has been sent.",
          icon: "success",
          closeOnClickOutside: false,
        }).then(() => {
          this.$router.push({ name: "Vacation" });
        });
        
      } catch (error) {
        console.error("Error adding vacation:", error);
        swal({
          text: error.response?.data?.message || "Failed to submit vacation request. Please try again.",
          icon: "error",
          closeOnClickOutside: false,
        });
      } finally {
        this.loading = false;
      }
    },

    calculateDuration() {
      if (!this.startDate || !this.endDate) return 0;
      
      const start = new Date(this.startDate);
      const end = new Date(this.endDate);
      const diffTime = Math.abs(end - start);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
      
      return diffDays;
    },

    goBack() {
      this.$router.push({ name: "Vacation" });
    },

    async getUsers() {
      try {
        const res = await axios.get("http://localhost:9998/users/");
        this.users = res.data;
      } catch (error) {
        console.error("Error fetching users:", error);
        swal({
          text: "Failed to load users. Please refresh the page.",
          icon: "error",
          closeOnClickOutside: false,
        });
      }
    },

    async getSchemas() {
      try {
        const res = await axios.get("http://localhost:9998/schema/active/");
        this.schemas = res.data.filter((x) => x.schemaName === "vacation");
      } catch (error) {
        console.error("Error fetching schemas:", error);
        swal({
          text: "Failed to load vacation types. Please refresh the page.",
          icon: "error",
          closeOnClickOutside: false,
        });
      }
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.getUsers();
    this.getSchemas();
  },
};
</script>

<style scoped>
.add-vacation-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

.vacation-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.vacation-card:hover {
  transform: translateY(-5px);
}

.card-header {
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  color: white;
  padding: 30px;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.icon-wrapper {
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.header-text {
  flex: 1;
}

.card-title {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 8px 0;
  color: white;
}

.card-subtitle {
  font-size: 16px;
  margin: 0;
  opacity: 0.9;
}

.card-body {
  padding: 40px;
}

.vacation-form {
  max-width: 100%;
}

.form-group {
  margin-bottom: 30px;
}

.form-label {
  display: flex;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
  color: #2c3e50;
  margin-bottom: 12px;
}

.label-icon {
  margin-right: 8px;
  color: #3498db;
  width: 16px;
}

.optional-text {
  font-size: 14px;
  color: #7f8c8d;
  font-weight: 400;
  margin-left: 8px;
}

.select-wrapper {
  position: relative;
}

.modern-select {
  appearance: none;
  background: white;
  border: 2px solid #e9ecef;
  border-radius: 12px;
  padding: 16px 50px 16px 16px;
  font-size: 16px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.modern-select:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.select-arrow {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #7f8c8d;
  pointer-events: none;
}

.date-range-section {
  background: #f8f9fa;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 30px;
}

.section-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 20px;
}

.section-title i {
  margin-right: 10px;
  color: #3498db;
}

.date-inputs {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  gap: 20px;
  align-items: end;
}

.date-separator {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #7f8c8d;
  font-size: 18px;
  padding-bottom: 20px;
}

.date-input-wrapper {
  position: relative;
}

.modern-datepicker {
  width: 100%;
}

.modern-datepicker input {
  border: 2px solid #e9ecef;
  border-radius: 12px;
  padding: 16px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.modern-datepicker input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.duration-display {
  margin-top: 20px;
}

.duration-card {
  background: white;
  border: 2px solid #e9ecef;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  color: #2c3e50;
}

.duration-card i {
  color: #3498db;
  font-size: 18px;
}

.duration-text {
  font-weight: 600;
  font-size: 16px;
}

.modern-textarea {
  border: 2px solid #e9ecef;
  border-radius: 12px;
  padding: 16px;
  font-size: 16px;
  resize: vertical;
  min-height: 120px;
  transition: all 0.3s ease;
}

.modern-textarea:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.form-actions {
  display: flex;
  gap: 16px;
  justify-content: flex-end;
  margin-top: 40px;
  padding-top: 30px;
  border-top: 1px solid #e9ecef;
}

.btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s ease;
  border: none;
  cursor: pointer;
}

.btn-cancel {
  background: #6c757d;
  color: white;
}

.btn-cancel:hover {
  background: #5a6268;
  transform: translateY(-2px);
}

.btn-submit {
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  color: white;
  min-width: 180px;
  justify-content: center;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(52, 152, 219, 0.3);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Responsive Design */
@media (max-width: 768px) {
  .add-vacation-container {
    padding: 20px 10px;
  }
  
  .card-header {
    padding: 20px;
  }
  
  .header-content {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }
  
  .card-title {
    font-size: 24px;
  }
  
  .card-body {
    padding: 20px;
  }
  
  .date-inputs {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .date-separator {
    transform: rotate(90deg);
    padding: 0;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .btn {
    width: 100%;
    justify-content: center;
  }
}

/* DatePicker Custom Styles */
.mx-datepicker {
  width: 100%;
}

.mx-input {
  border: 2px solid #e9ecef !important;
  border-radius: 12px !important;
  padding: 16px !important;
  font-size: 16px !important;
  height: auto !important;
}

.mx-input:focus {
  border-color: #3498db !important;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1) !important;
}
</style>