FROM node:20-alpine

WORKDIR /app

# Install dependencies into the image layer before copying source.
# This layer is cached separately so re-builds only re-run npm install
# when package.json / package-lock.json actually change.
COPY package*.json ./
# --legacy-peer-deps: @vue/cli-plugin-unit-jest@5 expects @vue/vue3-jest@^27
# but the project pins ^29, causing a peer dep conflict on npm 7+.
RUN npm install --legacy-peer-deps

COPY . .

EXPOSE 8080

# --host 0.0.0.0 is required so the dev server binds to all interfaces
# inside the container (the default 127.0.0.1 is unreachable from outside).
CMD ["npm", "run", "serve", "--", "--host", "0.0.0.0"]
