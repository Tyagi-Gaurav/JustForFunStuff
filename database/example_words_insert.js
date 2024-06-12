print('Start #################################################################');

db
db.createCollection('word');

db.word.insertMany([
  {"word":"Grumble","meanings":[{"definition":"to murmur or mutter in discontent; complain sullenly"}]},
  {"word":"Staunch","meanings":[{"definition":"Very loyal and committed in attitude","synonyms":["Stalwart","Loyal","Faithful","Trusty","Committed"]}]}
])

print('END #################################################################');
