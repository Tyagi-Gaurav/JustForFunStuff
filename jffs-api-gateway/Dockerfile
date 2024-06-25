FROM nginx

COPY . /etc/nginx

RUN apt-get update && apt-get install -y jq && mkdir -p /etc/nginx/templates
COPY api_backends.conf.template /etc/nginx/templates
COPY mobile.html /usr/share/nginx/html/

EXPOSE 80
EXPOSE 443