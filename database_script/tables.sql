CREATE TABLE `vocabularies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref` char(20) NOT NULL,
  `name` char(255) NOT NULL,
  `url` char(255) NOT NULL,
  `version` char(100) NOT NULL,
  `description` varchar(500) NOT NULL,
  `status` char(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE ConceptsDB.`concepts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idHybrid` char(255) NOT NULL,
  `pxordx` char(1) NOT NULL,
  `oldpxordx` char(1),
  `codetype` char(3) NOT NULL,
  `conceptClassId` char(20),
  `conceptId` int(11),
  `vocabularyId` char(20) NOT NULL,
  `domainId` varchar(20) NOT NULL,
  `track` varchar(20),
  `standardConcept` char(1),
  `code` varchar(30) NOT NULL,
  `codewithperiods` varchar(30),
  `codescheme` char(20),
  `longDesc` varchar(500),
  `shortDesc` varchar(500),
  `codeStatus` char(1),
  `codeChange` char(30),
  `codeChangeYear` year(4),
  `codePlannedType` char(20),
  `codeBillingStatus` char(1),
  `codeCmsClaimStatus` char(1),
  `sexCd` char(1),
  `anatOrCond` char(1),
  `poaCodeStatus` char(30),
  `poaCodeChange` char(255),
  `poaCodeChangeYear` year(4),
  `validStartDate` char(8),
  `validEndDate` char(8),
  `invalidReason` char(255),
  `createDt` int(11),
  `conceptState` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE UsersDB.`user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;