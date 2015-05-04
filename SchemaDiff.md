An introduction to SchemaDiff

# Introduction #

I created SchemaDiff because I couldn't find a utility that did diffs of two different database schemas that was free or inexpensive.

SchemaDiff quickly compares all of the tables, columns, and indexes in two databases and produces reports showing the differences.

I'm working on a general version to release here. Currently the version I have is Sybase specific, but with the DatabaseMetaData class I will create a version that should be able to compare any two databases with JDBC drivers.


# Details #

SchemaDiff is very fast and runs in 30-40 seconds on two Sybase databases with about 800 tables each.

  * Compares table/owner pairs for existence and locking style
  * Compares columns for existence, data type, length, precision, scale, and identity
  * Compares indexes for existence, columns/column order, uniqueness, and whether they are clustered or not