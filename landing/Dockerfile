FROM node:16-alpine

LABEL MAINTAINER="Arisu Team <cutie@floofy.dev>"
RUN apk update && apk add git ca-certificates

WORKDIR /opt/landing
COPY . .
RUN npm i -g typescript eslint next
RUN npm ci
RUN NEXT_TELEMETRY_DISABLED=1 NODE_ENV=production npm run build
RUN rm -rf src

ENTRYPOINT [ "next", "start" ]
