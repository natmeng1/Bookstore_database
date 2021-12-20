create table Book
	(book_id		varchar(15),
	 author_id		varchar(7),
	 publisher_id		varchar(7),
     book_name varchar(20),
     ISBN varchar(20),
     genre varchar (10),
     purchase_price DECIMAL(4,2),
     sale_price DECIMAL(4,2) check (sale_price > 0) ,
     no_of_pages numeric(4),
     quantity numeric(4) check (quantity >= 0),
	 primary key (book_id, author_id, publisher_id)
	);

create table Publisher
	(book_id		varchar(15),
	 publisher_id		varchar(7),
     publisher_name varchar(20),
     street_name varchar(20),
     unit_num numeric(4),
     city varchar(20),
     country varchar(20),
     phone_num varchar(20),
     direct_deposit numeric(5),
	 primary key (book_id, publisher_id)
	);

create table Sales
	(book_id		varchar(15),
	 author_id		varchar(7),
     genre varchar(20),
     sale_price DECIMAL(4,2),
     purchase_price DECIMAL(4,2)
	);

create table Author
	(
	 author_id		varchar(7),
     fname varchar(20),
     lname varchar(20),
	 primary key (author_id)
	);

create table Orders
	(
	 order_number		numeric(7),
     fname varchar(20),
     lname varchar(20),
	 primary key (order_number)
	);

create table Tracking
(
	 order_number		numeric(7),
     the_day varchar(20),
     the_month varchar(20),
     the_year varchar(20),
     city varchar(20),
     country varchar (20),
	 primary key (order_number)
	);

create table Book_order
(
	 order_number		numeric(7),
     book_id varchar(20),
     author_id varchar(20),
	 primary key (order_number,book_id, author_id )
	);

create table Users
(
     username varchar(20),
     the_password varchar(20),
     phone_num varchar(20),
     s_street_name varchar(20),
     s_postal varchar(20),
     s_city varchar(20),
     s_country varchar(20),
     b_street_name varchar(20),
     b_postal varchar(20),
     b_city varchar(20),
     b_country varchar(20),
	 primary key (username)
	);

create table Book_author
	(
	 author_id		varchar(20),
     book_id varchar(20),
	 primary key (author_id,book_id)
	);