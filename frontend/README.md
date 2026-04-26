### Frontend folder ###

Angular 21 single-page application with server-side rendering support.

### Prerequisites

- Node.js >= 20.0.0

### Running the frontend

From the frontend directory:

```
npm install
npm start
```

Open http://localhost:4200 in your browser.

### Running tests

**Unit tests:**
```
npm test
```
**E2E tests:**
```
npm run e2e
```

### Linting and formatting

ESLint and Prettier run automatically on staged files via Husky pre-commit hook. To run manually:

```
npm run lint
npm run prettier
```

### Code generation

Generate services from OpenAPI spec:
```
npm run sync:openapi
```

### Tech stack

- Angular 21 with SSR support
- Vitest for unit testing
- Playwright for E2E testing
- ESLint with angular-eslint
- Prettier for formatting
- Husky + lint-staged for pre-commit hooks
