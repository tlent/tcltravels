# TCL Travels Frontend

React + TypeScript single-page app for TCL Travels, built with Vite and Tailwind CSS.

See the [root README](../README.md) for full project setup, API documentation, and environment variables. This frontend is normally served by the Spring Boot backend as a bundled static build, but can also run standalone against a local backend.

## Stack

- React 19 + TypeScript
- Vite
- Tailwind CSS v4
- React Router
- TanStack Query
- React Hook Form + Zod

## Development

```bash
npm install
npm run dev      # dev server on port 5173, proxies /api to localhost:8080
npm run build    # production build to dist/
npm run lint      # ESLint
```
