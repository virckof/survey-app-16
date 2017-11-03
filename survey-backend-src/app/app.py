from flask import Flask, redirect, url_for, session, jsonify, request
from flask.ext import restful
from flask_restful import reqparse
from flask_httpauth import HTTPBasicAuth

from Result import Result

app = Flask(__name__)
app.debug = True
auth = HTTPBasicAuth()

parser = reqparse.RequestParser()
parser.add_argument('user_id', type=str)
parser.add_argument('results', type=str)

@auth.verify_password
def verify_password(username,password):
	if username == "admin" and password == "admin":
		return True
	return False

#insert new query to database
@app.route('/survey/results/create', methods=['POST'])
@auth.login_required
def create():
	args = parser.parse_args()
	user_id = args['user_id']
	results = args['results']

	if user_id == None:
		return jsonify({"ERROR": "Please provide a user id"})
	elif results == None:
		return jsonify({"ERROR": "Please provide results"})
	results = Result(user_id, results)
	response = results.save()
	return jsonify(response)

if __name__ == '__main__':
	app.run(debug=True)
