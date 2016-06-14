examples to dump and restore mysql

It should be as simple as running this:

mysql -u <user> -p < db_backup.dump
If the dump is of a single database you may have to add a line at the top of the file:

USE <database-name-here>;

C:\> mysql -u root -p

mysql> create database mydb;
mysql> use mydb;
mysql> source db_backup.dump;

mysql -p -u[user] [database] < db_backup.dump

mysql -uroot -p DatabaseName < path\TableName.sql

mysqldump -u -p mydatabase table1 > table1.sql

$ mysqldump --opt -u [uname] -p[pass] [dbname] > [backupfile.sql]

$ mysqldump -u root -p Tutorials > tut_backup.sql

You can try to use sed in order to extract only the table you want.

Let say the name of your table is mytable and the file mysql.dump is the file containing your huge dump:

$ sed -n -e '/CREATE TABLE.*mytable/,/CREATE TABLE/p' mysql.dump > mytable.dump


17
down vote
This can be done more easily? This is how I did it:

Create a temporary database (e.g. restore):

mysqladmin -u root -p create restore
Restore the full dump in the temp database:

mysql -u root -p restore < fulldump.sql
Dump the table you want to recover:

mysqldump restore mytable > mytable.sql
Import the table in another database:

mysql -u root -p database < mytable.sql

