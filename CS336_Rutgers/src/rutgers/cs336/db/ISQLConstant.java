package rutgers.cs336.db;

public interface ISQLConstant {
	// MySQL JDBC Connector URL
	//init("jdbc:mysql://localhost:3306/BuyMe", "cs336", "cs336_password");
	String MySQL_URL      = "jdbc:mysql://cs336-buyme.cf7jfkoilx7f.us-east-1.rds.amazonaws.com:3306/cs336buyme";
	String MySQL_USER_ID  = "cs336";
	String MySQL_PASSWORD = "cs336_password";


	// User
	String SQL_USER_INSERT = "INSERT INTO User (username, password, email, firstname, lastname, address, phone, active, userType) VALUES (?, ?, ?, ?, ?, ?, ?, true, ?)";
	//
	String SQL_USER_SELECT = "SELECT password, firstname, lastname, active, userType FROM User WHERE username = ?";


	// Bid
	String SQL_BID_INSERT = "INSERT Bid (bidID, offerID, buyer, price, autoRebidLimit, bidDate) SELECT ?, o.offerID, ?, ?, ?, NOW() FROM Offer o WHERE o.endDate >= NOW() AND (NOT o.minPrice > ?) AND o.offerID = ? AND o.status = 1 AND ? > (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = ?)";
	//
	String SQL_BID_SELECT = "SELECT bidID, buyer, price, bidDate FROM Bid b WHERE b.offerID = ?";


	// CategoryField
	String SQL_CATEGORYFIELD_SELECT = "SELECT categoryName, CategoryField.fieldID, fieldName, fieldType FROM CategoryField INNER JOIN Field ON CategoryField.fieldID = Field.fieldID ORDER BY categoryName, CategoryField.fieldID";

	// Offer
	String SQL_OFFER_INSERT = "INSERT INTO Offer (offerID, categoryName, seller, initPrice, increment, minPrice, conditionCode, description, startDate, endDate, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), DATE_ADD(NOW(), INTERVAL + ? DAY), 1)";

	String SQL_OFFER_SELECT = "SELECT categoryName, seller, min_price, description, startDate, endDate, status FROM Offer WHERE offerID = ?";

	String SQL_OFFER_SEARCH = "SELECT offerID, categoryName, seller, min_price, description, startDate, endDate FROM Offer WHERE status = 1 and categoryName = ? and description LIKE ?";


	// OfferField
	String SQL_OFFERFIELD_INSERT = "INSERT INTO OfferField (offerID, fieldID, fieldText) VALUES (?, ?, ?)";

	String SQL_OFFERFIELD_SELECT = "SELECT OfferField.fieldID, fieldName, fieldType, fieldText FROM OfferField INNER JOIN Field ON OfferField.fieldID = Field.fieldID WHERE OfferField.offerID = ? ORDER BY OfferField.fieldID";

	String SQL_OFFERFIELD_SEARCH = "SELECT OfferField.offerID, OfferField.fieldID, fieldName, fieldType, fieldText FROM OfferField INNER JOIN Field ON OfferField.fieldID = Field.fieldID WHERE OfferField.offerID IN (SELECT offerID FROM Offer WHERE status = 1 and categoryName = ? and description LIKE ?)";


	// Browse
	//	String BROWSE_OPEN_OFFER (including current bid, sorted by different criteria)
	//	String SEARCH_BY_CRITERIA
	//	String SHOW_BID_HISTORY (just for one)
	//	String OFFER_BID_BY_USER
	//	String SIMILAR_ITEM

	String SQL_OFFER_ALERT_CRITERION_INSERT = "INSERT INTO OfferAlertCriterion (criterionID, buyer, categoryName, triggerTxt, generateDate) VALUES (?, ?, ?, ?, NOW())";

	// Question
	String SQL_QUESTION_INSERT             = "INSERT INTO Question (questionID, userID, question, questionDate) VALUES (?, ?, ?, NOW())";
	String SQL_QUESTION_UPDATE_WITH_ANSWER = "UPDATE Question SET answer = ?, repID = ?, answerDate = NOW() WHERE questionID = ?";
	String SQL_QUESTION_QUERY_OPEN         = "SELECT questionID, userID, question, questionDate FROM Question WHERE answer IS NULL OR answer = '' LIMIT 0,1";
	String SQL_QUESTION_QUERY_BY_USER      = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question userID = ?";
	String SQL_QUESTION_QUERY_BY_REP       = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question repID = ?";
	String SQL_QUESTION_QUERY_BY_1TAGS     = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question WHERE (question LIKE ? OR answer LIKE ?)";
	String SQL_QUESTION_QUERY_BY_2TAGS     = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question WHERE (question LIKE ? OR answer LIKE ?) AND (question LIKE ? OR answer LIKE ?)";
	String SQL_QUESTION_QUERY_BY_3TAGS     = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question WHERE (question LIKE ? OR answer LIKE ?) AND (question LIKE ? OR answer LIKE ?) AND (question LIKE ? OR answer LIKE ?)";

}

/*
Project Checklist:

I. Create accounts of users; login, logout

II. Auctions
	[] Seller creates auctions and posts items for sale
		[] Set all the characteristics of the item
		[] Set closing date and time
		[*] Set a hidden minimum price (reserve)
	[] A buyer should be able to bid
		[] Manually
			[] Let the buyer set a new bid
		[*] With automatic bidding
			[*] Set a secret upper limit
			[*] Put in a higher bid automatically for the user in case someone bids higher
	[] Define the winner of the auction
		[] When the closing time has come, check if the seller has set a reserve
			[] If yes: if the reserve is higher than the last bid none is the winner.
			[] If no: whoever has the higher bid is the winner

III. Browsing and advanced search functionality
	[] Let people browse on the items and see the status of the current bidding
	[] Sort by different criteria (by type, bidding price, etc.)
	[] Search the list of items by various criteria.
	[] An user should be able to:
		[] View all the history of bids for any specific auction
		[] View the list of all auctions a specific buyer or seller has participated in
		[] View the list of "similar" items on auctions in the preceding month (and auction information about them)


BROWSE_OPEN_OFFER (including current bid, sorted by different criteria)
SEARCH_BY_CRITERIA
SHOW_BID_HISTORY (just for one)
OFFER_BID_BY_USER
SIMILAR_ITEM



IV. Alerts and messaging functions
	[] Alert the buyer that a higher bid has been placed
	[*] Alert the buyer in case someone bids more than your upper limit (for automatic bidding)
	[] Let user set an alert for specific items s/he is interested
		[] Get an alert when the item becomes available
	[] User can post questions
	[] User can search and browse questions/answers

V. Customer representatives & admin functions
	[] Admin (create an admin account ahead of time)
		[] Creates accounts for customer representatives
		[] Generates sales reports for:
			[] Total earnings
			[] Earnings per:
				[] Item
				[] Item type
				[] End-user
			[] Best-selling items
			[] Best buyers
	[] Customer representative:
		[] Answers to questions of users
		[] Edits account information, bids and auctions
		[] Removes bids
		[] Removes auctions
(Everywhere above where there is a function for creating something, one should/could have ones for modifying and deleting it; these are not listed, in order to make the description shorter. But this is an opportunity for "staged development": enhance your system (and grade :) as time allows at the end by adding these features.)
 */