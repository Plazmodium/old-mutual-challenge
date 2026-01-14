# Old Mutual Code Challenge - Frontend

This is the frontend application for the Old Mutual Code Challenge. It is built using **Angular 21** and provides a user interface to browse and view details about countries.

## 🛠️ Tools & Technologies

- **Framework**: [Angular 21](https://angular.dev/)
- **Styling**: [Tailwind CSS 4](https://tailwindcss.com/)
- **Testing**: [Vitest](https://vitest.dev/)
- **Rendering**: Server-Side Rendering (SSR) enabled
- **Package Manager**: npm

## 🏗️ Architecture Layout

The project follows a modular and component-based architecture:

- **Containers (`src/app/containers`)**: Smart components that handle data fetching and state management.
  - `home`: Displays the list of all countries.
  - `detail`: Displays detailed information about a specific country.
- **Components (`src/app/components`)**: Presentational (dumb) components focused on UI rendering.
  - `country-card`: Reusable card component for displaying country summaries.
- **Services (`src/app/services`)**: Business logic and API communication.
  - `CountriesService`: Fetches country data from the backend.
  - `GenericHttpService`: A wrapper for standard HTTP operations.
- **Models (`src/app/models`)**: TypeScript interfaces defining data structures.
- **SSR**: Configured for Server-Side Rendering to improve SEO and initial load performance.

## 📂 Project Structure

```text
src/
├── app/
│   ├── components/       # Reusable UI components
│   ├── containers/       # Page-level components (Smart components)
│   ├── models/           # TypeScript interfaces
│   ├── services/         # API services and business logic
│   ├── app.config.ts     # Main application configuration
│   ├── app.routes.ts     # Routing definitions
│   └── app.ts            # Root component
├── main.ts               # Application entry point
├── styles.css            # Global styles (Tailwind imports)
└── server.ts             # SSR server configuration
```

## 🚀 Getting Started

### Prerequisites

- Node.js (version recommended in `package.json` engine or latest LTS)
- npm

### Installation

```bash
npm install
```

### Development Server

Run the development server:

```bash
npm start
```

Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

### Build

Run the following command to build the project. The build artifacts will be stored in the `dist/` directory.

```bash
npm run build
```

### Running Tests

Execute the unit tests using Vitest:

```bash
npm test
```

## 🎨 Styling

The project uses Tailwind CSS 4 for utility-first styling. Configuration can be found in `package.json` and CSS imports are in `src/styles.css`.

## 🧪 Testing Strategy

Unit tests are written using Vitest, providing a fast and modern testing experience. Test files follow the `.spec.ts` naming convention and are located alongside the files they test.
