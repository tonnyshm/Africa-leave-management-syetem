#!/bin/bash

echo "🔨 Cleaning project and Docker state..."

# Step 1: Clean Maven build
cd auth-service && ./mvnw clean package -DskipTests || mvn clean package -DskipTests && cd ..
cd leave-service && ./mvnw clean package -DskipTests || mvn clean package -DskipTests && cd ..

# Step 2: Force rebuild Docker containers and volumes
echo "🧼 Pruning Docker..."
docker-compose down --volumes --remove-orphans
docker system prune -af --volumes

echo "🐳 Rebuilding Docker images..."
docker-compose build --no-cache

echo "🚀 Starting fresh containers..."
docker-compose up -d --force-recreate

echo "✅ Done. Use 'docker-compose logs -f' to follow logs."
