CREATE KEYSPACE IF NOT EXISTS paris
WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1' }
AND durable_writes = true;

CREATE TABLE paris.trees(
  zone text PRIMARY KEY,
  tree_count int ,
);