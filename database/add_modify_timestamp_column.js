db.word.aggregate( [
   {
     $addFields: {
       modifiedDateTime: new Date() ,
     }
   },
   {
     $addFields: { word:
       { $add: [ "$modifiedDateTime"] } }
   }
] )