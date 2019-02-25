
DROP TABLE  hibernate_sequences IF EXISTS;

CREATE TABLE hibernate_sequences (
  sequence_name VARCHAR(255) DEFAULT NULL,
  sequence_next_hi_value INTEGER DEFAULT NULL
);


DROP TABLE UserAccount IF EXISTS;

CREATE TABLE UserAccount (
  id INTEGER IDENTITY PRIMARY KEY,
  version INTEGER NOT NULL,
  password VARCHAR(255) DEFAULT NULL,
  username VARCHAR(255) DEFAULT NULL,
  UNIQUE KEY username (username)
);


DROP TABLE SocialNetworkAccount IF EXISTS;

CREATE TABLE SocialNetworkAccount (
  id INTEGER IDENTITY PRIMARY KEY,
  version INTEGER NOT NULL,
  accessToken VARCHAR(255) DEFAULT NULL,
  createDate datetime DEFAULT NULL,
  displayName VARCHAR(255) DEFAULT NULL,
  expireTime bigint(20) DEFAULT NULL,
  imageUrl VARCHAR(255) DEFAULT NULL,
  profileUrl VARCHAR(255) DEFAULT NULL,
  providerId VARCHAR(255) DEFAULT NULL,
  providerUserId VARCHAR(255) DEFAULT NULL,
  rank INTEGER NOT NULL,
  refreshToken VARCHAR(255) DEFAULT NULL,
  secret VARCHAR(255) DEFAULT NULL,
  userId VARCHAR(255) DEFAULT NULL,
  UNIQUE KEY userId (userId,providerId,rank),
  UNIQUE KEY userId_2 (userId,providerId,providerUserId)
);



DROP TABLE Researcher IF EXISTS;

CREATE TABLE Researcher (
  id INTEGER IDENTITY PRIMARY KEY,
  version INTEGER NOT NULL,
  address VARCHAR(255) DEFAULT NULL,
  email VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) DEFAULT NULL,
  phone VARCHAR(255) DEFAULT NULL,
  userAccount_id INTEGER DEFAULT NULL  
);
ALTER TABLE Researcher ADD CONSTRAINT IF NOT EXISTS fk_researcher_useraccount FOREIGN KEY (userAccount_id) REFERENCES UserAccount (id);



DROP TABLE Confirmation IF EXISTS;
CREATE TABLE Confirmation (
  id INTEGER IDENTITY PRIMARY KEY,
  version INTEGER NOT NULL,
  confirmationCode VARCHAR(255) DEFAULT NULL,
  confirmationDate datetime DEFAULT NULL,
  registrationDate datetime DEFAULT NULL,
  researcher_id INTEGER NOT NULL
);
ALTER TABLE Confirmation ADD CONSTRAINT IF NOT EXISTS fk_confirmation_resarcher FOREIGN KEY (researcher_id) REFERENCES Researcher (id);




DROP TABLE Experiment IF EXISTS;

CREATE TABLE Experiment (
  id INTEGER IDENTITY PRIMARY KEY,
  version INTEGER NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  experimentId VARCHAR(255) DEFAULT NULL,
  experimentName VARCHAR(255) DEFAULT NULL,
  publicExperiment tinyint(1) NOT NULL,
  owner_id INTEGER NOT NULL  
);
ALTER TABLE Experiment ADD CONSTRAINT IF NOT EXISTS fk_expeirment_researcher FOREIGN KEY (owner_id) REFERENCES Researcher (id);


DROP TABLE SocialNetworkConfiguration IF EXISTS;

CREATE TABLE SocialNetworkConfiguration (
  id INTEGER IDENTITY PRIMARY KEY,
  version INTEGER NOT NULL,
  notifyWhenExperimentExecutionFinished tinyint(1) NOT NULL,
  publishExistingExperimentMadePublic tinyint(1) NOT NULL,
  publishNewExperimentExecutionFinished tinyint(1) NOT NULL,
  publishNewPublicExperimentCreated tinyint(1) NOT NULL,
  publishNewPublicExperimentExecutionStarted tinyint(1) NOT NULL,
  service VARCHAR(255) DEFAULT NULL,
  actor_id INTEGER NOT NULL
);

ALTER TABLE Experiment ADD CONSTRAINT IF NOT EXISTS fk_expeirment_researcher FOREIGN KEY (owner_id) REFERENCES Researcher (id);



DROP TABLE Tag IF EXISTS;

CREATE TABLE Tag (
  id INTEGER IDENTITY PRIMARY KEY,
  version INTEGER NOT NULL,
  name VARCHAR(30) NOT NULL DEFAULT ''
);


DROP TABLE UserAccount_authorities IF EXISTS;

CREATE TABLE UserAccount_authorities (
  UserAccount_id INTEGER NOT NULL,
  authority VARCHAR(255) DEFAULT NULL
);

ALTER TABLE UserAccount_authorities ADD CONSTRAINT IF NOT EXISTS fk_authorities_useraccount FOREIGN KEY (UserAccount_id) REFERENCES UserAccount (id);


DROP TABLE Workspace IF EXISTS;

CREATE TABLE Workspace (
  id INTEGER IDENTITY PRIMARY KEY,
  version VARCHAR(20) NOT NULL,
  owner_id INTEGER NOT NULL,
  description VARCHAR(200) DEFAULT '""',
  downloads INTEGER DEFAULT '0',
  launches INTEGER DEFAULT '0',
  lastMod datetime DEFAULT NULL,
  name VARCHAR(100)  NOT NULL DEFAULT '',
  origin_id INTEGER DEFAULT NULL,
  wsVersion INTEGER  NOT NULL DEFAULT '0'  
);

ALTER TABLE Workspace ADD CONSTRAINT IF NOT EXISTS fk_workspace_origin FOREIGN KEY (origin_id) REFERENCES Workspace (id);
ALTER TABLE Workspace ADD CONSTRAINT IF NOT EXISTS fk_workspace_owner FOREIGN KEY (owner_id) REFERENCES Researcher (id);


DROP TABLE WorkspaceTags IF EXISTS ;

CREATE TABLE WorkspaceTags (
  id_ws INTEGER NOT NULL,
  id_tag INTEGER NOT NULL
);

ALTER TABLE WorkspaceTags ADD CONSTRAINT IF NOT EXISTS fk_taggedWorkspaces FOREIGN KEY (id_ws) REFERENCES Workspace (id);
ALTER TABLE WorkspaceTags ADD CONSTRAINT IF NOT EXISTS fk_workspaceTags FOREIGN KEY (id_tag) REFERENCES Tag (id);
