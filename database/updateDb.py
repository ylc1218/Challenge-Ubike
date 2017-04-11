#!/usr/bin/python2

import urllib
import gzip
import json
import os
import urlparse
import psycopg2.extensions


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


def executeSql(sql, conn):
	cur = conn.cursor()
	try:
		cur.execute(sql)
		conn.commit()
	except Exception as e:
		print "unable to execute sql"
		print str(e)


def update():
	data = retrieveData()
	conn = getConnection()
	cur = conn.cursor()

	ids = [(k) for k, v in data["retVal"].iteritems()]
	infos = [(k, v["sna"], v["lat"], v["lng"]) for k, v in data["retVal"].iteritems()]
	status = [(k, v["sbi"], v["act"]) for k, v in data["retVal"].iteritems()]

	# delete old info
	id_val = '(' + ','.join(ids) + ')'
	sql = "DELETE from ubike_info where id not in " + id_val
	executeSql(sql, conn)
	print "delete old info"

	# update info
	info_val = ','.join(cur.mogrify('(%s,%s,%s,%s)', row) for row in infos)
	sql = "INSERT INTO ubike_info (id, sna, lat, lng) VALUES " + info_val \
		+ "ON CONFLICT (id) DO UPDATE SET " \
		+ "sna = EXCLUDED.sna, lat = EXCLUDED.lat, lng = EXCLUDED.lng"
	executeSql(sql, conn)
	print "update info"

	# update status
	status_val = ','.join(cur.mogrify('(%s,%s,%s)', row) for row in status);
	sql = "INSERT INTO ubike_status VALUES " + status_val \
		+ " ON CONFLICT (id) DO UPDATE SET" \
		+ " sbi = EXCLUDED.sbi, act = EXCLUDED.act"
	executeSql(sql, conn)
	print "update status"

	conn.close()
