# Build Stage
FROM node:18-alpine AS builder
WORKDIR /app

# Install dependencies
COPY frontend/package*.json ./
RUN npm ci

# Copy source code
COPY frontend/ .

# Build for production
RUN npm run build -- --configuration production

# Runtime Stage (Nginx)
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Remove default nginx static assets
RUN rm -rf ./*

# Copy built assets from builder stage
COPY --from=builder /app/dist/frontend-temp/browser .

# Copy custom nginx config if needed (optional, using default for now but robust apps need nginx.conf)
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
