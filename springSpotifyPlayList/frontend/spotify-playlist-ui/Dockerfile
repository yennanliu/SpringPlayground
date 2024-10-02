# Use the official lightweight Node.js 14 image
FROM node:14-slim

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install dependencies
RUN npm install && npm install vue-cli-service && npm install --save-dev @vue/cli-service
 
# Output installed dependencies for debugging purposes
RUN npm list

# Copy the rest of the application code to the working directory
COPY . .

# Expose port 8080 to the outside world
EXPOSE 8080

# Debugging step: Run a simple command to keep the container alive for inspection
#CMD ["npm", "run", "serve"]
#CMD ["tail", "-f", "/dev/null"]

CMD ["npm", "run", "serve"]