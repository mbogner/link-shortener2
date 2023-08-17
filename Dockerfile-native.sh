#!/usr/bin/env bash
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
cd "${DIR}" || exit 1

IMAGE=link-shortener2-native:latest
DOCKERFILE=Dockerfile-native
docker build -t $IMAGE -f $DOCKERFILE . || exit 1
