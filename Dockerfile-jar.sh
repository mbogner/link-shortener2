#!/usr/bin/env bash
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
cd "${DIR}" || exit 1

IMAGE=link-shortener2-jar:latest
DOCKERFILE=Dockerfile-jar
docker build -t $IMAGE -f $DOCKERFILE . || exit 1
