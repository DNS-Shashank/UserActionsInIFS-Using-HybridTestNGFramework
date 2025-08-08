#!/bin/bash
echo "Setting up environment variables for UserActionsInIFS tests..."

# Set mandatory environment variables
export IFS_USERNAME=shashank.durthu@infor.com
export IFS_PASSWORD=Nrth~dd4s#kola8

# Set optional environment variables
export IFS_TEST_URL=https://mingle-t20-portal.mingle.inforos.dev.inforcloudsuite.com/v2/INTQAINFOROSV2_AX1
export IFS_INVALID_USERNAME=invalid-user@infor.com
export IFS_INVALID_PASSWORD=wrong-password

echo "Environment variables set successfully!"
echo "Running tests..."

mvn test

echo ""
echo "Tests completed. Check reports/ directory for results."