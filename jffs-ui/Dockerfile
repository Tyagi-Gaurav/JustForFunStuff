FROM chonku/nodejs:v0.1.8

WORKDIR /app

COPY package.json package.json
COPY package-lock.json package-lock.json
RUN npm install

COPY public public
COPY src src

CMD [ "npm", "start" ]

EXPOSE 3000
