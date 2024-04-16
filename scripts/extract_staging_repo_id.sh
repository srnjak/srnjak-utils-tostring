#!/bin/bash

# Check if the prefix parameter is provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <prefix>"
    exit 1
fi

# The prefix for staging repository IDs
PREFIX=$1

# Check if the MVN_SETTINGS_PATH environment variable is set
if [ -z "$MVN_SETTINGS_PATH" ]; then
    echo "MVN_SETTINGS_PATH environment variable is not set."
    exit 1
fi

# Run the Maven command to list staging repositories and capture the output
OUTPUT=$(mvn nexus-staging:rc-list -s $MVN_SETTINGS_PATH)

# Extract the last repository ID matching the provided prefix
LAST_REPO_ID=$(echo "$OUTPUT" | grep "$PREFIX" | tail -n1 | awk '{print $2}')

# Output the last repository ID
echo "$LAST_REPO_ID"
