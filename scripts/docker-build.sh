#!/usr/bin/env bash
set -e

./gradlew customer-entity-endpoints:customer-entity-rest-api:bootBuildImage --imageName $IMAGE

should_push=${PUSH_IMAGE:-false}

if $should_push; then
    docker push $IMAGE
fi