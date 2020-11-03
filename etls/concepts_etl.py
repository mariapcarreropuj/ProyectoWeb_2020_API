import mysql.connector
import logging.config
import configparser
import database_concepts
import utils
import concepts_file_parser
import os

def _get_logger():
    logger = logging.getLogger(__name__)
    formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
    dir_name, filename = os.path.split(os.path.abspath(__file__))
    output_file = dir_name + "/concepts_etl.log"
    handler = logging.FileHandler(output_file)
    handler.setFormatter(formatter)
    logger.setLevel(logging.DEBUG) # DEBUG - INFO - WARN - ERROR
    logger.addHandler(handler)
    return logger

logger = _get_logger()

def load_concepts(file_path, concepts, cnx):
    concept_read = 0
    concept_inserted = 0
    concept_errors = 0
    row = 2
    #concepts_array = []
    for line in utils.read_tsv_file(file_path, delimiter='\t'):

        concept = concepts_file_parser.get_concept(line)
        concept_read += 1
        #print(concept)
        try:
            if (len(concept['pxordx']) != 0 and
                len(concept['codetype']) != 0 and
                len(concept['code']) != 0 and
                len(concept['vocabulary_id']) != 0 and
                len(concept['domain_id']) != 0):
                
                #print(concept)
                # Add new concept to dictionary
              
                concept_ref = concept['code'].strip()
                if concept['concept_id'] != None:
                    concept_concept_id = concept['concept_id'].strip()
                elif concept['concept_id'] == None:
                    concept_concept_id= ""
                concept_vocabulary = concept['vocabulary_id'].strip()
               
                concat_combination = concept_ref + concept_concept_id + concept_vocabulary
                #print("combination: ",concatcombination)
                if concat_combination not in concepts:
                    id = database_concepts.add_concept(concept,
                                                 cnx,concat_combination)
                   # concepts_array.append(concat_combination)                             
                    concepts[concept_ref] = id
                    logger.info("Inserting concept code {0} in database_concept.".format(concept['code']))
                    concept_inserted += 1
                else:
                    logger.info("concept code {0} already exists in database_concept.".format(concept['code']))
            else:
                message = "Error in row: %d, missing fields to create new concept." % row
                logger.error(message)
                print(message)
                concept_errors += 1
        except Exception as e:
            message = str(e) + " file: {0} - row: {1}".format(file_path, row)
            logger.error(message)
            print(message)
            concept_errors += 1
            return False
        row += 1
    return True

def execute(path_file):
    config = configparser.ConfigParser()
    config.read('config.ini')
    database_concepts_configuration = config['database']

    config = {
      'user': database_concepts_configuration['db_user'],
      'password': database_concepts_configuration['db_password'],
      'host': database_concepts_configuration['db_host'],
      'database': database_concepts_configuration['db_schema'],
      'raise_on_warnings': True
    }

    logger.info("Connecting to database_concepts...")
    cnx = mysql.connector.connect(**config)
    logger.info("The connection to the database was succesfull")

    logger.info('Getting all current concepts from database')
    concepts = database_concepts.get_current_concepts(cnx)
    print(concepts)

    #dir_path = '../files/concepts/'
    #list_files = map(lambda file_name: os.path.join(dir_path, file_name), os.listdir(dir_path))
    #files_read = 0
    #for path_file in list_files:
    print("*********** processing file %s *****************" % path_file)
    logger.info('processing file %s' % path_file)
    resultado = load_concepts(path_file, concepts, cnx)
     #    files_read += 1
    print("completed processing of the concepts")
    logger.info('Completed processing of files')
    return resultado

#if __name__ == "__main__":
#    main()