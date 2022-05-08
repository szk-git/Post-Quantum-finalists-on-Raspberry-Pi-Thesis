FROM nginx:stable-alpine as production-stage
COPY build/ /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]