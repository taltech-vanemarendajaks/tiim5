FROM node:22-alpine AS base
WORKDIR /app
COPY package.json package-lock.json* ./
RUN --mount=type=cache,target=/root/.npm npm ci

FROM base AS build
COPY . .
RUN npx ng build --configuration=production

FROM node:22-alpine AS runner
WORKDIR /app
COPY --from=build /app/dist/studyplanner/ ./dist/studyplanner/
USER node
EXPOSE 4000
CMD ["node", "dist/studyplanner/server/server.mjs"]
