FROM node:16-alpine

RUN apk update && apk add git ca-certificates

WORKDIR /opt/breb-site
COPY . .
RUN rm yarn.lock
RUN npm ci

ENTRYPOINT [ "npm", "start" ]
