FROM node:24-alpine AS base
WORKDIR /app
COPY package.json package-lock.json* ./
RUN --mount=type=cache,target=/root/.npm npm ci

FROM base AS dev
COPY . .

FROM dev AS build
RUN npx ng build --configuration=production

FROM node:24-alpine AS production
WORKDIR /app
COPY --from=build /app/package.json /app/package-lock.json ./
RUN --mount=type=cache,target=/root/.npm npm ci --omit=dev --ignore-scripts
COPY --from=build /app/dist/studyplanner/server/ ./server/
COPY --from=build /app/dist/studyplanner/browser/ ./browser/
USER node
EXPOSE 4000
CMD ["node", "server/server.mjs"]
