#!/usr/bin/python2

import logging
from apscheduler.schedulers.blocking import BlockingScheduler
from apscheduler.executors.pool import ThreadPoolExecutor
from updateDb import update
from datetime import datetime

logging.basicConfig()

executors = {
	'default': ThreadPoolExecutor(2)
}

sched = BlockingScheduler(executors=executors)

@sched.scheduled_job('interval', seconds=30)
def timed_job():
	print('-- Starting updateDb --')
	update()
	print('-- Finish updateDb --')

sched.start()
