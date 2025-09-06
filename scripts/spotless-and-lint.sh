#!/bin/bash

set -e

echo "Running Spotless formatting and linting..."

# Run spotless apply to format code
./gradlew spotlessApply

echo "Spotless formatting completed successfully!"

# Run Lint
./gradlew lint

echo "Linting completed successfully!"
echo "All lint checks passed!"