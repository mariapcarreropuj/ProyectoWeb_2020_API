import mysql.connector
from datetime import datetime

def get_current_concepts(cnx):
    concepts = {}
    cursor = cnx.cursor()
    query = ("SELECT id, idHybrid FROM concepts")
    cursor.execute(query)
    for (id, idHybrid) in cursor:
        if idHybrid not in concepts:
            concepts[idHybrid] = id
    cursor.close()
    return concepts

def add_concept(concept, cnx, concat_combination):
    sql = ("""INSERT INTO concepts(idHybrid, pxordx, oldpxordx, codetype, conceptClassId, conceptId, vocabularyId, 
        domainId, track, standardConcept, code, codewithperiods, codescheme, longDesc, shortDesc, codeStatus, codeChange,
        codeChangeYear, codePlannedType, codeBillingStatus, codeCmsClaimStatus, sexCd, anatOrCond, poaCodeStatus, poaCodeChange,
         poaCodeChangeYear, validStartDate, validEndDate, invalidReason, createDt, conceptState)
              VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)""")
    concept_id_val = None
    standard_concept_val = None
    sex_cd_val = None
    valid_start_date_val = None
    valid_end_date_val = None
    poa_code_change_val = None
    poa_code_status_val = None
    poa_code_change_year_val = None
    invalid_reason_val = None
    create_dt_val= None

    if(concept['concept_id'] != None):
        concept_id_val = concept['concept_id'].strip()

    if(concept['sex_cd']!= None):
        sex_cd_val = concept['sex_cd'].strip()

    if(concept['standard_concept']!=None):
        standard_concept_val = concept['standard_concept'].strip()

    if(concept['valid_start_date']!= None):
        valid_start_date_val = concept['valid_start_date'].strip()

    if(concept['valid_end_date'] != None):
        valid_end_date_val = concept['valid_end_date'].strip()

    if(concept['poa_code_change']!= None):
        poa_code_change_val = concept['poa_code_change'].strip()

    if(concept['poa_code_status']!=None):
        poa_code_status_val = concept['poa_code_status'].strip()

    if(concept['poa_code_change_year']!= None):
        poa_code_change_year_val = concept['poa_code_change_year'].strip()

    if(concept['invalid_reason']!= None):
        invalid_reason_val = concept['invalid_reason'].strip()

    if(concept['create_dt']!= None):
        create_dt_val = concept['create_dt'].strip()

    values = (
        concat_combination,
        concept['pxordx'].strip(),
        concept['oldpxordx'].strip(),
        concept['codetype'].strip(),
        concept['concept_class_id'].strip(),
        concept_id_val,
        concept['vocabulary_id'].strip(),
        concept['domain_id'].strip(),
        concept['track'].strip(),
        standard_concept_val,
        concept['code'].strip(),
        concept['codewithperiods'].strip(),
        concept['codescheme'].strip(),
        concept['long_desc'].strip(),
        concept['short_desc'].strip(),
        concept['code_status'].strip(),
        concept['code_change'].strip(),
        concept['code_change_year'].strip(),
        concept['code_planned_type'].strip(),
        concept['code_billing_status'].strip(),
        concept['code_cms_claim_status'].strip(),
        sex_cd_val,
        concept['anat_or_cond'].strip(),
        poa_code_status_val,
        poa_code_change_val,
        poa_code_change_year_val,
        valid_start_date_val,
        valid_end_date_val,
        invalid_reason_val,
        create_dt_val,
        concept['conceptstate'].strip()
    )
    cursor = cnx.cursor()
    cursor.execute(sql, values)
    cnx.commit()
    return cursor.lastrowid

def update_task_status(status, uuid, cnx):
    now = datetime.now()
    sql = ("""UPDATE tasks SET status = %s, last_update_date = %s WHERE uuid = %s""")
    values = (
        status,
        now.strftime("%Y-%m-%d %H:%M:%S"),
        uuid,
    )
    cursor = cnx.cursor()
    cursor.execute(sql, values)
    cnx.commit()