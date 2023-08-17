#!/bin/bash
date
docker exec -i redis redis-cli <<EOF
AUTH admin admin123!
FLUSHALL
EOF
