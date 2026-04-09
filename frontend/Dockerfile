# ── Stage 1: Build ──────────────────────────────────────
FROM node:25-alpine AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci --silent
COPY . .
RUN npm run build

# ── Stage 2: Serve ──────────────────────────────────────
FROM caddy:2.11.2-alpine AS caddy-stage

EXPOSE 2015
COPY Caddyfile /etc/caddy/Caddyfile
COPY --from=builder /app/build /srv

RUN mkdir -p /config/caddy /data/caddy \
    && chown -R 65532:65532 /config /data /srv \
    && chmod -R g=u /config /data /srv

USER 65532
