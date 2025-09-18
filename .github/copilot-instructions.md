# Copilot Instructions for LG-CNS Instagram Clone Project

## Big Picture Architecture
- **Backend (`be-instagram-clone/`)**: Java Spring Boot service. Handles core business logic, API endpoints, and data persistence. Organized by domain (e.g., post, comment, user) under `src/main/java/com/lgcns/beinstagramclone/`.
- **Frontend (`fe-instagram-clone/`)**: React + Vite app. UI components are grouped by feature (e.g., feed, comment, profile) under `src/component/` and pages under `src/page/`.
- **Docs & Artifacts (`산출물 보고서/`)**: Contains requirements, API specs (Swagger), DB schema images, and project plans. Use these for reference, not code generation.

## Developer Workflows
- **Backend Build & Run**: Use Gradle wrapper (`./gradlew build` or `gradlew.bat build` on Windows). Main config: `build.gradle`, `application.yml`.
- **Backend Tests**: Run with `./gradlew test` or `gradlew.bat test`. Test classes are in `src/test/java/com/lgcns/beinstagramclone/`.
- **Frontend Dev Server**: Start with `npm install` then `npm run dev` in `fe-instagram-clone/`.
- **Frontend Build**: Use `npm run build`.

## Project-Specific Conventions
- **Backend**: Follows Spring Boot conventions. Service, Controller, Repository layers. DTOs for API requests/responses. Test classes mirror main code structure.
- **Frontend**: React components use feature-based folders. API calls via `src/api/axios.js`. Utility functions in `src/utils/`.
- **Naming**: Java packages and React folders use domain-driven names (e.g., `post`, `comment`, `profile`).
- **Swagger API Spec**: Reference `산출물 보고서/3. API 명세서 (Swagger 캡처)/api-docs.json` for endpoint details.

## Integration Points
- **API Communication**: Frontend calls backend REST APIs. Endpoints and DTOs are documented in Swagger captures and images.
- **External Dependencies**: Backend uses Spring Boot, JPA, and possibly other common libraries (see `build.gradle`). Frontend uses React, Vite, Axios.

## Patterns & Examples
- **Backend**: Example service: `PostService.java` in `src/main/java/com/lgcns/beinstagramclone/post/service/`. Test: `PostServiceTest.java` in corresponding test folder.
- **Frontend**: Example page: `FeedPage.jsx`. Example component: `src/component/feed/`.
- **API Calls**: See `src/api/axios.js` for Axios setup and usage.

## Tips for AI Agents
- Always match new code to the domain-driven structure and naming.
- For new endpoints, update both backend controller/service and frontend API call.
- Use DTOs for all API boundaries; reference Swagger docs for field names and types.
- Keep build/test commands platform-appropriate (Windows: use `.bat` scripts).
- When in doubt, check the README or Swagger docs for requirements and data flows.

---
_Last updated: 2025-09-18_
