# Email Service Documentation

## Overview

The Employee System now features a dedicated email service implementation with separate thread pools for asynchronous email processing. This ensures that email operations don't block the main application threads and provides better scalability and performance.

## Architecture

### Thread Pool Configuration

The system now includes three dedicated thread pools configured in `AsyncConfig.java`:

1. **Default Async Executor**
   - Core Pool Size: 5
   - Max Pool Size: 10
   - Queue Capacity: 100
   - Thread Name Prefix: `Default-Async-`

2. **Email Task Executor** (Dedicated for email operations)
   - Core Pool Size: 3
   - Max Pool Size: 8
   - Queue Capacity: 50
   - Thread Name Prefix: `Email-Service-`
   - Keep Alive Time: 60 seconds
   - Rejection Policy: CallerRunsPolicy

3. **Notification Task Executor** (For future notification expansion)
   - Core Pool Size: 2
   - Max Pool Size: 5
   - Queue Capacity: 25
   - Thread Name Prefix: `Notification-Service-`

### Service Layer Architecture

```
VacationService
       ↓
  EmailService (Facade)
       ↓
   MailService (Low-level email operations)
       ↓
  JavaMailSender
```

## Components

### 1. AsyncConfig.java

Enhanced configuration class that provides:
- Multiple thread pools for different async operations
- Proper thread naming for easy debugging
- Graceful shutdown handling
- Rejection policies for when pools are full

### 2. EmailService.java

High-level facade service that provides:
- `sendVacationNotificationAsync()` - User vacation request notifications
- `sendAdminNotificationAsync()` - Admin notifications for new requests
- `sendVacationStatusUpdateAsync()` - Status change notifications
- `sendWelcomeEmailAsync()` - Welcome emails for new users

**Key Features:**
- All methods return `CompletableFuture<Void>` for async handling
- Comprehensive logging with timestamps and thread names
- Proper exception handling without breaking async flow
- Professional email templates

### 3. MailService.java (Enhanced)

Low-level email service that provides:
- Dedicated `@Async("emailTaskExecutor")` annotation for using email thread pool
- Enhanced error handling and logging
- HTML email support
- Retry mechanism support (via `sendMailWithRetry()`)
- Thread-safe operations

### 4. VacationService.java (Updated)

Updated to use the new `EmailService`:
- Removed direct `MailService` dependency
- Now sends both admin and user notifications
- Improved logging and error handling

## Usage Examples

### Sending Vacation Notification

```java
@Autowired
private EmailService emailService;

// Send admin notification
emailService.sendAdminNotificationAsync(
    "admin@company.com",
    userId,
    "Annual Leave",
    "2024-01-15",
    "2024-01-20"
);

// Send user notification
emailService.sendVacationNotificationAsync(
    "user@company.com",
    userId,
    "Annual Leave", 
    "2024-01-15",
    "2024-01-20"
);
```

### Handling Async Results

```java
CompletableFuture<Void> emailFuture = emailService.sendWelcomeEmailAsync(
    "newuser@company.com", "John", "Doe"
);

// Optional: Handle completion
emailFuture.thenRun(() -> {
    log.info("Welcome email sent successfully");
}).exceptionally(throwable -> {
    log.error("Failed to send welcome email: " + throwable.getMessage());
    return null;
});
```

## Configuration Properties

Email service uses existing mail configuration in `application.properties`:

```properties
# Mail Properties
spring.mail.host=smtp.mailtrap.io
spring.mail.port=25
spring.mail.username=your_username
spring.mail.password=your_password
spring.mail.protocol=smtp
```

## Monitoring and Debugging

### Thread Pool Monitoring

The email service logs thread pool information:
```
Email Task Executor configured with core pool size: 3, max pool size: 8, queue capacity: 50
```

### Email Operation Tracking

Each email operation includes detailed logging:
```
[2024-01-15 10:30:15] Starting email sending process in thread: Email-Service-1 for recipient: user@company.com
[2024-01-15 10:30:16] ✅ Email sent successfully in thread: Email-Service-1 to: user@company.com with subject: 'Vacation Request Submitted'
```

### Error Handling

Failed email operations are logged with details:
```
[2024-01-15 10:30:17] ❌ Mail sending failed in thread: Email-Service-2 for recipient: user@company.com - Error: Connection timeout
```

## Benefits

1. **Non-blocking Operations**: Email sending doesn't block main application threads
2. **Scalability**: Dedicated thread pools can handle multiple email operations concurrently
3. **Reliability**: Proper error handling ensures application stability
4. **Monitoring**: Comprehensive logging helps with debugging and monitoring
5. **Flexibility**: Easy to extend with new email types and retry mechanisms
6. **Performance**: Separate thread pools prevent email operations from affecting other async operations

## Future Enhancements

1. **Retry Mechanism**: Implement automatic retry for failed emails
2. **Email Queue**: Add persistent queue for email operations
3. **Templates**: Create HTML email templates with Thymeleaf
4. **Metrics**: Add metrics collection for email service performance
5. **Dead Letter Queue**: Handle permanently failed emails
6. **User Email Lookup**: Integrate with user service to get email addresses

## Testing

The email service includes comprehensive unit tests in `EmailServiceTest.java`:
- Test all async email methods
- Test exception handling
- Test email body construction
- Mock-based testing for isolation

## Security Considerations

1. **Email Validation**: Validate email addresses before sending
2. **Rate Limiting**: Consider implementing rate limiting for email operations
3. **Content Sanitization**: Sanitize email content to prevent injection attacks
4. **Audit Logging**: Log email operations for audit purposes

## Troubleshooting

### Common Issues

1. **Emails not sending**: Check mail server configuration and credentials
2. **Thread pool exhaustion**: Monitor thread pool metrics and adjust sizes if needed
3. **Memory issues**: Monitor queue sizes and processing times
4. **Performance degradation**: Check for blocking operations in async methods

### Debug Commands

Enable debug logging for email operations:
```properties
logging.level.EmployeeSystem.service.EmailService=DEBUG
logging.level.EmployeeSystem.service.MailService=DEBUG
``` 