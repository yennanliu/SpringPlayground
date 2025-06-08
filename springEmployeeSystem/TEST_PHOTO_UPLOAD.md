# Photo Upload Testing Guide

## Backend Testing

### 1. Start the Backend Server
```bash
cd backend/EmployeeSystem
mvn spring-boot:run
```

### 2. Test Endpoints

#### Upload a photo for user with ID 1:
```bash
curl -X POST \
  http://localhost:9998/users/update/1 \
  -H 'Content-Type: multipart/form-data' \
  -F 'user={"firstName":"John","lastName":"Doe","email":"john@example.com","departementId":1}' \
  -F 'photo=@/path/to/your/image.jpg'
```

#### Retrieve a user photo:
```bash
curl -X GET http://localhost:9998/users/photo/1 --output user_photo.jpg
```

## Frontend Testing

### 1. Start the Frontend Server
```bash
cd frontend/employee-system-ui
npm install
npm run serve
```

### 2. Test Photo Upload Flow

1. **Navigate to Users List**: Go to http://localhost:8080/users/
2. **Edit a User**: Click "Edit" on any user card
3. **Upload Photo**:
   - Click on "Upload User Photo" file input
   - Select an image file (JPEG, PNG, or GIF)
   - You should see a preview of the selected image
   - Click "Submit" to save
4. **Verify Upload**:
   - Go back to the users list
   - The user card should now show their photo instead of initials
   - Click on the user to view their profile page
   - The profile page should display the uploaded photo

### 3. Expected Behavior

- **File Validation**: Only image files (JPEG, PNG, GIF) should be accepted
- **Size Validation**: Files larger than 5MB should be rejected with an error message
- **Photo Preview**: Selected photos should show a preview before upload
- **Photo Display**: Uploaded photos should appear in:
  - User cards in the users list
  - User profile pages
  - User edit forms (showing current photo)
- **Fallback**: If no photo exists or loading fails, show default image or initials

### 4. Error Scenarios to Test

- Try uploading a non-image file (should show error)
- Try uploading a file larger than 5MB (should show error)  
- Try uploading to a non-existent user (should show error)
- Test with slow/failed network (should gracefully fallback to default image)

## Files Modified

### Backend:
- `UserController.java`: Added photo upload endpoint and photo retrieval endpoint
- `UserService.java`: Added methods to handle photo uploads and user updates
- `User.java`: Already had photo field as byte array

### Frontend:
- `EditUser.vue`: Fixed photo upload form and added preview functionality
- `ShowUserDetails.vue`: Updated to display photos from backend endpoint
- `UserBox.vue`: Updated to show user photos in cards instead of just initials 