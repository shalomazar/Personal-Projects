import matplotlib
import mysql

# fill in your username
username = '/usr/local/mysql/bin/mysql -u root -p'

cnx = mysql.connector.connect(user=username, database='IMDB')

# fill in your query
query = 'Select * from X where Y'

try:
    # Execute the SQL command
    cursor.execute(sql)
    # Fetch all the rows in a list of lists.
    results = cursor.fetchall()
    # results are in an array containing the content of your query.
    # Use it as you wish ...

except:
    print "Error: unable to fecth data"

cnx.close()
