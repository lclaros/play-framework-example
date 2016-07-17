Examples slick queries

queries Examples
	val query = for {
	  coffee <- Coffees if coffee.name like "%expresso%"
	} yield (coffee.name, coffee.price)


Schema
	type Person = (Int,String,Int,Int)
	class People(tag: Tag) extends Table[Person](tag, "PERSON") {
	  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
	  def name = column[String]("NAME")
	  def age = column[Int]("AGE")
	  def addressId = column[Int]("ADDRESS_ID")
	  def * = (id,name,age,addressId)
	  def address = foreignKey("ADDRESS",addressId,addresses)(_.id)
	}
	lazy val people = TableQuery[People]

	type Address = (Int,String,String)
	class Addresses(tag: Tag) extends Table[Address](tag, "ADDRESS") {
	  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
	  def street = column[String]("STREET")
	  def city = column[String]("CITY")
	  def * = (id,street,city)
	}
	lazy val addresses = TableQuery[Addresses]

Queries in comparison
JDBC Query
	import java.sql._

	Class.forName("org.h2.Driver")
	val conn = DriverManager.getConnection("jdbc:h2:mem:test1")
	val people = new scala.collection.mutable.MutableList[(Int,String,Int)]()
	try{
	  val stmt = conn.createStatement()
	  try{

	    val rs = stmt.executeQuery("select ID, NAME, AGE from PERSON")
	    try{
	      while(rs.next()){
	        people += ((rs.getInt(1), rs.getString(2), rs.getInt(3)))
	      }
	    }finally{
	      rs.close()
	    }

	  }finally{
	    stmt.close()
	  }
	}finally{
	  conn.close()
	}

Slick Plain SQL queries
	import slick.driver.H2Driver.api._

	val db = Database.forConfig("h2mem1")

	val action = sql"select ID, NAME, AGE from PERSON".as[(Int,String,Int)]
	db.run(action)


Slick type-safe, composable queries
	import slick.driver.H2Driver.api._

	val db = Database.forConfig("h2mem1")

	val query = people.map(p => (p.id,p.name,p.age))
	db.run(query.result)

SELECT *
	sql"select * from PERSON".as[Person]

	people.result

SELECT
	sql"""
	  select AGE, concat(concat(concat(NAME,' ('),ID),')')
	  from PERSON
	""".as[(Int,String)]

	people.map(p => (p.age, p.name ++ " (" ++ p.id.asColumnOf[String] ++ ")")).result


WHERE
	people.filter(p => p.age >= 18 && p.name === "C. Vogt").result

	Aggregations (max, etc.)
	people.sortBy(p => (p.age.asc, p.name)).result
	people.sortBy(p => (p.age.asc, p.name)).result
	people.map(_.age).max.result

GROUP BY
	people.groupBy(p => p.addressId)
	       .map{ case (addressId, group) => (addressId, group.map(_.age).avg) }
	       .result

HAVING
	people.groupBy(p => p.addressId)
	       .map{ case (addressId, group) => (addressId, group.map(_.age).avg) }
	       .filter{ case (addressId, avgAge) => avgAge > 50 }
	       .map(_._1)
	       .result

	Implicit inner joins
	people.flatMap(p =>
	  addresses.filter(a => p.addressId === a.id)
	           .map(a => (p.name, a.city))
	).result

	// or equivalent for-expression:
	(for(p <- people;
	     a <- addresses if p.addressId === a.id
	 ) yield (p.name, a.city)
	).result


Explicit inner joins
	sql"""
	  select P.NAME, A.CITY
	  from PERSON P
	  join ADDRESS A on P.ADDRESS_ID = a.id
	""".as[(String,String)]


	(people join addresses on (_.addressId === _.id))
	  .map{ case (p, a) => (p.name, a.city) }.result

Outer joins (left/right/full)
	sql"""
	  select P.NAME,A.CITY
	  from ADDRESS A
	  left join PERSON P on P.ADDRESS_ID = a.id
	""".as[(Option[String],String)]

	(addresses joinLeft people on (_.id === _.addressId))
	  .map{ case (a, p) => (p.map(_.name), a.city) }.result

Subquery
	sql"""
	  select *
	  from PERSON P
	  where P.ID in (select ID
	                 from ADDRESS
	                 where CITY = 'New York City')
	""".as[Person]
	val address_ids = addresses.filter(_.city === "New York City").map(_.id)
	people.filter(_.id in address_ids).result // <- run as one query

Scalar value subquery / custom function
	sql"""
	  select * from PERSON P,
	                     (select rand() * MAX(ID) as ID from PERSON) RAND_ID
	  where P.ID >= RAND_ID.ID
	  order by P.ID asc
	  limit 1
	""".as[Person].head

	val rand = SimpleFunction.nullary[Double]("RAND")

	val rndId = (people.map(_.id).max.asColumnOf[Double] * rand).asColumnOf[Int]

	people.filter(_.id >= rndId)
	       .sortBy(_.id)
	       .result.head

insert
	sqlu"""
	  insert into PERSON (NAME, AGE, ADDRESS_ID) values ('M Odersky', 12345, 1)
	"""

	people.map(p => (p.name, p.age, p.addressId)) += ("M Odersky",12345,1)

update
	sqlu"""
	  update PERSON set NAME='M. Odersky', AGE=54321 where NAME='M Odersky'
	"""

	people.filter(_.name === "M Odersky")
	       .map(p => (p.name,p.age))
	       .update(("M. Odersky",54321))

delete
	sqlu"""
	  delete PERSON where NAME='M. Odersky'
	"""

	people.filter(p => p.name === "M. Odersky")
	       .delete


CASE
	sql"""
	  select
	    case
	      when ADDRESS_ID = 1 then 'A'
	      when ADDRESS_ID = 2 then 'B'
	    end
	  from PERSON P
	""".as[Option[String]]

	people.map(p =>
	  Case
	    If(p.addressId === 1) Then "A"
	    If(p.addressId === 2) Then "B"
	).result


