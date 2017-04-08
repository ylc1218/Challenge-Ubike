#!/usr/bin/python2

import logging
from apscheduler.schedulers.blocking import BlockingScheduler
from apscheduler.executors.pool import ThreadPoolExecutor
from updateDb import updateInfo, updateStatus
from datetime import datetime

logging.basicConfig()

executors = {
	'default': ThreadPoolExecutor(2)
}

sched = BlockingScheduler(executors=executors)

@sched.scheduled_job('interval', hours=1)
def timed_job():
	print('Starting updateInfo')
	updateInfo()
	print('Finish updateInfo')


@sched.scheduled_job('interval', minutes=1)
def timed_job():
	print('Starting updateStatus')
	updateStatus()
	print('Finish updateStatus')

sched.start()
