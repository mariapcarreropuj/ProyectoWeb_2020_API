#!/usr/bin/env python
import pika
import json
import os
import sys
import configparser
import logging.config
import utils
import concepts_etl
import mysql.connector
from datetime import datetime
import database_concepts
import vocabulary_etl
import database
import time

def update_status_task(uuid, result):
    config = configparser.ConfigParser()
    config.read('config.ini')
    database_configuration = config['database_tasks']

    config = {
      'user': database_configuration['db_user'],
      'password': database_configuration['db_password'],
      'host': database_configuration['db_host'],
      'database': database_configuration['db_schema'],
      'raise_on_warnings': True
    }

    print("Connecting to database...")
    cnx = mysql.connector.connect(**config)
    print("The connection to the database was succesfull")
    if result:
        status = "Succeeded"
    else:
        status = "Failed"
    print("uuid: {0} - status: {1}".format(status, uuid))
    database_concepts.update_task_status(status, uuid, cnx)
    print("Task status updated")

# Read rabbitmq configuration
config = configparser.ConfigParser()
config.read('config.ini')
database_configuration = config['rabbitmq']

credentials = pika.PlainCredentials(database_configuration['user'],
                                    database_configuration['password'])
connection = pika.BlockingConnection(pika.ConnectionParameters(
                                        host=database_configuration['host'],
                                        port=5672,
                                        virtual_host='/',
                                        credentials=credentials,
                                        heartbeat=0))
channel = connection.channel()

channel.queue_declare(queue=database_configuration['queue'],durable=True)

def callback(ch, method, properties, body):
    print(" [x] Received %r" % body)
    task = json.loads(body)
    file_id = task['file_id']
    #time.sleep(body.count(b'.'))
    now = datetime.now()
    if (task['document_type']=="concepts"):
        path_file = "./received_files/concepts/concepts_{0}_{1}.tsv".format(file_id,
                                                                                now.strftime("%Y%m%d%H%M%S"))
        utils.download_file_from_google_drive(file_id, 
                                              path_file)
        print("executing concepts etl with file: {}".format(path_file))
        resultado = concepts_etl.execute(path_file)
        print("resultado: ", resultado)
    elif (task['document_type']=="vocabulary"):
        path_file = "./received_files/vocabulary/vocabulary_{0}_{1}.csv".format(file_id,
                                                                            now.strftime("%Y%m%d%H%M%S"))
        utils.download_file_from_google_drive(file_id, 
                                              path_file)
        print("executing vocabulary etl with file: {}".format(path_file))
        resultado = vocabulary_etl.execute(path_file)
        print("resultado: ", resultado)
    if resultado:
        # update task in success
        update_status_task(task['uuid'], True)
    else:
        # update task in error
        update_status_task(str(task['uuid']), False)
    ch.basic_ack(delivery_tag=method.delivery_tag)


channel.basic_qos(prefetch_count=1)
channel.basic_consume(queue=database_configuration['queue'],
                      on_message_callback=callback,
                      auto_ack=False
                      )

print(' [*] Waiting for messages. To exit press CTRL+C')
channel.start_consuming()