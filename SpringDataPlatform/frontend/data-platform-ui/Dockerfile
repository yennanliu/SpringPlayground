# Node.js 18 LTS with native ARM support
FROM node:18-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy source
COPY . .

EXPOSE 8080

# Default command (overridden by docker-compose)
CMD ["npm", "run", "serve"]
