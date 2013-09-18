
CREATE TABLE test_entity (
  id bigint NOT NULL auto_increment,
  version INT DEFAULT 0,
  name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;