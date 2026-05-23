# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a full-stack Spotify playlist application with ML-based song recommendations. The application integrates with the Spotify Web API to provide features like song search, playlist creation, and intelligent music recommendations.

**Architecture:**
- Backend: Spring Boot (Java 8) with Maven
- Frontend: Vue.js 2 with Vue CLI
- Deployment: Docker Compose
- Integration: Spotify Web API via spotify-web-api-java library

## Development Commands

### Backend (Spring Boot)
```bash
# Navigate to backend directory
cd backend/SpotifyPlayList

# Build the application
mvn clean package

# Run tests
mvn test

# Run the application locally
mvn spring-boot:run

# Skip tests during build
mvn clean package -DskipTests

# Run specific test class
mvn test -Dtest=AlbumControllerTest
```

### Frontend (Vue.js)
```bash
# Navigate to frontend directory
cd frontend/spotify-playlist-ui

# Install dependencies
npm install

# Run development server
npm run serve

# Build for production
npm run build

# Run linter
npm run lint
```

### Docker Development
```bash
# Set required environment variables
export SPOTIFY_CLIENT_SECRET=<your_spotify_client_secret>
export SPOTIFY_REDIRECT_URL=http://<server_ip>:8080/playlist
export VUE_APP_BASE_URL=http://localhost:8888/

# Run full stack application
docker-compose up

# For EC2 deployment with sudo
sudo -E docker-compose up

# Clean up Docker resources
docker rm -f $(docker ps -aq)
docker rmi -f $(docker images -q)
docker system prune
```

## Application Configuration

### Required Spotify API Setup
1. Register at [Spotify Developer Platform](https://developer.spotify.com/documentation/web-api)
2. Update `spotify.client.secret` and `spotify.client.id` in `backend/SpotifyPlayList/src/main/resources/application.properties`
3. Configure redirect URL in Spotify app settings to match `spotify.redirect.url`
4. Update `baseURL` in `frontend/spotify-playlist-ui/src/App.vue` for frontend API calls

### Key Configuration Files
- **Backend Config:** `backend/SpotifyPlayList/src/main/resources/application.properties`
- **Frontend Config:** `frontend/spotify-playlist-ui/src/App.vue` (baseURL configuration)
- **Docker Config:** `docker-compose.yml` with environment variable mappings

## Project Structure

### Backend Architecture
- **Controllers:** Handle HTTP requests and OAuth flow (`controller/` package)
- **Services:** Business logic for Spotify API integration (`service/` package)  
- **Models/DTOs:** Data transfer objects and response models (`model/` package)
- **Config:** Web configuration and CORS setup (`config/` package)

Key service classes:
- `AuthService`: Spotify OAuth authentication and token management
- `RecommendationsService`: ML-based song recommendation logic
- `PlayListService`: Playlist creation and management
- `SearchService`: Artist, album, and track search functionality

### Frontend Architecture
- **Views:** Main application pages (`views/` directory)
- **Components:** Reusable UI components (`components/` directory)
- **Router:** Vue Router configuration (`router/index.js`)

Key views:
- `GetRecommendation.vue`: ML recommendation interface
- `CreatePlayList.vue`: Playlist creation functionality
- Search views for albums, artists, and tracks

## API Endpoints

- **Backend API:** http://localhost:8888/swagger-ui.html
- **Frontend UI:** http://localhost:8080
- **Production ports:** Backend on 8888, Frontend on 8080

## Testing

The project includes comprehensive test coverage:
- **Unit Tests:** Service layer tests with Mockito
- **Integration Tests:** Controller tests for API endpoints
- **Development Tests:** Manual API testing classes in `test/java/.../dev/`

Run backend tests with `mvn test` or specific test classes with `mvn test -Dtest=<TestClassName>`.

## Dependencies

### Key Backend Dependencies
- Spring Boot 2.4.5 (Web, Test, RestTemplate)
- spotify-web-api-java 8.3.6 (Legacy - used for auth/playlist/search only)
- **Direct HTTP Integration**: Recommendations API uses RestTemplate for direct Spotify API calls
- Swagger 2.7.0 (API documentation)
- Mockito 5.2.0 (Testing)
- Lombok (Code generation)

## Important Architecture Notes

### Recommendation API Implementation
The recommendation functionality (`RecommendationsService`) has been **modernized to use direct HTTP calls** instead of the spotify-web-api-java library due to compatibility issues:

- **New Classes:**
  - `SpotifyHttpClient` - HTTP request builder and client utilities
  - `SpotifyRecommendationsResponse` - Complete DTOs for Spotify API responses with all fields
  - `LegacyRecommendationsResponse` - Frontend-compatible response format
  - `RecommendationsResponseMapper` - Comprehensive mapping between formats
  - `RecommendationsValidator` - Response validation and data integrity checks
  - `SpotifyErrorHandler` - Enhanced error handling with detailed error messages
  - `SpotifyApiException` - Custom exception with status codes and context

- **Modified Services:**
  - `RecommendationsService` - Now uses RestTemplate + response mapper
  - `RecommendationsController` - Updated to use legacy response format
  - `WebConfig` - Added RestTemplate bean with error handler

- **Endpoints Affected:** `/recommend/` and `/recommend/playlist/{id}`
- **Frontend Compatibility:** ✅ Complete - maintains exact response structure
  - Preserves `tracks.tracks[]` array structure
  - Maintains `externalUrls.externalUrls.spotify` nested format
  - Converts `preview_url` to `previewUrl` camelCase
  - All existing frontend code works without changes

### Enhanced Response Mapping (Phase 4)
**Complete Field Coverage:**
- ✅ All Spotify API fields mapped: `restrictions`, `linked_from`, `available_markets`
- ✅ Enhanced error responses with helpful context and user-friendly messages
- ✅ Comprehensive validation for data integrity and frontend compatibility
- ✅ Edge case handling: null fields, empty responses, malformed data
- ✅ Robust error categorization: auth errors, rate limits, bad requests
- ✅ 17 comprehensive tests covering all scenarios and error conditions

### Key Frontend Dependencies
- Vue.js 2.6.14
- Vue Router 3.5.1
- Axios 1.6.8 (HTTP client)
- SweetAlert 2.1.2 (UI notifications)

## Quick Update Shortcuts

### Adding Information to CLAUDE.md
Use this shortcut pattern to quickly add important information:

```bash
# Quick CLAUDE.md update pattern:
# 1. Identify the section (Architecture, Dependencies, Commands, etc.)
# 2. Use Edit tool with specific section markers
# 3. Always maintain existing structure

# Example: Adding new architecture notes
Edit CLAUDE.md -> Find "## Important Architecture Notes" -> Add after existing content

# Example: Adding new dependencies  
Edit CLAUDE.md -> Find "### Key Backend Dependencies" -> Add to list

# Example: Adding new commands
Edit CLAUDE.md -> Find "### Backend (Spring Boot)" -> Add new command with description
```

### Common Update Patterns:
1. **New Service/Class**: Add to "Important Architecture Notes" section
2. **New Command**: Add to appropriate "Development Commands" section  
3. **Configuration Change**: Update "Application Configuration" section
4. **Dependency Change**: Update "Dependencies" section
5. **Endpoint Change**: Update "API Endpoints" section

### Template for Architecture Updates:
```markdown
- **New Feature/Service**: Brief description
  - `ClassName` - What it does and why it's important
  - Key methods or functionality
  - Integration points with existing services
```