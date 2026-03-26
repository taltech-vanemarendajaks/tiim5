FROM node:24-alpine AS base
WORKDIR /app
COPY package.json package-lock.json* ./
RUN --mount=type=cache,target=/root/.npm npm ci

FROM base AS build
COPY . .
RUN npx ng build --configuration=production

FROM node:24-alpine AS production
WORKDIR /app
COPY --from=build /app/dist/studyplanner/ ./
USER node
EXPOSE 4200
CMD ["node", "server/server.mjs"]
