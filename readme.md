show dbs
db.runCommand( { convertToCapped: 'person', size: 8192 } )
db.person.stats()
 
db.person.insert( { firstname : "Ada", lastname : "Milea", "age" : 25} )

