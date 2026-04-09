# ── Stage 1: Build ──────────────────────────────────────
FROM node:20-alpine AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci --silent
COPY . .
RUN npm run build

# ── Stage 2: Serve ──────────────────────────────────────
# caddy:alpine runs as root by default, so we harden it for
# OpenShift's restricted SCC (arbitrary non-root UID).
FROM caddy:2-alpine

# Copy Caddyfile and built React app
COPY Caddyfile /etc/caddy/Caddyfile
COPY --from=builder /app/build /srv/www

# Caddy needs to write its data/config dirs.
# Make them world-writable so any UID OpenShift assigns can use them.
RUN mkdir -p /data /config \
    && chown -R root:root /data /config /srv/www \
    && chmod -R g=u   /data /config /srv/www

EXPOSE 8080

CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
