scala list examples

	scala> val nums = List(5, 1, 4, 3, 2)
	nums: List[Int] = List(5, 1, 4, 3, 2)

	scala> nums.filter(_ > 2)
	res0: List[Int] = List(5, 4, 3)

	val originalList = List(5, 1, 4, 3, 2)
	val newList = originalList.filter(_ > 2)

	scala> nums.filter(_ % 2 == 0)
	res21: List[Int] = List(4, 2)

	scala> nums.filter(_ % 2 == 0).sort(_ < _)
	warning: there were 1 deprecation warnings; re-run with -deprecation for details
	res22: List[Int] = List(2, 4)

filter method examples with a List of Strings

	val fruits = List("orange", "peach", "apple", "banana")

	scala> fruits.filter(_.length > 5)
	res21: List[java.lang.String] = List(banana, orange)

	scala> fruits.filter(_.startsWith("a"))
	res22: List[java.lang.String] = List(apple)

	Combining filter, sort, and map

	trait Person {
	  def first: String
	  def age: Int
	  def valid: Boolean
	}

	Returns the first name of 'valid' persons, sorted by age

	def validByAge(in: List[Person]) =
	  in.filter(_.valid).
	  sort(_.age < _.age).
	  map(_.first)


	scala> case class Person(first: String, last: String, mi: String)
	defined class Person

	scala> val fred = Person("Fred", "Flintstone", "J")
	fred: Person = Person(Fred,Flintstone,J)

	scala> val wilma = Person("Wilma", "Flintstone", "A")
	wilma: Person = Person(Wilma,Flintstone,A)

	scala> val barney = Person("Barney", "Rubble", "J")
	barney: Person = Person(Barney,Rubble,J)

	scala> val betty = Person("Betty", "Rubble", "A")
	betty: Person = Person(Betty,Rubble,A)

	scala> val peeps = Seq(fred, wilma, barney, betty)
	peeps: Seq[Person] = List(Person(Fred,Flintstone,J), Person(Wilma,Flintstone,A), Person(Barney,Rubble,J), Person(Betty,Rubble,A))

	scala> peeps.filter(_.last == "Flintstone").map(_.first)
	res0: Seq[String] = List(Fred, Wilma)

	scala> peeps.filter(_.last == "Flintstone").map(person => person.first)
	res1: Seq[String] = List(Fred, Wilma)





