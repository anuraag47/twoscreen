# Two Screen App — Android Developer Assessment

A two-screen Android application that captures customer information and submits it to a REST API.

---

## Screens

- **Screen 1 — Customer Form:** Collects Full Name, Email, Phone, and City with real-time validation. Submit button is disabled until all fields pass validation.
- **Screen 2 — Success:** Displays a confirmation message and a summary of the submitted details.

---

## Tech Stack

| Technology | Usage |
|---|---|
| Kotlin | Primary language |
| MVVM | Architecture pattern |
| Jetpack Navigation + Safe Args | Single-activity navigation |
| Hilt | Dependency injection |
| Retrofit 2 + Gson | Networking |
| OkHttp Logging Interceptor | API request/response logging |
| Kotlin Coroutines + viewModelScope | Async operations |
| LiveData | UI state management |
| ViewBinding | View access |

---

## MockAPI Endpoint

POST https://6a0986dae7e3f433d48329dc.mockapi.io/twoscreen/api/v1/customers

Request body:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567",
  "city": "Dubai"
}
```

---

## Setup & Build Instructions

1. Clone the repository
git clone https://github.com/anuraag47/twoscreen/

2. Open the project in **Android Studio Hedgehog or later**
3. Let Gradle sync complete
4. Run the app on an emulator or physical device (API 24+)

No API keys or local configuration needed — the MockAPI endpoint is hardcoded and publicly accessible.

---

## Architecture Decisions

**Single Activity:** The app uses one `MainActivity` as a shell. Both screens are Fragments managed by the Jetpack Navigation Component, which handles the back stack and fragment transactions cleanly.

**MVVM:** The `CustomerFormViewModel` holds all form state and validation logic, keeping the Fragment responsible only for displaying data and forwarding user input. This makes the logic easily testable and survives configuration changes.

**Repository pattern:** `CustomerRepository` sits between the ViewModel and `ApiService`, so the ViewModel never talks to Retrofit directly. If the data source changes in future, only the repository needs updating.

**Hilt:** Used for injecting `CustomerRepository` into the ViewModel and `ApiService` into the repository. All dependencies are scoped to `SingletonComponent` so Retrofit and OkHttp are created once and reused.

**LiveData one-shot events:** Navigation and error Snackbar are triggered via `MutableLiveData` that resets to `null` after being consumed, preventing re-triggering on screen rotation.

---

## Assumptions

- City selection is restricted to four hardcoded options: Dubai, Abu Dhabi, Sharjah, Riyadh
- Phone validation accepts digits only, between 7 and 15 characters
- The success screen is a dead end — there is no "go back and submit again" flow, matching the assessment spec
- MockAPI returns HTTP 201 on successful POST
