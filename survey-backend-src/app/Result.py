import MySQLdb
from DBConnector import * 

class Result(object):
    def __init__(self, user_id, results):
	self.user_id = user_id
	self.results = results

    def save(self):
	try:
	    db_connector = DBConnector("localhost", "root", "shoutTEARstreamTAIL", "survey_backend")
	    db = db_connector.getDB()
	    cursor = db.cursor()
	    cursor.execute("LOCK TABLE survey WRITE")
	    cursor.execute("INSERT INTO survey (user_id, results) VALUES (%s,%s)",(self.user_id,self.results))
	    db.commit()
	    cursor.execute("UNLOCK TABLES")
	    return {"Output": "Successfully inserted result to database with the following info:",
		    "user_id":self.user_id,
		    "results":self.results}
	except Exception, ex:
	    return{"Error":str(ex)}
