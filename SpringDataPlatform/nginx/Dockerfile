FROM nginx:1.25-alpine

# wget for healthcheck
RUN apk add --no-cache wget

COPY nginx.conf /etc/nginx/nginx.conf
COPY index.html /usr/share/nginx/html/index.html

EXPOSE 80
