#!/usr/bin/python2

import urllib
import gzip
import json
import os
import urlparse

import psycopg2.extensions
#psycopg2.extensions.register_type(psycopg2.extensions.UNICODE)
#psycopg2.extensions.register_type(psycopg2.extensions.UNICODEARRAY)


def retrieveData():
	
	url = "http://data.taipei/youbike";
	filename = "ubike.gz"
	urllib.urlretrieve(url, filename)

	with gzip.open(filename, "r") as f:	
		data = json.loads(f.read())

	'''
	#print data
	for k, v in data["retVal"].iteritems():
		sna = v["sna"]
		lat = v["lat"]
		lng = v["lng"]
		#print k, sna, lat, lng
	'''
	return data


def getConnection():
	
	urlparse.uses_netloc.append("postgres")
	dburl = urlparse.urlparse(os.environ["DATABASE_URL"])

	try:
		conn = psycopg2.connect(
			database=dburl.path[1:],
			user=dburl.username,
			password=dburl.password,
			host=dburl.hostname,
			port=dburl.port		
		)		
		return conn
	except Exception as e:
		print "unable to connect to database"
		print str(e)


def execSql(sql, tuples, conn):
	cur = conn.cursor()
	try:
		cur.executemany(sql, tuples)
		conn.commit()
	except Exception as e:
		print "unable to execute sql"
		print str(e)


def updateInfo():
	data = retrieveData()
	conn = getConnection()	

	tuples = [(k, v["sna"], v["lat"], v["lng"]) for k, v in data["retVal"].iteritems()]
	sql = "INSERT INTO ubike_info (id, sna, lat, lng) VALUES(%s,%s,%s,%s) \
			ON CONFLICT (id) DO UPDATE SET \
			sna = EXCLUDED.sna, lat = EXCLUDED.lat, lng = EXCLUDED.lng"
	execSql(sql, tuples, conn)
	print "Info updated"

def updateStatus():
	data = retrieveData()
	conn = getConnection()

	tuples = [(k, v["sbi"], v["act"]) for k, v in data["retVal"].iteritems()]
	sql = "INSERT INTO ubike_status (id, sbi, act) VALUES(%s,%s,%s) \
			ON CONFLICT (id) DO UPDATE SET \
			sbi = EXCLUDED.sbi, act = EXCLUDED.act"
	execSql(sql, tuples, conn)
	print "Status updated"

