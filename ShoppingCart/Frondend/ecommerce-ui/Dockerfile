FROM node:20-slim

WORKDIR /app

# Install dependencies before copying source (better layer caching)
COPY package*.json ./
RUN npm install

COPY . .

EXPOSE 8080

CMD ["npm", "run", "serve", "--", "--host", "0.0.0.0"]
