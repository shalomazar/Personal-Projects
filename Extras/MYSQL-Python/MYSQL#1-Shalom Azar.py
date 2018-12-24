import matplotlib.pyplot as plt
import mysql

# fill in your username
username = 'root'
passwrd ='azaritc123'
DB ='imdb'

cnx = mysql.connector.connect(user=username, password=passwrd,database= DB)
cnxc = cnx.cursor()

#Queries
query1 = 'select year, count(year) from movies where genre = \'Action\' group by year'
query2 = 'select year, count(year) from movies where genre = \'Comedy\' group by year'
query3 = 'select year, count(year) from movies where genre = \'Drama\' group by year'

try:
    cnxc.execute(query1)
    results1 = cnxc.fetchall()
    cnxc.execute(query2)
    results2 = cnxc.fetchall()
    cnxc.execute(query3)
    results3 = cnxc.fetchall()

except:
    print "Error: unable to fecth data"

print(results1)
print(results2)
print(results3)

plt.title('Movies according to their genre grouped by year')
plt.xlabel('Year')
plt.ylabel('Genre')
plt.scatter(*zip(*results1), label=' Action Genre', c ='blue')
plt.scatter(*zip(*results2), label=' Comedy Genre', c = 'red')
plt.scatter(*zip(*results3), label=' Drama Genre', c = 'black')
plt.legend()
plt.show()

cnx.close()
