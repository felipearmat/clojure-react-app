FROM clojure:temurin-17-tools-deps-alpine

WORKDIR /app

# EXPOSE is merely a hint that a certain ports are useful
EXPOSE 3000

# Copy project to container
COPY ./app /app

# Start the clojure dev server
CMD ["clojure", "-X:dev"]
