<template>
    <div class="container">
      <div class="row">
        <div class="col-12 text-center">
          <h4 class="pt-3">Add new Vacation</h4>
        </div>
      </div>
 
      <div class="row">
        <div class="col-3"></div>
        <div class="col-md-6 px-5 px-md-0">
         <form>
           <div class="form-group">
             <label>Start date</label>
             <input type="text" class="form-control" v-model="startDate" required>
           </div>
           <div class="form-group">
             <label>End date</label>
             <input type="text" class="form-control" v-model="endDate" required>
           </div>
           <div class="form-group">
             <label>Type</label>
             <input type="text" class="form-control" v-model="type" required>
           </div>
           <button type="button" class="btn btn-primary" @click="addVacation">Submit</button>
         </form>
        </div>
        <div class="col-3"></div>
      </div>
   </div>
 </template>
 
 <script>
 var axios =  require('axios')
 import swal from 'sweetalert';
 
 export default {
   data(){
     return {
       startDate : null,
       endDate : null,
       type : null,
     }
   },
   methods : {
     async addVacation() {
       const newVacation = {
         startDate : this.startDate,
         endDate : this.endDate,
         type : this.type,
       };
 
       const baseURL =  "http://localhost:9998/";
 
       await axios({
         method: 'post',
         url: baseURL+"vacation/add",
         data : JSON.stringify(newVacation),
         headers: {
           'Content-Type': 'application/json'
         }
       })
       .then(() => {
         swal({
           text: "Vacation Added Successfully!",
           icon: "success",
           closeOnClickOutside: false,
         });
       })
       .catch(err => console.log(err));
     }
   },
   mounted(){
   }
 }
 </script>
 
 <style scoped>
 h4 {
   font-family: 'Roboto', sans-serif;
   color: #484848;
   font-weight: 700;
 }
 </style>