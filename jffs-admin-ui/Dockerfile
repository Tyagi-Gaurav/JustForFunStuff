FROM chonku/nodejs:v0.1.8

WORKDIR /app

COPY package.json package.json
COPY package-lock.json package-lock.json
COPY public public
COPY src src

RUN npm install
RUN npm run build
RUN npm install -g serve

CMD [ "serve", "-s", "build" ]

EXPOSE 3000
