# Unit Tests for Photo Upload Functionality

## Overview

This document describes the comprehensive unit tests added for the user photo upload functionality in the backend. The tests cover both the service layer (`UserService`) and controller layer (`UserController`).

## Test Coverage

### UserService Tests

#### 1. **Photo Upload Tests (`UserServiceTest.java`)**

- `testUpdateUserWithPhoto_Success()` - Tests successful photo upload with valid file
- `testUpdateUserWithPhoto_NoPhoto()` - Tests user update without photo
- `testUpdateUserWithPhoto_EmptyPhoto()` - Tests handling of empty photo files
- `testUpdateUserWithPhoto_UserNotFound()` - Tests error handling when user doesn't exist
- `testUpdateUserWithPhoto_PhotoProcessingError()` - Tests IOException handling during photo processing

#### 2. **User Update Tests**

- `testUpdateUser_Success()` - Tests complete user field updates
- `testUpdateUser_PartialUpdate()` - Tests partial updates (only non-null fields)
- `testUpdateUser_UserNotFound()` - Tests error handling for non-existent users

### UserController Tests

#### 1. **Photo Upload Endpoint Tests (`UserControllerTest.java`)**

- `testUpdateUserWithPhoto_Success()` - Tests successful multipart form submission
- `testUpdateUserWithPhoto_NoPhoto()` - Tests update without photo attachment
- `testUpdateUserWithPhoto_InvalidJson()` - Tests handling of malformed JSON
- `testUpdateUserWithPhoto_ServiceException()` - Tests service layer error propagation

#### 2. **Photo Retrieval Endpoint Tests**

- `testGetUserPhoto_Success()` - Tests successful photo retrieval
- `testGetUserPhoto_UserNotFound()` - Tests 404 response for non-existent users
- `testGetUserPhoto_NoPhotoData()` - Tests 404 response when user has no photo
- `testGetUserPhoto_ServiceException()` - Tests error handling for service exceptions

## Running the Tests

### Prerequisites

```bash
cd backend/EmployeeSystem
```

### Run All Tests

```bash
mvn test
```

### Run Specific Test Classes

```bash
# Run UserService tests only
mvn test -Dtest=UserServiceTest

# Run UserController tests only
mvn test -Dtest=UserControllerTest
```

### Run Specific Test Methods

```bash
# Run specific photo upload tests
mvn test -Dtest=UserServiceTest#testUpdateUserWithPhoto_Success
mvn test -Dtest=UserControllerTest#testGetUserPhoto_Success
```

### Generate Test Coverage Report

```bash
mvn jacoco:report
```

The coverage report will be available at `target/site/jacoco/index.html`

## Test Scenarios Covered

### ✅ **Happy Path Scenarios**

1. **Successful photo upload** - Valid user, valid photo file
2. **User update without photo** - Update user fields without changing photo
3. **Photo retrieval** - Successfully retrieve existing user photo

### ✅ **Edge Cases**

1. **Empty photo file** - Handling of empty MultipartFile
2. **Null photo parameter** - Handling when no photo is provided
3. **Partial user updates** - Only updating non-null fields
4. **Missing photo data** - User exists but has no photo

### ✅ **Error Scenarios**

1. **User not found** - Attempting to update non-existent user
2. **File processing errors** - IOException during photo processing
3. **Invalid JSON** - Malformed user data in multipart request
4. **Service exceptions** - Database or other service layer errors

## Test Implementation Details

### **Mocking Strategy**

- **UserRepository** - Mocked to simulate database operations
- **MultipartFile** - Mocked to simulate file uploads without actual files
- **AuthenticationService** - Mocked for authentication-related operations

### **Verification Points**

- **Method calls** - Verify correct service/repository methods are called
- **Data integrity** - Verify user fields are updated correctly
- **Photo handling** - Verify photo bytes are stored/retrieved correctly
- **Error propagation** - Verify exceptions are handled and propagated correctly

### **Test Data**

- **Photo bytes** - Simple byte arrays simulating image data
- **User data** - Complete and partial UserCreateDto objects
- **Mock responses** - Proper HTTP status codes and response bodies

## Expected Results

All tests should pass, demonstrating:

- ✅ Photo upload functionality works correctly
- ✅ Error handling is robust and appropriate
- ✅ User field updates work as expected
- ✅ Photo retrieval returns correct data and status codes
- ✅ Edge cases are handled gracefully

## Integration with CI/CD

These tests can be integrated into your CI/CD pipeline to ensure photo upload functionality remains stable across deployments:

```yaml
# Example GitHub Actions step
- name: Run Backend Tests
  run: |
    cd backend/EmployeeSystem
    mvn test
```

## Troubleshooting

### Common Issues

1. **Mock setup errors** - Ensure all required mocks are properly configured
2. **IOException handling** - Verify proper exception handling in test methods
3. **ApiResponse methods** - Use `isSuccess()` instead of `getSuccess()`

### Debugging Tests

```bash
# Run tests with debug output
mvn test -X

# Run specific failing test with verbose output
mvn test -Dtest=UserServiceTest#testUpdateUserWithPhoto_Success -X
``` 