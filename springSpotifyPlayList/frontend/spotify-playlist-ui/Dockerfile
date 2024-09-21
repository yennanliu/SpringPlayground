# Use the official lightweight Node.js 14 image
FROM node:14-slim

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install dependencies
RUN npm install

# Install Vue CLI service globally (optional, depending on your needs)
RUN npm install -g @vue/cli-service

# Copy the rest of the application code to the working directory
COPY . .

# Build the application for production
RUN npm run build

# Use a lightweight web server to serve the built files
# You can switch to another server if needed, like nginx
FROM nginx:alpine

# Copy built files from the previous stage
COPY --from=0 /app/dist /usr/share/nginx/html

# Expose port 80 for the web server
EXPOSE 80

# Start nginx when the container launches
CMD ["nginx", "-g", "daemon off;"]