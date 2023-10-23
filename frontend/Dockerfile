FROM node:18-alpine

WORKDIR /app

# EXPOSE is merely a hint that a certain ports are useful
EXPOSE 8080

# Add .bin to PATH so it can run npm command
ENV PATH /app/node_modules/.bin:$PATH

# Copy json files to prepare the docker container
COPY ./package*.json /app/

# Install dependencies
RUN npm install

# Copy project to container
COPY . /app

# Start the node dev server
CMD [ "npm", "start" ]
