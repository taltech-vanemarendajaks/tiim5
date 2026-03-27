FROM node:24.14.1-alpine AS base
WORKDIR /app

COPY package.json package-lock.json* ./
RUN --mount=type=cache,target=/root/.npm npm ci

FROM base AS dev
COPY . .

FROM dev AS build
RUN npm run build

FROM node:24.14.1-alpine AS production
WORKDIR /app

COPY --chown=node:node --from=build /app/package.json /app/package-lock.json ./
RUN --mount=type=cache,target=/root/.npm npm ci --omit=dev --ignore-scripts

COPY --chown=node:node --from=build /app/dist/studyplanner/server/ ./server/
COPY --chown=node:node --from=build /app/dist/studyplanner/browser/ ./browser/

ENV NODE_ENV=production
USER node
EXPOSE 4000
CMD ["node", "server/server.mjs"]
