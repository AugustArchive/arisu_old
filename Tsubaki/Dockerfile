# Tsubaki: Core backend infrastructure for Arisu, all the magic begins here ✨🚀
# Copyright (C) 2020-2021 Noelware
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

FROM alpine:latest

# Install Tsubaki dependencies required for this image
RUN apk add --no-cache git curl ca-certificates && rm -rf /var/cache/apk/*
RUN mkdir -p /opt/java/adoptopenjdk
RUN curl -X GET -L -o /tmp/adoptopenjdk.tar.gz https://github.com/AdoptOpenJDK/openjdk16-binaries/releases/download/jdk-16.0.1%2B9/OpenJDK16U-jdk_x64_alpine-linux_hotspot_16.0.1_9.tar.gz
RUN tar -xvf /tmp/adoptopenjdk.tar.gz -C /opt/java/adoptopenjdk
RUN rm /tmp/adoptopenjdk.tar.gz
ENV JAVA_HOME="/opt/java/adoptopenjdk/jdk-16.0.1+9" \
    PATH="/opt/java/adoptopenjdk/jdk-16.0.1+9/bin:$PATH"

# Now we actually build Tsubaki
WORKDIR /opt/Tsubaki
COPY . .

RUN chmod +x gradlew
RUN ./gradlew :bootstrap:build

ENTRYPOINT [ "sh", "./docker/start.sh" ]
